package com.example.academate.ui.presentation.be_a_mentor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import com.example.academate.ui.presentation.login_screen.UserViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMentor(navController: NavController, viewModelUser: UserViewModel){
    // mengambil username current user
    val username by viewModelUser.username.collectAsState()

    // inisialisasi database
    var database = FirebaseDatabase.getInstance("https://academate-c7250-default-rtdb.firebaseio.com")
    var mentorRef = database.getReference("mentors") // pointer untuk root mentor
    val currentUser = database.getReference("users").child(username) // pointer ke current user

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
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
    ) {
        var name by remember { mutableStateOf("") }
        var course by remember { mutableStateOf("") }
        var experience by remember { mutableStateOf("") }
        var motivation by remember { mutableStateOf("") }

        Column (
            modifier = Modifier
                .fillMaxSize()
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ){
                Row (
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp)
                ){
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Be a Mentor",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column (
                    modifier = Modifier
//                                .height(50.dp)
                        .background(
                            Color.White.copy(alpha = .5f),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp))
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 30.dp)

                ){
                    Column {
                        Text(
                            text = "Nama Lengkap",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = {text ->
                                name = text
                            },
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                                .heightIn(min = 40.dp, max = 60.dp)
                                .padding(top = 8.dp),
                            placeholder = { Text(
                                text = "Nama Lengkap", fontSize = 12.sp) },
                            textStyle = TextStyle(
                                fontSize = 12.sp
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Column {
                        Text(
                            text = "Mata Pelajaran yang Ingin Diajarkan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,

                            )
                        OutlinedTextField(
                            value = course,
                            onValueChange = {text ->
                                course = text
                            },
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                                .heightIn(min = 40.dp, max = 60.dp)
                                .padding(top = 8.dp),
                            placeholder = { Text(
                                text = "Mata Pelajaran", fontSize = 12.sp) },
                            textStyle = TextStyle(
                                fontSize = 12.sp
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Column {
                        Text(
                            text = "Pengalaman",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,

                            )
                        OutlinedTextField(
                            value = experience,
                            onValueChange = {text ->
                                experience = text
                            },
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                                .heightIn(min = 40.dp, max = 60.dp)
                                .padding(top = 8.dp),
                            placeholder = { Text(
                                text = "Deskripsi singkat pengalaman yang relevan", fontSize = 12.sp) },
                            textStyle = TextStyle(
                                fontSize = 12.sp
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Column {
                        Text(
                            text = "Motivasi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,

                            )
                        OutlinedTextField(
                            value = motivation,
                            onValueChange = {text ->
                                motivation = text
                            },
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                                .heightIn(min = 40.dp, max = 60.dp)
                                .padding(top = 8.dp),
                            placeholder = { Text(
                                text = "Alasan tertarik menjadi mentor", fontSize = 12.sp)
                            },
                            textStyle = TextStyle(
                                fontSize = 12.sp
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    var isChecked by remember {
                        mutableStateOf(false)
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it }
                        )
                        Text(
                            text = "Dengan mengajukan formulir ini, saya menyatakan bahwa informasi yang saya berikan adalah benar.",
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            color = Color.Black.copy(alpha = .5f)
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            // update key member dari user
                            val hashMap = hashMapOf<String, Any>() // Membuat HashMap kosong dengan kunci bertipe String dan nilai bertipe Any
                            hashMap["mentor"] = true // membuat nilai baru dan ditaruh ke hashMap
                            // update nilai member
                            currentUser.updateChildren(hashMap)

                            // membuat user baru di root "users"
                            var userRef = mentorRef.child(username)
                            var mentor = Mentor(name, course, experience)
                            userRef.setValue(mentor)

//                            viewModelUser.addToNameList(name)
//                            viewModelUser.addToCourseList(course)
                            viewModelUser.addMentorDetail(name, course)

                            navController.navigate(Route.PEMBERITAHUANBEMENTOR)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1))
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}

// untuk menyimpan data email dan password, yang akan dimasukkan ke database (logic ada di  onClick button
data class Mentor(var nama_lengkap: String, var course: String, var experience: String, var poin:Int = 0)

