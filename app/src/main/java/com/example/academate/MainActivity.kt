package com.example.academate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.academate.navigate.CustomScaffold
import com.example.academate.navigate.Navigasi
import com.example.academate.navigate.Route
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val showBar = listOf(
        Route.HOME,
        Route.MATAKULIAH,
        Route.SEARCH,
        Route.PROFILE
    )
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = getColor(R.color.black)

            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currPage = backStackEntry?.destination?.route

            Surface {
                CustomScaffold(
                    navController = navController,
                    showBottomBar = currPage in showBar
                ) {
                    Navigasi(navController = navController)
                }
            }
        }
    }
}