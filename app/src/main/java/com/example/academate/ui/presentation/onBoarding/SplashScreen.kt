package com.example.academate.ui.presentation.onBoarding

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import com.example.academate.ui.theme.Biru
import kotlinx.coroutines.delay

private const val TAG = "SplashScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        try {
            Log.d(TAG, "SplashScreen started")

            scale.animateTo(
                targetValue = 0.3f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )

            Log.d(TAG, "Animation completed, waiting 3 seconds...")
            delay(3000L)

            Log.d(TAG, "Navigating to OnBoarding1...")
            navController.navigate(Route.ONBOARDING1) {
                popUpTo(Route.SPLASHSCREEN) { inclusive = true }
            }
            Log.d(TAG, "Navigation triggered successfully")

        } catch (e: Exception) {
            Log.e(TAG, "Error in SplashScreen: ${e.message}", e)
        }
    }
    Box() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.blue1)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Image(
                painter = painterResource(id = R.drawable.logooo),
                contentDescription = "logo",
                modifier = Modifier.size(230.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "AcadeMate",
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold ,
//                color = colorResource(id = R.color.white)
//            )
        }
    }
}