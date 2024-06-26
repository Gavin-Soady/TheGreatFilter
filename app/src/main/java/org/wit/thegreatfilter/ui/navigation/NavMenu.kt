package org.wit.thegreatfilter.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.wit.thegreatfilter.R
import org.wit.thegreatfilter.utils.navigateTo


enum class NavMenuItems (val icon : Int, val navItem : NavigationScreen){
    MATCH(R.drawable.baseline_match, NavigationScreen.Match),
    CHATLIST(R.drawable.baseline_chat, NavigationScreen.MatchList),
    PROFILE(R.drawable.baseline_profile, NavigationScreen.Profile)

}
@Composable
fun NavMenu(selectedItem: NavMenuItems, navController: NavController) {

  Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(all = 0.dp)
          .background(
              Color(0x99FFFFFF)
              )

  ){
      for (item in NavMenuItems.values()){
          Image(
              painter = painterResource(id = item.icon),
              contentDescription = null,
              modifier = Modifier
                  .size(45.dp)
                  .padding(4.dp)
                  .weight(1f)
                  .clickable {
                      navigateTo(navController,item.navItem.route)
                  },

          )
      }

  }


}


