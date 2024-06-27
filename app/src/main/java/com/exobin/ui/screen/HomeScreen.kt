package com.exobin.ui.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.exobin.ui.theme.DarkPrimaryGreen
import com.exobin.ui.theme.LightPrimaryGreen
import com.exobin.ui.theme.darkTextPrimary
import com.exobin.ui.theme.lightTextPrimary
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.exobin.component.CustomCircularProgressIndicator
import com.exobin.ui.theme.darkGray
import com.exobin.ui.theme.gray
import com.exobin.ui.theme.orange
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exobin.R
import com.exobin.ui.theme.darkShapePrimary
import com.exobin.ui.theme.lightShapePrimary
import com.exobin.ui.theme.onSurface
import com.exobin.ui.theme.surface
import com.exobin.ui.theme.white
import com.exobin.ui.viewmodel.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun HomeScreen(
    navController: NavController,
    modifiers: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val textColor = if (isDarkTheme) lightTextPrimary else darkTextPrimary
    val buttonColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val borderColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen
    val backgroundBottomColor = if (isDarkTheme) gray else onSurface

    val garbagePercentage = viewModel.garbagePercentage.observeAsState(0)
    val trashCount by viewModel.trashCount.observeAsState(0)

    LaunchedEffect(key1 = true) {
        systemUiController.setSystemBarsColor(
            color = Color(0xFF000000).copy(alpha = 0.3f),
            darkIcons = !isDarkTheme
        )
    }

    LaunchedEffect(key1 = garbagePercentage) {
        garbagePercentage?.let {
            println("Garbage percentage updated: $it")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row for profile image and notification bell
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Profile image
            Image(
                painter = painterResource(id = R.drawable.gambarku2), // Replace with actual resource ID
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp) // Set the size to 40dp
                    .clip(CircleShape)
            )
            // Title Text with notification bell icon
            Text(
                text = "ExoBin",
                color = textColor,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .weight(1f) // Fill remaining space in the row
                    .align(Alignment.CenterVertically), // Align vertically to center
                textAlign = TextAlign.Center // Center the text horizontally
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(40.dp) // Set the size to 40dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown Menu
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf("Tempat Sampah 1") }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expanded = !expanded }
            ) {
                Text(
                    text = selectedOption,
                    color = textColor,
                    fontWeight = FontWeight.Bold, // Making the text bold
                    fontSize = 18.sp, // Adjusting the font size
                    modifier = Modifier.padding(vertical = 30.dp) // Adding some left padding
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = textColor
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center) // Center the dropdown list
                    .background(gray.copy(alpha = 0.5f))
                    .animateContentSize(animationSpec = tween(durationMillis = 300)),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        "Tempat Sampah 1",
                        "Tempat Sampah 2",
                        "Tempat Sampah 3",
                        "Tempat Sampah 4",
                        "Tempat Sampah 5"
                    ).forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption = option
                            expanded = false
                        }) {
                            Text(
                                text = option,
                                color = textColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Custom Circular Progress Indicator
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomCircularProgressIndicator(
                    modifier = Modifier
                        .size(250.dp)
                        .background(backgroundColor),
                    initialValue = garbagePercentage.value,
                    primaryColor = borderColor,
                    secondaryColor = gray,
                    circleRadius = 230f,
                    onPositionChange = {}
                )

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Level Sampah",
                    color = textColor,
                    style = MaterialTheme.typography.h6 // Adjust the style as needed
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(130.dp)
                        .background(
                            color = backgroundBottomColor,
                            shape = RoundedCornerShape(16.dp) // RoundedCornerShape dengan radius 16.dp untuk keempat sisi
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Realtime Conditions",
                            color = textColor,
                            style = MaterialTheme.typography.h6,
                            fontSize = 15.sp,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            MiniCircularProgressIndicator(
                                progress = 33,
                                color = MaterialTheme.colors.primary,
                                text = "Suhu"
                            )
                            MiniCircularProgressIndicator(
                                progress = 14,
                                color = Color.Red,
                                text = "Kelembapan"
                            )
                            MiniCircularProgressIndicator(
                                progress = trashCount,
                                color = Color.Green,
                                text = "Jumlah"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniCircularProgressIndicator(
    progress: Int,
    color: Color,
    text: String
) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) lightTextPrimary else darkTextPrimary
    val strokeWidth = 6.dp // Adjust stroke width as desired
    val borderColor = if (isDarkTheme) darkShapePrimary else lightShapePrimary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp) // Adjust size as needed
                .background(borderColor.copy(alpha = 1f), shape = CircleShape)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(strokeWidth / 2), // Adjust padding based on stroke width
                color = color,
                progress = progress.toFloat() / 100f,
                strokeWidth = strokeWidth
            )

            // Draw progress value in the center
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$progress",
                    color = textColor,
                    fontSize = 20.sp, // Adjust font size as desired
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(text = text, color = textColor)
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}

