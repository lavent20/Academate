package com.example.academate.navigate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.academate.component.SearchScreen
import com.example.academate.data.repository.CurrentMatkulViewModel
import com.example.academate.ui.presentation.mentor.DaftarMentor
import com.example.academate.ui.presentation.be_a_mentor.FormMentor
import com.example.academate.ui.presentation.home_screen.HomeScreen
import com.example.academate.ui.presentation.mentor.InformasiMentor
import com.example.academate.ui.presentation.review.InputReviewMentor
import com.example.academate.ui.presentation.login_screen.Login
import com.example.academate.ui.presentation.mata_kuliah.MataKuliah
import com.example.academate.ui.presentation.be_a_mentor.PemberitahuanBeMentor
import com.example.academate.ui.presentation.profil.Profil
import com.example.academate.ui.presentation.review.ReviewMentor
import com.example.academate.ui.presentation.signup_screen.SignUp
import com.example.academate.ui.presentation.onBoarding.SplashScreen
import com.example.academate.ui.presentation.mata_kuliah.InformasiMatkul
import com.example.academate.ui.presentation.onBoarding.onBoarding1
import com.example.academate.ui.presentation.onBoarding.onBoarding2
import com.example.academate.ui.presentation.login_screen.UserViewModel
import com.example.academate.ui.presentation.riwayat_sewa.RiwayatScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigasi(
    navController: NavHostController,
    matkulViewModel: CurrentMatkulViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = Route.SPLASHSCREEN) {
        composable(Route.SPLASHSCREEN) {
            SplashScreen(navController)
        }
        composable(Route.ONBOARDING1) {
            onBoarding1(navController)
        }
        composable(Route.ONBOARDING2) {
            onBoarding2(navController)
        }
        composable(Route.LOGIN){
            Login(navController, userViewModel)
        }
        composable(Route.SIGNUP){
            SignUp(navController)
        }

        composable(Route.HOME) {
            HomeScreen(navController, userViewModel, matkulViewModel)
        }
        composable(Route.MATAKULIAH) {
            MataKuliah(navController, matkulViewModel)
        }
        composable(Route.INFORMASI_MATKUL) {
            InformasiMatkul(navController, matkulViewModel)
        }
        composable(Route.DAFTARMENTORRPL){
            DaftarMentor(navController, matkulViewModel, userViewModel)
        }
        composable(Route.INFORMASIMENTOR) {
            InformasiMentor(navController, userViewModel)
        }
        composable(Route.SEARCH) {
            SearchScreen(navController, matkulViewModel)
        }
        composable(Route.PROFILE) {
            Profil(navController, userViewModel)
        }
        composable(Route.FORMMENTOR){
            FormMentor(navController, userViewModel)
        }
        composable(Route.PEMBERITAHUANBEMENTOR){
            PemberitahuanBeMentor(navController)
        }
        composable(Route.RIWAYAT){
            RiwayatScreen(navController, userViewModel)
        }
        composable(Route.REVIEWMENTOR){
            ReviewMentor(navController)
        }
        composable(Route.INPUTREVIEWMENTOR){
            InputReviewMentor(navController)
        }
    }
}