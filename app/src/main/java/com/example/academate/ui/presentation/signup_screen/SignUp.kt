package com.example.academate.ui.presentation.signup_screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import com.example.academate.ui.presentation.login_screen.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    viewModelUser: UserViewModel = hiltViewModel() // Ambil UserViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.signUpState.collectAsState(initial = null)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    val userLocation by viewModelUser.location.collectAsState() // Lokasi dari UserViewModel

    // Permintaan izin lokasi
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Izin diberikan, ambil lokasi
            getLocation(context, viewModelUser::setLocation)
        } else {
            Toast.makeText(context, "Izin lokasi ditolak. Tidak bisa mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ... (Header dan Gambar - Dianggap sama) ...
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "Sign Up", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Input Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Input Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Ambil Lokasi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lokasi: ${if (userLocation.length > 30) userLocation.substring(0, 27) + "..." else userLocation}",
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1))
                    ) {
                        Icon(Icons.Outlined.LocationOn, contentDescription = "Get Location")
                        Text("Lokasi")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Sign Up
                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                            viewModel.registerUser(email, password)
                            viewModelUser.setUsername(username) // Simpan username di ViewModel
                            // Note: Lokasi disimpan di UserViewModel.location
                        } else {
                            Toast.makeText(context, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1))
                ) {
                    if (state.value?.isLoading == true) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text(text = "Sign Up")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = "Sudah punya akun?",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    TextButton(
                        onClick = {
                            navController.navigate(Route.LOGIN)
                        }
                    ) {
                        Text(
                            text = "Login",
                            color = colorResource(id = R.color.blue1)
                        )
                    }
                }
            }

            // LaunchedEffect untuk navigasi dan toast (Dianggap sama)
            LaunchedEffect(key1 = state.value?.isSuccess) {
                scope.launch {
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        Toast.makeText(context, "Berhasil membuat akun, silahkan login!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Route.LOGIN)
                    }
                }
            }
            LaunchedEffect(key1 = state.value?.isError) {
                scope.launch {
                    if (state.value?.isError?.isNotBlank() == true) {
                        Toast.makeText(context, "Gagal membuat akun!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}

// Fungsi helper untuk mendapatkan lokasi
fun getLocation(context: Context, onLocationResult: (String) -> Unit) {
    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        onLocationResult("Izin lokasi belum diberikan")
        return
    }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var location: Location? = null

    try {
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
        onLocationResult("Gagal mendapatkan lokasi: ${e.message}")
        return
    }

    if (location != null) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0) // Alamat lengkap
                onLocationResult(fullAddress ?: "Lokasi tidak diketahui")
            } else {
                val latLng = "Lat: ${String.format("%.4f", location.latitude)}, Lon: ${String.format("%.4f", location.longitude)}"
                onLocationResult(latLng)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            val latLng = "Lat: ${String.format("%.4f", location.latitude)}, Lon: ${String.format("%.4f", location.longitude)}"
            onLocationResult("Gagal geocode: $latLng")
        }
    } else {
        onLocationResult("Tidak dapat menemukan lokasi. Pastikan GPS aktif.")
    }
}

// untuk menyimpan data email dan password, yang akan dimasukkan ke database (logic ada di  onClick button
data class User(var email: String, var password: String, var isMentor: Boolean = false)