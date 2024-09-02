package com.heixss.findmypet.presenter.addpet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heixss.findmypet.common.DropDown
import com.heixss.findmypet.common.utils.ImageUtils
import com.heixss.findmypet.presenter.common.CameraGalleryChooser

@Composable
fun AddPetScreen() {
    Surface(color = colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUtils = ImageUtils(LocalContext.current)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CameraGalleryChooser(imageUtils)
            }
            PetDetails()
        }
    }
}

@Composable
fun PetDetails(
) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, end = 16.dp, start = 16.dp)
    ) {
        PetTypeDropdown(selectedType = "Cat") {

        }
        PetDetailsItem(label = "name")
        PetSexDropdown(selectedSex = "Female") {

        }
        PetDetailsItem(label = "breed")
    }
}

@Composable
fun PetDetailsItem(
    label: String
) {
    TextField(
        value = "",
        onValueChange = {},
        label = {
            Text(
                text = "$label: ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = {}),
        modifier = Modifier.fillMaxWidth(),
    )
}


@Composable
fun PetSexDropdown(
    selectedSex: String,
    onSexSelected: (String) -> Unit
) {
    val petSexOptions = listOf("Male", "Female")
    DropDown(selectedValue = selectedSex, values = petSexOptions, onValueSelected = onSexSelected)
}

@Composable
fun PetTypeDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val petTypeOptions =
        listOf("Cat", "Dog", "Parrot", "Rabbit", "Hamster", "Fish", "Turtle", "Snake")

    DropDown(
        selectedValue = selectedType,
        values = petTypeOptions,
        onValueSelected = onTypeSelected
    )
}

@Preview
@Composable
fun AddPetScreenPreview() {
    AddPetScreen()
}
