package com.heixss.findmypet.presenter.bottomnav

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.heixss.findmypet.presenter.common.NavigationEvent
import com.heixss.findmypet.presenter.common.ObserveAsEvents
import com.heixss.findmypet.presenter.common.Screen
import com.heixss.findmypet.presenter.profile.UserProfileScreen
import com.heixss.findmypet.presenter.profile.UserProfileViewModel
import com.heixss.findmypet.presenter.searchpets.PetsViewModel
import com.heixss.findmypet.presenter.searchpets.SearchPetsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavScreen(
    logOut: () -> Unit
) {
    val navController = rememberNavController()
    var navigationSelectedItem by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find my pet") },
            )
        },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        label = {
                            Text(navigationItem.label)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Screen.Home.route) {
                //call our composable screens here
            }
            composable(Screen.Profile.route) {
                val viewModel = hiltViewModel<UserProfileViewModel>()

                UserProfileScreen(
                    profileScreenState = viewModel.state.collectAsStateWithLifecycle(),
                    onEditClick = { firstName, lastName, address, phone, email ->
                        Log.d("EditClickLog", "First Name: $firstName")
                        Log.d("EditClickLog", "Last Name: $lastName")
                        Log.d("EditClickLog", "Address: $address")
                        Log.d("EditClickLog", "Phone: $phone")
                        Log.d("EditClickLog", "Email: $email")

                        viewModel.toggleEditMode(
                            firstName = firstName,
                            lastName = lastName,
                            address = address,
                            phone = phone,
                            email = email
                        )
                    },
                    onCancelEdit = {
                        viewModel.cancelEdit()
                    },
                    logOut = {
                        viewModel.logOut()
                    }
                )
                ObserveAsEvents(flow = viewModel.logOutChannelFlow) { event ->
                    when (event) {
                        NavigationEvent.LogIn -> {
                            logOut()
                        }

                        else -> {}
                    }
                }
            }
            composable(Screen.Search.route) {
                val viewModel = hiltViewModel<PetsViewModel>()
                val searchQuery by viewModel.searchQuery
                val pets = viewModel.searchedPets.collectAsLazyPagingItems()
                SearchPetsScreen(pets,
                    searchQuery,
                    onTextChange = {
                        viewModel.updateSearchQuery(it)
                    },
                    onSearchClicked = {
                        viewModel.searchPets(it)
                    },
                    onCloseClicked = {

                    })
            }
        }
    }
}