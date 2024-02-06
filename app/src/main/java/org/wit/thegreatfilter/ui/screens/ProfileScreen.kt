package org.wit.thegreatfilter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wit.thegreatfilter.ui.navigation.NavMenu
import org.wit.thegreatfilter.ui.navigation.NavMenuItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = "Profile Screen",
            modifier = Modifier.padding(8.dp),
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif
        )
        NavMenu(selectedItem = NavMenuItems.PROFILE, navController = navController )


    }



}