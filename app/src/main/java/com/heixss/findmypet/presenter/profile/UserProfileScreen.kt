package com.heixss.findmypet.presenter.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.heixss.findmypet.common.utils.ImageUtils
import com.heixss.findmypet.domain.profile.User
import com.heixss.findmypet.domain.searchpets.Pet
import com.heixss.findmypet.presenter.common.CameraGalleryChooser
import com.heixss.findmypet.presenter.common.EditButton


data class ProfileScreenState(
    var isLoading: Boolean,
    val user: User?,
    var isEditing: Boolean,
    var errorMsg: String?
)

@Composable
fun UserProfileScreen(
    profileScreenState: State<ProfileScreenState>, onEditClick: (
        firstName: String, lastName: String, address: String, phone: String, email: String
    ) -> Unit,
    onAddPet: () -> Unit,
    onCancelEdit: () -> Unit,
    logOut: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val user = profileScreenState.value.user
            val isEditing = profileScreenState.value.isEditing
            val isLoading = profileScreenState.value.isLoading
            var firstName = user?.firstName ?: ""
            var lastName = user?.lastName ?: ""
            var address = user?.address ?: ""
            var phone = user?.phone ?: ""
            var email = user?.email ?: ""
            val imageUtils = ImageUtils(LocalContext.current)

            if (isLoading && user == null) {
                CircularProgressIndicator()
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CameraGalleryChooser(imageUtils)
                    Column(modifier = Modifier.align(Alignment.Bottom)) {
                        IconButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                logOut()
                            },
                            enabled = !isLoading
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Log out",
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        EditButton(modifier = Modifier
                            .padding(end = 8.dp),
                            isLoading = isLoading,
                            isEditing = isEditing,
                            onClick = {
                                onEditClick(firstName, lastName, address, phone, email)
                            },
                            onClose = {
                                onCancelEdit()
                            })
                    }
                }
                UserDetails(firstName = user?.firstName,
                    lastName = user?.lastName,
                    address = user?.address,
                    phone = user?.phone,
                    email = user?.email,
                    profileScreenState.value.isLoading,
                    profileScreenState.value.isEditing,
                    onFirstNameChanged = {
                        firstName = it
                    },
                    onLastNameChanged = {
                        lastName = it
                    },
                    onAddressChanged = {
                        address = it
                    },
                    onPhoneChanged = {
                        phone = it
                    },
                    onEmailChanged = {
                        email = it
                    }

                )
                user?.pets?.let {
                    PetCarousel(pets = it, onAddPet)
                }
            }
        }

    }
}


@Composable
fun UserDetails(
    firstName: String?,
    lastName: String?,
    address: String?,
    phone: String?,
    email: String?,
    isLoading: Boolean,
    isEditing: Boolean,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onAddressChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, end = 16.dp, start = 16.dp)
    ) {
        firstName?.let {
            UserInfoItem(
                label = "First Name",
                vParameter = it,
                isLoading = isLoading,
                isEditing = isEditing,
                onValueChanged = onFirstNameChanged
            )
        }
        lastName?.let {
            UserInfoItem(
                label = "Last Name",
                vParameter = it,
                isLoading = isLoading,
                isEditing = isEditing,
                onValueChanged = onLastNameChanged,
            )
        }
        address?.let {
            UserInfoItem(
                label = "Address",
                vParameter = it,
                isLoading = isLoading,
                isEditing = isEditing,
                onValueChanged = onAddressChanged
            )
        }
        phone?.let {
            UserInfoItem(
                label = "Phone",
                vParameter = it,
                isLoading = isLoading,
                isEditing = isEditing,
                onValueChanged = onPhoneChanged
            )
        }
        email?.let {
            UserInfoItem(
                label = "Email",
                vParameter = it,
                isLoading = isLoading,
                isEditing = isEditing,
                onValueChanged = onEmailChanged
            )
        }
    }
}

@Composable
fun UserInfoItem(
    label: String,
    vParameter: String,
    isLoading: Boolean,
    isEditing: Boolean,
    onValueChanged: (String) -> Unit
) {
    var value by remember { mutableStateOf(vParameter) }
    TextField(
        value = if (isEditing) value else vParameter,
        onValueChange = {
            value = it
            onValueChanged(it)
        },
        label = {
            Text(
                text = "$label: ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = {}),
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading && isEditing
    )
}

@Composable
fun PetCarousel(pets: List<Pet>, onAddPet: () -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            AddPet(onAddPet)
        }
        items(pets) { pet ->
            PetItem(pet)
        }
    }
}

@Composable
fun PetItem(pet: Pet) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .wrapContentWidth(), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(pet.petPhoto)
                    .crossfade(true).build(),

                contentDescription = "photo", contentScale = ContentScale.Crop, modifier = Modifier
                    .clip(CircleShape)
                    .height(70.dp)
                    .width(70.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Type: ${pet.petType}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Sex: ${pet.petSex}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Breed: ${pet.petBreed}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AddPet(onAddPet: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .wrapContentWidth(), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        IconButton(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp),
            onClick = {
                onAddPet()
            },
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.AddCircle,
                contentDescription = "addPet",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


@Preview
@Composable
fun UserProfileScreenPreview() {
    val profileScreenState = remember {
        mutableStateOf(
            ProfileScreenState(
                isLoading = false, isEditing = true, user = User(
                    photo = "https://static.wikia.nocookie.net/5eb23293-19b2-4531-83fe-129a79440d43",
                    firstName = "Andrei",
                    lastName = "Florea",
                    email = "andrei.ctin.florea@gmail.com",
                    phone = "0723705870",
                    address = "Bucuresti, Sector 1, Soseaua Chitilei 242e",
                    pets = listOf(
                        Pet(
                            petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                            petType = 2,
                            description = "nice cat",
                            petSex = "M",
                            petBreed = "unkwon",
                            petName = "Apollo",
                            username = "Andrei"
                        )
                    )
                ),
                errorMsg = null
            )
        )
    }
    UserProfileScreen(profileScreenState = profileScreenState, onEditClick = { a, b, c, d, e ->

    }, onAddPet = {}, onCancelEdit = {}, logOut = {})
}

