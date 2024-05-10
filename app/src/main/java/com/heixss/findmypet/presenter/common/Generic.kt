package com.heixss.findmypet.presenter.common


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.heixss.findmypet.common.utils.ImageUtils
import kotlinx.coroutines.flow.Flow


@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent:(T) -> Unit){
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collect(onEvent)
        }
    }
}

@Composable
fun CameraGalleryChooser(imageUtils: ImageUtils) {

    var currentPhoto by remember {
        mutableStateOf<String?>(null)
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data?.data
            currentPhoto = if (data == null) {
                // Camera intent
                imageUtils.currentPhotoPath
            } else {
                // Gallery Pick Intent
                imageUtils.getPathFromGalleryUri(data)
            }
        }
    }

    Column {
        if (currentPhoto != null) {
            // You can also use a normal Image by decoding the currentPhoto to a Bitmap using the BitmapFactory
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentPhoto)
                    .crossfade(true)
                    .build(),

                contentDescription = "photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .height(100.dp)
                    .width(100.dp)
                    .clickable {
                        launcher.launch(imageUtils.getIntent())
                    },
            )
        } else {
            IconButton(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
                onClick = {
                    launcher.launch(imageUtils.getIntent())
                },
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "uploadPic",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CameraGalleryChooserPreview() {
    val context = LocalContext.current
    val imageUtils = ImageUtils(context)
    CameraGalleryChooser(imageUtils)
}

@Composable
fun SearchWidget(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .semantics {
                contentDescription = "SearchWidget"
            },
        color = MaterialTheme.colorScheme.background
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "TextField"
                },
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    text = "Search here...",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {
                        onSearchClicked(text)
                        focusManager.clearFocus(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .semantics {
                            contentDescription = "CloseButton"
                        },
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                            focusManager.clearFocus(true)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                    focusManager.clearFocus(true)
                }
            )
        )
    }
}

@Composable
@Preview
fun SearchWidgetPreview() {
    SearchWidget(
        text = "Search",
        onTextChange = {},
        onSearchClicked = {},
        onCloseClicked = {}
    )
}

@Composable
fun EditButton(modifier: Modifier, isLoading: Boolean, isEditing: Boolean, onClick: () -> Unit, onClose: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.Center
    ) {
        if (isEditing)
            IconButton(
                onClick = {
                    onClose()
                },
                enabled = !isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Edit Icon",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        IconButton(
            onClick = {
                onClick()
            },
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Icon(
                    imageVector = if (!isEditing) Icons.Default.Edit else Icons.Default.Check,
                    contentDescription = "Edit Icon",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}