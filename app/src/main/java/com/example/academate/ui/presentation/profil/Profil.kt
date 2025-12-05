package com.example.academate.ui.presentation.profil

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import com.example.academate.ui.presentation.login_screen.UserViewModel
import com.example.academate.ui.theme.Biru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profil(navController: NavController, viewModelUser: UserViewModel) {

    val username by viewModelUser.username.collectAsState()
    var status by remember { mutableStateOf("Member") }

    // inisialisasi database
    val database = FirebaseDatabase.getInstance()
    val mentorRef = database.getReference("users").child(username) // pointer ke user current

    // mengambil status apakah mentor atau bukan
    mentorRef.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val map: Map<String, Any>? = snapshot.getValue() as? Map<String, Any>
            if ( map?.get("mentor").toString() == "true") {
                // mendapatkan username current user
                status = "Mentor"
                Log.w("mentor", status)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    })

    Box(
        modifier = Modifier
            .background(Biru)
    ) {
        Scaffold(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue2),
                        colorResource(id = R.color.white)
                    ),
                    startY = 300f
                )
            ),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Profil", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .fillMaxHeight()
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto_profil),
                    contentDescription = "profil",
                    modifier = Modifier
                        .size(width = 160.dp, height = 160.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = username,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = status,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(36.dp))
                ProfilMenu(navController, status)
            }
        }
    }
}

@Composable
fun ProfilMenu(navController: NavController, status:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(24.dp),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if(status == "Member") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "be_a_mentor",
                        modifier = Modifier.size(32.dp),
                    )
                    ClickableText(
                        text = AnnotatedString("Be a Mentor"),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 16.sp,
                        ),
                        onClick = {
                            navController.navigate(Route.FORMMENTOR)
                        }
                    )
                }
//                } else {
//                    ClickableText(
//                        text = AnnotatedString("You are a mentor"),
//                        style = MaterialTheme.typography.bodySmall.copy(
//                            fontSize = 16.sp,
//                        ),
//                        onClick = {
////                            navController.navigate(Route.FORMMENTOR)
//                        }
//                    )
//                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .height(IntrinsicSize.Min)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "riwayat",
                    modifier = Modifier.size(32.dp)
                )
                ClickableText(
                    text = AnnotatedString("Riwayat"),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 16.sp,
                    ),
                    onClick = {
                        navController.navigate(Route.RIWAYAT)
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = "logout",
                    modifier = Modifier.size(32.dp)
                )
                ClickableText(
                    text = AnnotatedString("Logout"),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 16.sp,
                    ),
                    onClick = {
                        navController.navigate(Route.LOGIN)
                    }
                )
            }
        }
    }
}