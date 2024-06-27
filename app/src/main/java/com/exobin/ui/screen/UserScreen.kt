package com.exobin.ui.screen

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exobin.ui.theme.DarkPrimaryGreen
import com.exobin.ui.theme.LightPrimaryGreen
import com.exobin.ui.theme.darkTextPrimary
import com.exobin.ui.theme.lightTextPrimary
import com.exobin.ui.viewmodel.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun UserScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    modifers: Modifier = Modifier
){

    // Ubah sistem UI sesuai tema
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val textColor = if (isDarkTheme) lightTextPrimary else darkTextPrimary
    val buttonColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val borderColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen

    systemUiController.setSystemBarsColor(color = backgroundColor)

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
            val garbagePercentage = viewModel.garbagePercentage.observeAsState(0)

            Text(
                text = "Presentase sampah = ${garbagePercentage.value} %",
                color = textColor,
                style = MaterialTheme.typography.h4,
                fontSize = 12.sp
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle button click */ },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, borderColor)
            ) {
                Text(text = "Click Me", color = Color.White)
            }
        }
    }
}

