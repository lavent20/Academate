package com.example.academate.ui.presentation.mata_kuliah

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.data.repository.CurrentMatkulViewModel
import com.example.academate.navigate.Route
import com.example.academate.ui.theme.Biru
import com.example.academate.ui.theme.Putih
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun InformasiMatkul(
    navController: NavController,
    matkulViewModel: CurrentMatkulViewModel
){
    val currentMatkul by matkulViewModel.currentMatkul.collectAsState()

    // inisialisasi detail matakuliah
    var namaMatkul by remember { mutableStateOf("") }
    var fakultas by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }


    // inisialisasi database
    val database = FirebaseDatabase.getInstance("https://academate-c7250-default-rtdb.firebaseio.com")
    val matkulRef = database.getReference("MataKuliah")

    matkulRef.child("matkul$currentMatkul").addValueEventListener(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val snapshotValue = snapshot.getValue() // Mengambil nilai dari snapshot
            val map: Map<String, Any>? = snapshotValue as? Map<String, Any>

            namaMatkul = map?.get("namaMatkul").toString()
            fakultas = map?.get("fakultas").toString()
            desc = map?.get("desc").toString()

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
                        Biru,
                        Putih
                    )
                )
            )
    ){
        HeaderInformasiMatkul(
            namaMatkul = namaMatkul,
            navController = navController
        )
        DeskripsiMatkul(
            namaMatkul = namaMatkul,
            fakultas = fakultas,
            desc = desc
        )
        ButtonCariMentor(namaMatkul,matkulViewModel,navController = navController)
    }
}


@Composable
fun HeaderInformasiMatkul(
    namaMatkul: String,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp)
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = namaMatkul,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
    }
}

@Composable
fun DeskripsiMatkul(
    namaMatkul: String,
    fakultas: String,
    desc: String,
    modifier: Modifier = Modifier
){
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.matakuliah),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .shadow(8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 12.dp)
        ){
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = namaMatkul,
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = fakultas,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Deskripsi",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = desc,
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ButtonCariMentor(
    namaMatkul: String,
    matkulViewModel: CurrentMatkulViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
){
    Button(
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1)),
        onClick = {
            navController.navigate(Route.DAFTARMENTORRPL)
            matkulViewModel.setCurrentNamaMatkul(namaMatkul)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Cari Mentor",
            color = Color.White
        )
    }
}