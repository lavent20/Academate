package com.example.academate.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.academate.R
import com.example.academate.data.model.MataKuliahModelResponse
import com.example.academate.data.repository.CurrentMatkulViewModel
import com.example.academate.data.repository.MataKuliahRepository
import com.example.academate.ui.presentation.home_screen.DaftarMataKuliahDiminati
import com.example.academate.ui.theme.AcadeMateTheme
import com.example.academate.ui.theme.Biru
import com.example.academate.ui.theme.Putih
import com.example.academate.util.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, matkulViewModel: CurrentMatkulViewModel) {
    Column(
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
//        Search(navController)
        var searchText by remember { mutableStateOf("") }

        // pmatakuliah
        val mataKuliahRepository = MataKuliahRepository()
        val scope = rememberCoroutineScope()
        var matkul by remember {
            mutableStateOf<List<MataKuliahModelResponse>>(emptyList())
        }
        val shouldShowLazyColumn = remember { mutableStateOf(false) }

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

        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier.size(25.dp)
                )
            }
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = {
                    Text(
                        text = "Search Here",
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .heightIn(min = 30.dp, max = 50.dp)
                    .width(300.dp),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
            IconButton(onClick = {
                Log.w("searchtext", searchText)
                shouldShowLazyColumn.value = !shouldShowLazyColumn.value
            }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "search",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // logic search
        if(shouldShowLazyColumn.value){
            Spacer(modifier = Modifier.height(5.dp))
            LazyColumn(){
                items(matkul.size){item ->
                    if(matkul[item].item!!.namaMatkul == searchText){
                        Text(
                            text = "Menampilkan Hasil Pencarian :",
                            modifier = Modifier
                                .padding(start = 20.dp),
                            fontSize = 12.sp,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        DaftarMataKuliahDiminati(
                            painter = painterResource(id = R.drawable.matakuliah),
                            matakuliah = matkul[item].item!!.namaMatkul,
                            fakultas = matkul[item].item!!.fakultas,
                            navController = navController,
                            matkulViewModel,
                            item+1
                        )
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Search(navController: NavController) {
//    var searchText by remember { mutableStateOf("") }
//
//    // pmatakuliah
//    val mataKuliahRepository = MataKuliahRepository()
//    val scope = rememberCoroutineScope()
//    var matkul by remember {
//        mutableStateOf<List<MataKuliahModelResponse>>(emptyList())
//    }
//    val shouldShowLazyColumn = remember { mutableStateOf(false) }
//
//    LaunchedEffect(key1 = true, block = {
//        scope.launch {
//            mataKuliahRepository.getMataKuliah().collect {
//                when (it) {
//                    is Resource.Error -> {}
//                    is Resource.Loading -> {}
//                    is Resource.Success -> matkul = it.data!!
//                }
//            }
//        }
//    })
//
//    Row(
//        modifier = Modifier
//            .padding(vertical = 12.dp)
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        IconButton(
//            onClick = {
//                navController.popBackStack()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Outlined.ArrowBack,
//                contentDescription = "back",
//                modifier = Modifier.size(30.dp)
//            )
//        }
//        OutlinedTextField(
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//            value = searchText,
//            onValueChange = { searchText = it },
//            placeholder = {
//                Text(
//                    text = "Search Here",
//                    fontSize = 14.sp
//                )
//            },
//            modifier = Modifier
//                .heightIn(min = 30.dp, max = 50.dp)
//                .width(300.dp),
//            shape = RoundedCornerShape(10.dp),
//        )
//        IconButton(onClick = {
//            Log.w("searchtext", searchText)
//            shouldShowLazyColumn.value = !shouldShowLazyColumn.value
//        }) {
//            Icon(
//                imageVector = Icons.Outlined.Search,
//                contentDescription = "search",
//                modifier = Modifier.size(30.dp)
//            )
//        }
//    }
//
//    // logic search
//    if(shouldShowLazyColumn.value){
//        Spacer(modifier = Modifier.height(30.dp))
//        LazyColumn(){
//            items(matkul){
//                if(it.item!!.namaMatkul == searchText){
//                    DaftarMataKuliahDiminati(
//                        painter = painterResource(id = R.drawable.matakuliah),
//                        matakuliah = it.item!!.namaMatkul,
//                        fakultas = it.item!!.fakultas,
//                        navController = navController
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun MainShowNullMentor() {
//    Column(
//        modifier = Modifier.fillMaxHeight(),
//        verticalArrangement = Arrangement.Center
//    ) {
//        ShowNullMentor()
//    }
//}

//@Composable
//fun ShowMentor(listMentor: ListMentor) {
//    Card (
//        modifier = Modifier
//            .padding(horizontal = 24.dp, vertical = 8.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Image(
//                painter = painterResource(id = listMentor.imgProfil),
//                contentDescription = null,
//                modifier = Modifier.size(75.dp)
//            )
//            Column {
//                Text(
//                    text = stringResource(id = listMentor.namaMentor),
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//                Text(
//                    text = stringResource(id = listMentor.matkulMentor),
//                    fontSize = 14.sp,
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Lihat Mentor",
//                        fontSize = 14.sp
//                    )
//                    Icon(
//                        imageVector = Icons.Default.KeyboardArrowRight,
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp),
//                        tint = colorResource(id = R.color.blue1)
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun ShowNullMentor() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(36.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Maaf, belum ada mentor untuk mata kuliah yang anda cari.",
//            textAlign = TextAlign.Center
//        )
//    }
//}