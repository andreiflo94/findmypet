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

data class AddPetScreenState(
    val petTypeOptions: List<String> = listOf("Cat", "Dog", "Parrot", "Rabbit", "Hamster", "Fish", "Turtle", "Snake"),
    val petSexOptions: List<String> = listOf("Male", "Female"),
    val selectedPetType: String = "Cat",
    val selectedPetSex: String = "Male",
    val name: String = "",
    val breed: String = ""
)

@Composable
fun AddPetScreen(
    addPetScreenState: AddPetScreenState,
    onSexSelected: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onBreedChanged: (String) -> Unit
) {
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

            // Pass the options and current selection to PetDetails composable
            PetDetails(
                petSexOptions = addPetScreenState.petSexOptions,
                petTypeOptions = addPetScreenState.petTypeOptions,
                selectedPetSex = addPetScreenState.selectedPetSex,
                selectedPetType = addPetScreenState.selectedPetType,
                name = addPetScreenState.name,
                breed = addPetScreenState.breed,
                onTypeSelected = onTypeSelected,
                onSexSelected = onSexSelected,
                onNameChanged = onNameChanged,
                onBreedChanged = onBreedChanged
            )
        }
    }
}

@Composable
fun PetDetails(
    petSexOptions: List<String>,
    petTypeOptions: List<String>,
    selectedPetSex: String,
    selectedPetType: String,
    name: String,
    breed: String,
    onSexSelected: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onBreedChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, end = 16.dp, start = 16.dp)
    ) {
        PetTypeDropdown(selectedType = selectedPetType, petTypeOptions) {
            onTypeSelected.invoke(it)
        }
        PetDetailsItem(label = "name", value = name){
            onNameChanged.invoke(it)
        }
        PetSexDropdown(selectedSex = selectedPetSex, petSexOptions) {
            onSexSelected.invoke(it)
        }
        PetDetailsItem(label = "breed", value = breed){
            onBreedChanged.invoke(it)
        }
    }
}

@Composable
fun PetDetailsItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onValueChange.invoke(it) },
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
    selectedSex: String, petSexOptions: List<String>, onSexSelected: (String) -> Unit
) {

    DropDown(selectedValue = selectedSex, values = petSexOptions, onValueSelected = onSexSelected)
}

@Composable
fun PetTypeDropdown(
    selectedType: String, petTypeOptions: List<String>, onTypeSelected: (String) -> Unit
) {


    DropDown(
        selectedValue = selectedType, values = petTypeOptions, onValueSelected = onTypeSelected
    )
}

@Preview(showBackground = true)
@Composable
fun AddPetScreenPreview() {
    // Mock data for preview
    val previewState = AddPetScreenState(
        petSexOptions = listOf("Male", "Female"),
        petTypeOptions = listOf("Cat", "Dog", "Parrot"),
        selectedPetSex = "Male",
        selectedPetType = "Cat"
    )

    // Mock functions for preview (no actual logic, just empty lambdas)
    AddPetScreen(
        addPetScreenState = previewState,
        onSexSelected = {},
        onTypeSelected = {},
        onNameChanged = {},
        onBreedChanged = {}
    )
}