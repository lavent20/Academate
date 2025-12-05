package com.example.academate.ui.presentation.profil

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.academate.R
import com.example.academate.navigate.Route
import com.example.academate.ui.presentation.login_screen.UserViewModel
import com.example.academate.ui.theme.Biru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height


// ===================== Helper for Camera File ======================

private fun createImageFile(context: Context): File {
    val fileName = "JPEG_${System.currentTimeMillis()}_"
    return File.createTempFile(fileName, ".jpg", context.cacheDir)
}

// ====================================================================


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profil(navController: NavController, viewModelUser: UserViewModel) {

    val username by viewModelUser.username.collectAsState()
    val savedImageUri by viewModelUser.profileImageUri.collectAsState()

    // Status mentor
    var status by remember { mutableStateOf("Member") }

    // Firebase cek status
    val database = FirebaseDatabase.getInstance()
    val mentorRef = database.getReference("users").child(username)

    mentorRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val map = snapshot.getValue() as? Map<String, Any>
            if (map?.get("mentor").toString() == "true") {
                status = "Mentor"
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    })

    // ==================== Camera & Gallery State ====================

    var imageUri by remember {
        mutableStateOf(savedImageUri?.let { Uri.parse(it) })
    }

    val context = LocalContext.current

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            imageUri = cameraImageUri
            viewModelUser.setProfileImageUri(cameraImageUri.toString())
        }
    }

    // Camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val photoFile = createImageFile(context)
            cameraImageUri = androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            cameraLauncher.launch(cameraImageUri!!)
        } else {
            showPermissionDeniedDialog = true
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            viewModelUser.setProfileImageUri(uri.toString())
        }
    }

    // ================================================================

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO PROFIL
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { showImageSourceDialog = true }
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "foto profil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.navigation_user),
                        contentDescription = "placeholder",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)                // memberi jarak dari pinggir foto
                        .size(34.dp)                  // wrapper icon
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f)) // backdrop lebih smooth
                        .clickable { showImageSourceDialog = true }, // klik ganti foto
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Photo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)  // icon lebih proporsional
                    )
                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(username, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(status, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(30.dp))

            // ===================== MENU CARD ===========================

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    if (status == "Member") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { navController.navigate(Route.FORMMENTOR) }
                        ) {
                            Icon(Icons.Outlined.Star, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Be a Mentor", fontSize = 16.sp)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { navController.navigate(Route.RIWAYAT) }
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Riwayat", fontSize = 16.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { navController.navigate(Route.LOGIN) }
                    ) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout", fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // =================== Dialogs =======================
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Pilih Sumber Gambar") },
            text = { Text("Ambil foto atau pilih dari galeri") },
            confirmButton = {
                Button(onClick = {
                    showImageSourceDialog = false
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }) {
                    Icon(Icons.Default.CameraAlt, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Kamera")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showImageSourceDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Icon(Icons.Default.PhotoLibrary, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Galeri")
                }
            }
        )
    }

    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = { Text("Izin Kamera Ditolak") },
            text = { Text("Silakan gunakan galeri untuk memilih foto.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDeniedDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}
