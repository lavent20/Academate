package com.example.academate.navigate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.academate.R

@Composable
fun NavigasiBar(
//    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {

    val bottomBarList = listOf(
        BottomNavItem(
            name = "Home",
            route = Route.HOME,
            icon = painterResource(id = R.drawable.navigation_home)
        ),
        BottomNavItem(
            name = "Matkul",
            route = Route.MATAKULIAH,
            icon = painterResource(id = R.drawable.navigation_book_open)
        ),
        BottomNavItem(
            name = "Search",
            route = Route.SEARCH,
            icon = painterResource(id = R.drawable.navigation_search)
        ),
        BottomNavItem(
            name = "Profile",
            route = Route.PROFILE,
            icon = painterResource(id = R.drawable.navigation_user)
        ),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        NavigationBar(
            modifier = modifier,
            containerColor = Color.White,
            tonalElevation = 5.dp
        ) {
            bottomBarList.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    selected = selected,
//                    onClick = { onItemClick(item.route) },
                    { onItemClick(item.route, navController = navController) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.blue1),
                        unselectedIconColor = Color.Gray
                    ),
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = item.icon,
                                contentDescription = item.name,
                                modifier = modifier
                                    .size(30.dp)
                            )
                        }
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .padding(top = 25.dp, start = 2.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

fun onItemClick(route: String, navController: NavController) {
    navController.navigate(route)

}