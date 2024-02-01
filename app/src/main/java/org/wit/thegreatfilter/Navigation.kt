package org.wit.thegreatfilter

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
sealed class NavigationItem(val route: String) {
    object Signup : NavigationItem("Signup")
    object Login : NavigationItem("Login")
    object Profile : NavigationItem("Profile")
    object Match : NavigationItem("Match")
    object MatchList : NavigationItem("MatchList")
    object MatchChat : NavigationItem("MatchChat")

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationItem.Match.route){

        composable(NavigationItem.Signup.route){
            Signup()
        }
        composable(NavigationItem.Login.route){
            Login()
        }
        composable(NavigationItem.Profile.route){
            Profile()
        }
        composable(NavigationItem.Match.route){
            Match()
        }
        composable(NavigationItem.MatchList.route){
            MatchList()
        }
        composable(NavigationItem.Match.route){
            MatchChat()
        }
    }


}
