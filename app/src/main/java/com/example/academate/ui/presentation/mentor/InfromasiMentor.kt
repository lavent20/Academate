package com.example.academate.ui.presentation.mentor

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import com.example.academate.ui.presentation.login_screen.UserViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InformasiMentor(navController: NavController, userViewModel: UserViewModel){

    val mentorName by userViewModel.mentorname.collectAsState()
    val userName by userViewModel.username.collectAsState()

    // inisialisasi database
    val database = FirebaseDatabase.getInstance("https://academate-c7250-default-rtdb.firebaseio.com")
    val userRef = database.getReference("users").child(userName)
    val mentorRef = database.getReference("mentors")

    // inisialisasi nama, pengalaman, mata kuliah
    var namaLengkap by remember { mutableStateOf("") }
    var pengalaman by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }



    mentorRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
//            val snapshotValue = snapshot.getValue() // Mengambil nilai dari snapshot
            (snapshot.getValue() as? Map<String, Any>)?.forEach { (key, value) ->
//                val currentUserCheck = mentorRef.child(key)
                mentorRef.child(key).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val map: Map<String, Any>? = snapshot.getValue() as? Map<String, Any>

                        if(map?.get("nama_lengkap").toString() == mentorName){
                            namaLengkap = map?.get("nama_lengkap").toString()
                            pengalaman = map?.get("experience").toString()
                            course = map?.get("course").toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    }
                })
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
        }
    })

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue2),
                        colorResource(id = R.color.white)
                    ),
                    startY = 300f
                )
            )
    ){
        HeaderMentor(navController)

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            DeskripsiMentor(namaLengkap, pengalaman, course)
            ButtonMentor(namaLengkap, course, userRef, navController)
        }
    }
}

@Composable
fun HeaderMentor(
    navController: NavController
){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
            )
        }
        Text(
            text = "Informasi Mentor",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeskripsiMentor(namaLengkap: String, pengalaman: String, matakuliah: String){
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Image(
                painter = painterResource(id = R.drawable.foto_profil),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .shadow(8.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = namaLengkap,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Mata Kuliah",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = matakuliah,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pengalaman",
                fontSize = 18.sp ,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = pengalaman,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
//            var waktu by remember { mutableStateOf("") }
//            OutlinedTextField(
//                value = waktu,
//                onValueChange = {waktu = it},
//                label = {
//                    Text(
//                        text = "Waktu Sewa",
//                        fontSize = 12.sp,
//                        color = Color.Black
//                    )
//                },
//                keyboardOptions = KeyboardOptions(
//                    imeAction = ImeAction.Done
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .heightIn(min = 40.dp, max = 60.dp),
//                shape = RoundedCornerShape(8.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    textColor = Color.Black, // Warna teks
//                    placeholderColor = Color.Black,
//                    focusedBorderColor = Color.Black, // Warna border saat fokus
//                    unfocusedBorderColor = Color.Black // Warna border saat tidak fokus
//                )
//            )
//            Spacer(modifier = Modifier.height(30.dp))
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ButtonMentor(
    namaMentor: String,
    course:String,
    user: DatabaseReference,
    navController: NavController,
    modifier: Modifier = Modifier,
){
    var showDialog by remember { mutableStateOf(false) }
    Button(
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 20.dp),
        onClick = {
            showDialog = true
        }) {
        Text(
            text = "Sewa Mentor",
            color = Color.White
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.dp, colorResource(id = R.color.blue1))
                    ),
                title = {
                    Text(
                        text = "Konfirmasi Pesanan?",
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.blue1)
                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            //generatewaktu
                            val currentDateTime= LocalDateTime.now()
                            val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy : HH-mm-ss")
                            val formattedDateTime=currentDateTime.format(formatter)

                            var riwayatNode=user.child("riwayat")
                            var riwayat= Riwayat(namaMentor,course, formattedDateTime.toString())
                            var id = UUID.randomUUID().toString()

//                            var riwayatItem = riwayatNode.child(id).setValue(riwayat)
                            riwayatNode.child(id).setValue(riwayat)

//                            riwayatItem.setValue(riwayat)

                            //updatenilaimember
//                            riwayatNode.setValue(riwayat)
                            showDialog = false

                            navController.navigate(Route.HOME)
                        },
                        modifier = Modifier
                            .height(35.dp)
                            .width(120.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1))
                    ) {
                        Text(
                            text = "Konfirmasi",
                            color = Color.White
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        modifier = Modifier
                            .height(35.dp)
                            .width(120.dp),
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text(
                            text = "Batal",
                            color = colorResource(id = R.color.blue1)
                        )
                    }
                }
            )
        }
    }

}

// untuk menyimpan daftar riwayat
data class Riwayat(var namaMentor:String,var course:String, var waktu: String)