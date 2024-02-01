package org.wit.thegreatfilter

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// Change Min SDk to 24 to use this
import androidx.navigation.compose.rememberNavController

enum class Screen {
    Match,
    Login,
    Signup,
    Profile,
    MatchList,
    MatchChat
}
sealed class NavigationScreen(val route: String) {
    object Signup : NavigationScreen("signup")
    object Login : NavigationScreen("login")
    object Profile : NavigationScreen("profile")
    object Match : NavigationScreen("match")
    object MatchList : NavigationScreen("matchList")
    object MatchChat : NavigationScreen("matchChat")

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationScreen.Match.route){

        composable(NavigationScreen.Signup.route){
            Signup()
        }
        composable(NavigationScreen.Login.route){
            Login()
        }
        composable(NavigationScreen.Profile.route){
            Profile(navController)
        }
        composable(NavigationScreen.Match.route){
            Match(navController)
        }
        composable(NavigationScreen.MatchList.route){
            MatchList()
        }
        composable(NavigationScreen.MatchChat.route){
            MatchChat()
        }
    }

}


fun navigateTo(navController: NavController, route: String){
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }

}