package com.exobin.ui.screen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exobin.ui.theme.DarkPrimaryGreen
import com.exobin.ui.theme.LightPrimaryGreen
import com.exobin.ui.theme.darkTextPrimary
import com.exobin.ui.theme.lightTextPrimary
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.exobin.navigation.Destination
import com.exobin.ui.theme.gray
import com.exobin.ui.theme.onSurface
import com.exobin.ui.viewmodel.AuthViewModel


@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    context: Context,
    modifiers: Modifier = Modifier,
    selectedIndex: MutableState<Int> // Add this parameter
) {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val buttonColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val borderColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen

    // State for showing logout confirmation dialog
    var showLogoutDialog by remember { mutableStateOf(false) }

    systemUiController.setSystemBarsColor(color = backgroundColor)

    // Provide the context using CompositionLocalProvider
    CompositionLocalProvider(LocalContext provides context) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // Show logout confirmation dialog when logout button is clicked
                        showLogoutDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Text(text = "Logout", color = Color.White)
                }
            }
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirmLogout = {
                // Logout action
                viewModel.logout(context)
                // Close the dialog
                showLogoutDialog = false
                // Navigate to LoginScreen after logout
                navController.navigate(Destination.LoginScreen) {
                    // Pop up to the start destination of the graph to
                    // remove all back stack entries
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                // Reset selectedIndex to Home
                selectedIndex.value = 0
            },
            onDismiss = {
                // Dismiss the dialog if user clicks Cancel
                showLogoutDialog = false
            }
        )
    }

    // Deklarasi LaunchedEffect untuk melakukan navigasi saat nilai authState berubah
    LaunchedEffect(viewModel.authState) {
        val authState = viewModel.authState.value
        if (authState == null) {
            navController.navigate(Destination.StartScreen) {
                // Pop up to the start destination of the graph to
                // remove all back stack entries
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val textColor = if (isDarkTheme) lightTextPrimary else darkTextPrimary
    val buttonColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val borderColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val backgroundBottomColor = if (isDarkTheme) gray else onSurface

    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        title = {
            Text(
                text = "Konfirmasi Logout",
                color = textColor
            )
        },
        text = {
            Text(
                text = "Apakah Anda yakin ingin logout?",
                color = textColor
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirmLogout.invoke() },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, borderColor)
            ) {
                Text(text = "Ya", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss.invoke() },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, borderColor)
            ) {
                Text(text = "Batal", color = Color.White)
            }
        },
        backgroundColor = backgroundBottomColor
    )
}
