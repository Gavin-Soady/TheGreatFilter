package org.wit.thegreatfilter.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.ui.screens.Login
import org.wit.thegreatfilter.ui.screens.Match
import org.wit.thegreatfilter.ui.screens.MatchChat
import org.wit.thegreatfilter.ui.screens.MatchList
import org.wit.thegreatfilter.ui.screens.Profile
import org.wit.thegreatfilter.ui.screens.Signup
import org.wit.thegreatfilter.utils.NotificationMessage

/*enum class Screen {
    Match,
    Login,
    Signup,
    Profile,
    MatchList,
    MatchChat
} */
sealed class NavigationScreen(val route: String) {
    object Signup : NavigationScreen("signup")
    object Login : NavigationScreen("login")
    object Profile : NavigationScreen("profile")
    object Match : NavigationScreen("match")
    object MatchList : NavigationScreen("matchList")
    object MatchChat : NavigationScreen("matchChat/{chatId}"){
        fun createRoute(id: String) = "matchChat/$id"
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val vm = hiltViewModel<TGFViewModel>()
    NotificationMessage(vm = vm)
    NavHost(navController = navController, startDestination = NavigationScreen.Login.route){


        composable(NavigationScreen.Signup.route){
            Signup(navController,vm)
        }
        composable(NavigationScreen.Login.route){
            Login(navController,vm)
        }
        composable(NavigationScreen.Profile.route){
            Profile(navController,vm)
        }
        composable(NavigationScreen.Match.route){
            Match(navController,vm)
        }
        composable(NavigationScreen.MatchList.route) {
            MatchList(navController, vm)
        }
        composable(NavigationScreen.MatchChat.route){
            MatchChat(navController, vm, chatId = "123456")
        }
    }

}

/*
fun navigateTo(navController: NavController, route: String){
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }

}
 */