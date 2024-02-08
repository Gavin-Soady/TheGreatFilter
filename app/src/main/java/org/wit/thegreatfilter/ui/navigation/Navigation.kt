package org.wit.thegreatfilter.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.wit.thegreatfilter.ui.screens.Login
import org.wit.thegreatfilter.ui.screens.Match
import org.wit.thegreatfilter.ui.screens.MatchChat
import org.wit.thegreatfilter.ui.screens.MatchList
import org.wit.thegreatfilter.ui.screens.Profile
import org.wit.thegreatfilter.ui.screens.Signup

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
        composable(NavigationScreen.MatchList.route) {
            MatchList(navController)
        }
        composable(NavigationScreen.MatchChat.route){
            MatchChat()
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