package org.wit.thegreatfilter.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.ui.navigation.NavMenu
import org.wit.thegreatfilter.ui.navigation.NavMenuItems
import org.wit.thegreatfilter.ui.navigation.NavigationScreen
import org.wit.thegreatfilter.utils.CommonImage
import org.wit.thegreatfilter.utils.CommonProgressSpinner
import org.wit.thegreatfilter.utils.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchList(navController: NavController, vm: TGFViewModel) {
    val inProgress = vm.inProgressChats.value
    if (inProgress)
        CommonProgressSpinner()
    else {
        val chats = vm.chats.value
        val userData = vm.userData.value

        Column(modifier = Modifier.fillMaxSize()) {

            if (chats.isEmpty())
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No chats available yet",fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,)
                    Text(text = "Better Get Swiping!!!", fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,)
                }
            else {
                LazyColumn(modifier = Modifier.weight(1f)) {

                    items(chats) { chat ->
                        val chatUser = if (chat.user1.userId == userData?.userId) chat.user2
                        else chat.user1
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                                .clickable {
                                    chat.chatId?.let {
                                        navigateTo(
                                            navController,
                                            NavigationScreen.MatchChat.createRoute(it)
                                        )
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CommonImage(
                                data = chatUser.imageURL,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                            Text(
                                text = chatUser.name ?: "---",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            NavMenu(selectedItem = NavMenuItems.PROFILE, navController = navController)
        }

    }
}