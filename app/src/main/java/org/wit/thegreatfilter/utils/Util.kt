package org.wit.thegreatfilter.utils

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.ui.navigation.NavigationScreen


fun navigateTo(navController: NavController, route: String){

    navController.navigate(route){
        popUpTo(route)
        launchSingleTop = true

    }
}

@Composable
 fun CommonProgressSpinner() {
     Row(
         modifier = Modifier
             .alpha(0.5f)
             .background(Color.LightGray)
             .clickable(enabled = false) {}
             .fillMaxSize(),
         horizontalArrangement = Arrangement.Center,
         verticalAlignment = Alignment.CenterVertically
    ){
        CircularProgressIndicator()
     }
 }

@Composable
fun NotificationMessage(vm: TGFViewModel) {
    val notifState = vm.popupNotification.value
    val notifMessage = notifState?.getContentOrNull()
    if (!notifMessage.isNullOrEmpty())
        Toast.makeText(LocalContext.current, notifMessage, Toast.LENGTH_SHORT).show()
}

@Composable
fun CheckSignedIn(vm: TGFViewModel, navController: NavController ){
    val alreadyLoggedIn = remember { mutableStateOf(false) }
    val signedIn = vm.signedIn.value


    if (signedIn && !alreadyLoggedIn.value ){
        alreadyLoggedIn.value = true
        navController.navigate(NavigationScreen.Match.route){
            //reset back button (TrackStack)
            popUpTo(0)
        }
    }

}