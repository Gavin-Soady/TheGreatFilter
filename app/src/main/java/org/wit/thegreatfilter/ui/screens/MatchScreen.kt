package org.wit.thegreatfilter.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.data.UserData
import org.wit.thegreatfilter.ui.navigation.NavMenu
import org.wit.thegreatfilter.ui.navigation.NavMenuItems
import org.wit.thegreatfilter.utils.CommonImage
import org.wit.thegreatfilter.utils.CommonProgressSpinner
import org.wit.thegreatfilter.utils.Direction
import org.wit.thegreatfilter.utils.ProfileCardDivider
import org.wit.thegreatfilter.utils.rememberSwipeableCardState
import org.wit.thegreatfilter.utils.swipableCard

//import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Match(navController: NavController, vm: TGFViewModel) {

    val inProgress = vm.inProgressProfiles.value
    //val scrollState = rememberScrollState()
    if (inProgress)
        CommonProgressSpinner()
    else{
        val profiles = vm.matchProfiles.value
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xfff68084),
                            Color(0xCCbd4302),
                        )
                    )
                )
        ) {
            Spacer(modifier = Modifier.height(0.5.dp))

            //Cards
            val states = profiles.map { it to rememberSwipeableCardState() }
            Box(
                Modifier
                    .padding(24.dp).height(470.dp)
                    //.fillMaxSize()
                    //.aspectRatio(1f)

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No more profiles available")
                }
                states.forEach { (matchProfile, state) ->
                    ProfileCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .swipableCard(
                                state = state,
                                blockedDirections = listOf(Direction.Down, Direction.Up),
                                onSwiped = {},
                                onSwipeCancel = { Log.d("Swipeable card", "Canceled Swipe") }),
                        matchProfile = matchProfile

                    )
                    LaunchedEffect(matchProfile, state.swipedDirection) {
                        if (state.swipedDirection != null) {
                            if (state.swipedDirection == Direction.Left ||
                                state.swipedDirection == Direction.Down
                            ) {
                                vm.onDisLike(matchProfile)
                            } else {
                                 vm.onLike(matchProfile)
                            }
                        }
                    }
                }

            }


            // Buttons
            val scope = rememberCoroutineScope()
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircleButton(onClick = {
                    scope.launch {
                        val last = states.reversed().firstOrNull {
                            it.second.offset.value == Offset(0f, 0f)
                        }?.second
                        last?.swipe(Direction.Left)
                    }
                }, icon = Icons.Rounded.Close)
                CircleButton(onClick = {
                    scope.launch {
                        val last = states.reversed().firstOrNull {
                            it.second.offset.value == Offset(0f, 0f)
                        }?.second
                        last?.swipe(Direction.Right)
                    }
                }, icon = Icons.Rounded.Favorite)

            }
            NavMenu(selectedItem = NavMenuItems.MATCH, navController = navController)

        }
    }
    }

//NavMenu(selectedItem = NavMenuItems.MATCH, navController = navController )
@Composable
private fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .size(56.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        onClick = onClick
    ) {
        Icon(
            icon, null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier,
    matchProfile: UserData,
    //matchProfile: MatchProfile,
) {
    Card(modifier) {
        Box {
//            Image(contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize(),
//                painter = painterResource(matchProfile.drawableResId),
//                contentDescription = null)

            CommonImage(data = matchProfile.imageURL, modifier = Modifier.fillMaxSize().padding(30.dp).padding(start = 100.dp).padding(bottom = 100.dp).padding(top = 30.dp))
            Scrim(Modifier.align(Alignment.TopCenter))
            Column(Modifier.align(Alignment.TopStart).padding(start = 20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = matchProfile.name ?: matchProfile.username ?: "",
                        //color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 12.dp, start = 10.dp, bottom = 12.dp)
                    )

                }

                ProfileCardDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = matchProfile.positionTitle ?: "", modifier = Modifier.width(110.dp))

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = matchProfile.programmingLanguages ?: "", modifier = Modifier.width(110.dp))

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = matchProfile.location ?: "", modifier = Modifier.width(110.dp))

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = matchProfile.minSalary ?: "", modifier = Modifier.width(110.dp))

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp).weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = matchProfile.bio?: "", modifier = Modifier.width(400.dp))

                }
            }
        }
    }
}


@Composable
fun Scrim(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Brush.horizontalGradient(listOf(Color.White, Color.White, Color.Transparent, Color.Transparent, Color.Transparent  )))
            //.height(180.dp)
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White,Color.Transparent,Color.Transparent, Color.Transparent,  Color.Transparent, Color.White, Color.White,  ))))
}