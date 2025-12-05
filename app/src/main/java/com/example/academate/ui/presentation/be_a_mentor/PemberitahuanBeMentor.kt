package com.example.academate.ui.presentation.be_a_mentor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun PemberitahuanBeMentor(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Setting the angle in radians
                val angleRad = 60f / 180f * 3.14f

                // Fractional x
                val x = cos(angleRad)
                    .toFloat()

                // Fractional y
                val y = sin(angleRad)
                    .toFloat()

                // Set the Radius and offset as shown below
                val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
                val offset = center + Offset(x * radius, y * radius)

                // Setting the exact offset
                val exactOffset = Offset(
                    x = min(offset.x.coerceAtLeast(0f), size.width),
                    y = size.height - min(
                        offset.y.coerceAtLeast(0f),
                        size.height
                    )
                )

                // Draw a rectangle with the above values
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFB2D1FF), // Warna 2: Biru Muda (#B2D1FF)
                            Color(0xFFB2D1FF), // Warna 2: Biru Muda (#B2D1FF)
                            Color(0xFF0065FF), // Warna 1: Biru (#0065FF)
                        ),
                        start = Offset(size.width, size.height) - exactOffset,
                        end = exactOffset
                    ),
                    size = size
                )
            }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, end = 10.dp, bottom = 6.dp)
            ) {
                IconButton(onClick = {
                    navController.navigate(Route.HOME)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Be a Mentor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(200.dp))
            Column (
                modifier = Modifier
//                                .height(50.dp)
                    .background(
                        Color.White.copy(alpha = .5f),shape = RoundedCornerShape(corner = CornerSize(10.dp))
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Terima kasih sudah mengajukan diri \n" +
                            "sebagai mentor!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Saat ini, tim kami sedang melakukan pengecekan \npengajuan yang masuk. Informasi lebih lanjut akan \nkami hubungi lewat E-mail.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}