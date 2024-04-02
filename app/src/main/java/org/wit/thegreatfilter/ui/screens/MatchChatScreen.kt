package org.wit.thegreatfilter.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wit.thegreatfilter.TGFViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchChat(navController: NavController, vm: TGFViewModel, chatId: String) {

    Text(
        text = "Match Chat Screen",
        modifier = Modifier.padding(8.dp),
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif
    )
    Text(
        text = chatId,
        modifier = Modifier.padding(58.dp),
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif
    )


}