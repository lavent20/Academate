package com.example.academate.ui.presentation.login_screen

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.navigate.Route
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController,viewModelUser: UserViewModel, viewModel: SignInViewModel = hiltViewModel()) {
    // inisialisasi database
    val database = FirebaseDatabase.getInstance("https://academate-c7250-default-rtdb.firebaseio.com")
    val users = database.getReference("users") // pointer ke root users
    val mentorRef = database.getReference("mentors")

    // mutable state untuk form
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

    mentorRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val snapshotValue = snapshot.getValue() // Mengambil nilai dari snapshot

            (snapshotValue as? Map<String, Any>)?.forEach { (key, value) ->
                val currentUserCheck = mentorRef.child(key)
                currentUserCheck.addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snapshotValue = snapshot.getValue() // Mengambil nilai dari snapshot
                        val map: Map<String, Any>? = snapshotValue as? Map<String, Any>

                        var name = map?.get("nama_lengkap").toString()
                        var course = map?.get("course").toString()

                        viewModelUser.addMentorDetail(name, course)
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
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logooo),
            contentDescription = "logo",
            modifier = Modifier.size(200.dp)
        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = "AcadeMate",
//            fontSize = 32.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
        Spacer(modifier = Modifier.height(20.dp))

        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(36.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = { Text(text = "Email")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text(text = "Password")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
//                              navController.navigate(Route.HOME)
                        // menambahkan login
                        // menjalankan logika login
                        scope.launch {
                            viewModel.loginUser(email, password)
                        }

                        // mencari username menggunakan email yang digunakan untuk login
                        users.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val snapshotValue = snapshot.getValue() // Mengambil nilai dari snapshot

                                (snapshotValue as? Map<String, Any>)?.forEach { (key, value) ->
                                    val currentUserCheck = users.child(key)
                                    currentUserCheck.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val snapshotValue = snapshot.getValue() // Mengambil nilai dari snapshot
                                            val map: Map<String, Any>? = snapshotValue as? Map<String, Any>

                                            var DatabaseUserEmail = map?.get("email").toString()
                                            if (DatabaseUserEmail == email) {
                                                // mendapatkan username current user
                                                viewModelUser.setUsername(key)
                                                Log.w("Key", key)
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
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue1))
                ) {
                    Text(
                        text = "Login"
                    )
                }

                // menambahkan loading setelah submit
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.value?.isLoading == true) {
                        CircularProgressIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Belum punya akun?",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    TextButton(
                        onClick = {
                            navController.navigate(Route.SIGNUP)
                        }
                    ) {
                        Text(
                            text = "Sign Up",
                            color = colorResource(id = R.color.blue1),
                        )
                    }
                }
            }
            LaunchedEffect(key1 = state.value?.isSuccess) {
                scope.launch {
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        Toast.makeText(context, "Berhasil login!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Route.HOME)
                    }
                }
            }

            LaunchedEffect(key1 = state.value?.isError) {
                scope.launch {
                    if (state.value?.isError?.isNotEmpty() == true) {
                        Toast.makeText(context, "Gagal login!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}