package org.wit.thegreatfilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Match() {

    Text(
        text = "Match Screen",
        modifier = Modifier.padding(8.dp),
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif
    )


}