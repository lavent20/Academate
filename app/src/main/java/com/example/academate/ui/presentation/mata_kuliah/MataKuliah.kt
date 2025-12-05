package com.example.academate.ui.presentation.mata_kuliah

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.data.repository.MataKuliahRepository
import com.example.academate.data.model.MataKuliahModelResponse
import com.example.academate.data.repository.CurrentMatkulViewModel
import com.example.academate.navigate.Route
import com.example.academate.ui.theme.Biru
import com.example.academate.ui.theme.Putih
import com.example.academate.util.Resource
import kotlinx.coroutines.launch


@Composable
fun MataKuliah(
    navController: NavController,
    matkulViewModel: CurrentMatkulViewModel
) {

    val mataKuliahRepository = MataKuliahRepository()
    val scope = rememberCoroutineScope()
    var matkul by remember {
        mutableStateOf<List<MataKuliahModelResponse>>(emptyList())
    }

    LaunchedEffect(key1 = true, block = {
        scope.launch {
            mataKuliahRepository.getMataKuliah().collect {
                when (it) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> matkul = it.data!!
                }
            }
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
    ) {
        Header(navController)

        LazyColumn() {
            items(matkul.size) {item ->
                DaftarMataKuliah(
                    painter = painterResource(id = R.drawable.matakuliah),
                    idMatkul = matkul[item].item!!.id,
                    matakuliah = matkul[item].item!!.namaMatkul,
                    fakultas = matkul[item].item!!.fakultas,
                    matkulViewModel,
                    item+1,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun Header(
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
            text = "Mata Kuliah",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarMataKuliah(
    painter: Painter,
    idMatkul: String,
    matakuliah: String,
    fakultas: String,
    matkulViewModel: CurrentMatkulViewModel,
    matkulIndex: Int,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .padding(start = 20.dp, end = 20.dp, bottom = 5.dp),
            onClick = {
                navController.navigate(Route.INFORMASI_MATKUL)
                matkulViewModel.setCurrentMatkul(matkulIndex)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .height(80.dp)

                    ) {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, top = 10.dp)
                    ) {
                        Column {
                            Text(
                                text = matakuliah,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                )

                            Text(
                                text = fakultas,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}