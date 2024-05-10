package com.heixss.findmypet.presenter.searchpets

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.heixss.findmypet.domain.searchpets.Pet
import com.heixss.findmypet.presenter.common.SearchWidget

@Composable
fun SearchPetsScreen(
    pets: LazyPagingItems<Pet>,
    searchQuery: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchWidget(
                text = searchQuery,
                onTextChange = {
                    onTextChange(it)
                },
                onSearchClicked = {
                    onSearchClicked(it)
                },
                onCloseClicked = {
                    onCloseClicked()
                }
            )
        },
        content = {
            PetListScreen(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(5.dp),
                pets = pets
            )
        }
    )
}

@Composable
fun PetListScreen(
    modifier: Modifier = Modifier,
    pets: LazyPagingItems<Pet>
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = pets.loadState) {
        if (pets.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (pets.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()

        }
    }
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(count = pets.itemCount) { index ->
                pets[index]?.let { pet ->
                    PetItem(
                        pet = pet,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                if (pets.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
