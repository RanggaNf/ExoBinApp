package com.exobin

import android.media.SoundPool
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exobin.data.repository.HomeRepository
import com.exobin.navigation.Actions
import com.exobin.navigation.Destination
import com.exobin.ui.screen.AboutScreen
import com.exobin.ui.screen.HomeScreen
import com.exobin.ui.screen.LoginScreen
import com.exobin.ui.screen.RegisterScreen
import com.exobin.ui.screen.SettingsScreen
import com.exobin.ui.screen.StartScreen
import com.exobin.ui.screen.UserScreen
import com.exobin.ui.theme.DarkPrimaryGreen
import com.exobin.ui.theme.LightPrimaryGreen
import com.exobin.ui.theme.darkTextPrimary
import com.exobin.ui.theme.gray
import com.exobin.ui.theme.lightTextPrimary
import com.exobin.ui.theme.white
import com.exobin.ui.viewmodel.HomeViewModel
import com.exobin.utils.PreferenceUtil
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ExoBinNavigation() {
    val isDarkTheme = isSystemInDarkTheme()
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    val selectedIndex = remember { mutableStateOf(0) }
    val shouldShowBottomBar = remember { mutableStateOf(true) }
    val shouldShowTopBar = remember { mutableStateOf(true) }
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val context = LocalContext.current

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, destination, _ ->
            shouldShowBottomBar.value = when (destination?.route) {
                Destination.StartScreen,
                Destination.LoginScreen,
                Destination.RegisterScreen -> false
                else -> true
            }
            shouldShowTopBar.value = shouldShowBottomBar.value
            // Update selectedIndex based on the destination
            selectedIndex.value = when (destination?.route) {
                Destination.HomeScreen -> 0
                Destination.UserScreen -> 1
                Destination.AboutScreen -> 2
                Destination.SettingsScreen -> 3
                else -> selectedIndex.value
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    // Determine if user is already logged in
    val isLoggedIn = PreferenceUtil.isLoggedIn(context)
    if (isLoggedIn) {
        LaunchedEffect(key1 = Unit) {
            navController.navigate(Destination.HomeScreen)
        }
    }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar.value) {
                    CustomsBottomBar(navController, selectedIndex)
                }
            },
            backgroundColor = backgroundColor
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Destination.StartScreen,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Destination.StartScreen) {
                    StartScreen(navController = navController)
                }
                composable(Destination.LoginScreen) {
                    LoginScreen(navController = navController)
                }
                composable(Destination.RegisterScreen) {
                    RegisterScreen(navController = navController)
                }
                composable(Destination.HomeScreen) {
                    HomeScreen(navController = navController)
                }
                composable(Destination.UserScreen) {
                    val viewModel = remember { HomeViewModel(homeRepository = HomeRepository()) } // No need for manual injection with Hilt
                    UserScreen(navController = rememberNavController(), viewModel = viewModel)
                }
                composable(Destination.SettingsScreen) {
                    SettingsScreen(navController = navController, context = context, selectedIndex = selectedIndex)
                }
                composable(Destination.AboutScreen) {
                    AboutScreen(navController = navController)
                }
            }
        }
    }
}




@Composable
fun CustomsBottomBar(navController: NavHostController, selectedIndex: MutableState<Int>) {
    val listItems = listOf("Home", "User", "About", "Settings")
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val textColor = if (isDarkTheme) lightTextPrimary else darkTextPrimary
    val buttonColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val backgroundBottomColor = if (isDarkTheme) gray else white

    Card(
        elevation = 8.dp, // Tambahkan elevation untuk shadow
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(70.dp)
    ) {
        BottomNavigation(backgroundColor = backgroundBottomColor) { // Atur warna latar belakang
            listItems.forEachIndexed { index, label ->
                val isSelected = selectedIndex.value == index
                BottomNavigationItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.padding(top = 12.dp),
                        ) {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Filled.Home
                                    1 -> Icons.Filled.SupervisedUserCircle
                                    2 -> Icons.Filled.Info
                                    3 -> Icons.Filled.Settings
                                    else -> Icons.Filled.Error // Handle unexpected index
                                },
                                contentDescription = null,
                                tint = if (isSelected) buttonColor else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = label,
                                style = TextStyle(fontSize = 12.sp),
                                color = textColor
                            )
                        }
                    },
                    selected = isSelected,
                    onClick = {
                        selectedIndex.value = index // Update selected index
                        // Navigasi sesuai dengan indeks yang dipilih
                        when (index) {
                            0 -> navController.navigate(Destination.HomeScreen)
                            1 -> navController.navigate(Destination.UserScreen)
                            3 -> navController.navigate(Destination.SettingsScreen)
                            2 -> navController.navigate(Destination.AboutScreen)
                            // Tambahkan navigasi untuk item lain jika diperlukan
                        }
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}
