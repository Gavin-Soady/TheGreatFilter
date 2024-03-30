package org.wit.thegreatfilter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.ui.navigation.NavMenu
import org.wit.thegreatfilter.ui.navigation.NavMenuItems
import org.wit.thegreatfilter.ui.navigation.NavigationScreen
import org.wit.thegreatfilter.utils.CommonDivider
import org.wit.thegreatfilter.utils.CommonProgressSpinner
import org.wit.thegreatfilter.utils.navigateTo


//Shortcut to remove unused imports = CTRL+ ALT + O

enum class  Gender{
    MALE,FEMALE, ANY
}

@Composable
fun Profile(navController: NavController, vm: TGFViewModel) {
    val inProgress = vm.inProgress.value
    if (inProgress)
        CommonProgressSpinner()
    else {
        val userData = vm.userData.value
        val g = if (userData?.gender.isNullOrEmpty()) "MALE"
        else userData!!.gender!!.uppercase()

        val gPref = if (userData?.genderPreference.isNullOrEmpty()) "FEMALE"
        else userData!!.genderPreference!!.uppercase()
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }
        var gender by rememberSaveable { mutableStateOf(Gender.valueOf(g)) }
        var genderPreference by rememberSaveable { mutableStateOf(Gender.valueOf(gPref)) }

        val scrollState = rememberScrollState()
        Column {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(8.dp),
                vm = vm,
                name = name,
                username = username,
                bio = bio,
                gender = gender,
                genderPreference = genderPreference,
                onNameChange = { name = it },
                onUsernameChange = { username = it },
                onBioChange = { bio = it },
                onGenderChange = { gender = it },
                onGenderPreferenceChange = { genderPreference = it },
                onSave = {
                    vm.updateProfileData(name, username, bio, gender, genderPreference)
                },
                onBack = { navigateTo(navController, NavigationScreen.Match.route) },
                onLogout = {
                    vm.onLogout()
                    navigateTo(navController, NavigationScreen.Login.route)
                }
            )

            NavMenu(
                selectedItem = NavMenuItems.PROFILE,
                navController = navController
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: TGFViewModel,
    name: String,
    username: String,
    bio: String,
    gender: Gender,
    genderPreference: Gender,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onGenderPreferenceChange: (Gender) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
//    val imageUrl = vm.userData.value?.imageURL

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
        }
    }


        CommonDivider()

        //ProfileImage(imageUrl = imageUrl, vm = vm)

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            TextField(
                value = username,
                onValueChange = onUsernameChange,
                modifier = Modifier.background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Bio", modifier = Modifier.width(100.dp))
            TextField(
                value = bio,
                onValueChange = onBioChange,
                modifier = Modifier.height(150.dp).background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
                singleLine = false
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "I am a:", modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == Gender.MALE,
                        onClick = { onGenderChange(Gender.MALE) })
                    Text(
                        text = "Job Seeker",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderChange(Gender.MALE) })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == Gender.FEMALE,
                        onClick = { onGenderChange(Gender.FEMALE) })
                    Text(
                        text = "Candidate Seeker",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderChange(Gender.FEMALE) })
                }
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Looking for a :", modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = genderPreference == Gender.MALE,
                        onClick = { onGenderPreferenceChange(Gender.MALE) })
                    Text(
                        text = "Job",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderPreferenceChange(Gender.MALE) })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = genderPreference == Gender.FEMALE,
                        onClick = { onGenderPreferenceChange(Gender.FEMALE) })
                    Text(
                        text = "Candidate",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderPreferenceChange(Gender.FEMALE) })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = genderPreference == Gender.ANY,
                        onClick = { onGenderPreferenceChange(Gender.ANY) })
                    Text(
                        text = "Any",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderPreferenceChange(Gender.ANY) })
                }
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable { onLogout.invoke() })
        }

  }


//@Composable
//fun ProfileImage() {

//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//    ) { uri: Uri? ->
//        //uri?.let { vm.uploadProfileImage(uri) }
//    }
//
//    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
//        Column(modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//            .clickable {
//                launcher.launch("image/*")
//            },
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Card(shape = CircleShape, modifier = Modifier
//                .padding(8.dp)
//                .size(100.dp)) {
//                //CommonImage(data = imageUrl)
//            }
//            Text(text = "Change profile picture")
//        }
//
//        val isLoading = vm.inProgress.value
//        if (isLoading)
//            CommonProgressSpinner()
//    }
//}




//@Composable
//fun Profile(navController: NavController, vm: TGFViewModel) {
//    val inProgress = vm.inProgress.value
//    if (inProgress)
//        CommonProgressSpinner()
//    else {
//        val userData = vm.userData.value
//        val g = if (userData?.gender.isNullOrEmpty()) "MALE"
//        else userData!!.gender!!.uppercase()
//        val gPref = if (userData?.genderPreference.isNullOrEmpty()) "FEMALE"
//        else userData!!.genderPreference!!.uppercase()
//        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
//        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }
//        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }
//        var gender by rememberSaveable { mutableStateOf(Gender.valueOf(g)) }
//        var genderPreference by rememberSaveable { mutableStateOf(Gender.valueOf(gPref)) }
//
//        val scrollState = rememberScrollState()
//
//        Column {
//            ProfileContent(
//                modifier = Modifier
//                    .weight(1f)
//                    .verticalScroll(scrollState)
//                    .padding(8.dp),
//                vm = vm,
//                name = name,
//                username = username,
//                bio = bio,
//                gender = gender,
//                genderPreference = genderPreference,
//                onNameChange = { name = it },
//                onUsernameChange = { username = it },
//                onBioChange = { bio = it },
//                onGenderChange = { gender = it },
//                onGenderPreferenceChange = { genderPreference = it },
//                onSave = {
//                    //vm.updateProfileData(name, username, bio, gender, genderPreference)
//                },
//                onBack = { navigateTo(navController, NavigationScreen.Match.route) },
//                onLogout = {
//                    vm.onLogout()
//                    navigateTo(navController, NavigationScreen.Login.route)
//                }
//            )
//
//            NavMenu(
//                selectedItem = NavMenuItems.PROFILE,
//                navController = navController
//            )
//        }
//    }
//}
