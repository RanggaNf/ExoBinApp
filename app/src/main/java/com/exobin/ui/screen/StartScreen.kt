package com.exobin.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exobin.R
import com.exobin.component.PageIndicator
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontStyle
import com.exobin.animate.pressClickEffect
import com.exobin.navigation.Destination
import com.exobin.ui.theme.DarkPrimaryGreen
import com.exobin.ui.theme.ExoBinTheme
import com.exobin.ui.theme.LightPrimaryGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val imageList = listOf(
        R.drawable.exostart,
        R.drawable.exostart1,
        R.drawable.exostart2
    )

    val pagerState = rememberPagerState(pageCount = { imageList.size })
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    var buttonClick by remember { mutableStateOf(false) }

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val pageIndicatorColor = if (isDarkTheme) Color.White else Color.Black
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val buttonColor = if (isDarkTheme) DarkPrimaryGreen else LightPrimaryGreen

    LaunchedEffect(key1 = true) {
        systemUiController.setSystemBarsColor(
            color = Color(0xFF000000).copy(alpha = 0.3f),
            darkIcons = !isDarkTheme
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = "Full-screen image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, backgroundColor),
                        startY = 2f,
                        endY = 800f
                    )
                )
        )

        PageIndicator(
            numberOfPages = imageList.size,
            selectedPage = pagerState.currentPage,
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = 350.dp),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = 140.dp, start = 25.dp, end = 25.dp)
        ) {
            Text(
                text = "ExoBin",
                color = textColor,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ecological Xperience Operations for Bin Innovation Network",
                color = textColor,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Empowering Eco-Warriors Everywhere!",
                color = textColor,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 17.sp
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Button(
            onClick = {

                navController.navigate(Destination.LoginScreen)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 25.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(50.dp)
                .pressClickEffect(),
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
            shape = RoundedCornerShape(percent = 20),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 4.dp
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = "Continue",
                color = backgroundColor,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StartScreenPreview() {
    ExoBinTheme {
        val navController = rememberNavController()
        StartScreen(navController)
    }
}



