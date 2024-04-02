package org.wit.thegreatfilter.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.ui.navigation.NavMenu
import org.wit.thegreatfilter.ui.navigation.NavMenuItems
import org.wit.thegreatfilter.ui.navigation.NavigationScreen
import org.wit.thegreatfilter.utils.CommonDivider
import org.wit.thegreatfilter.utils.CommonImage
import org.wit.thegreatfilter.utils.CommonProgressSpinner
import org.wit.thegreatfilter.utils.navigateTo


//Shortcut to remove unused imports = CTRL+ ALT + O
//Align code = CTRL+ ALT + L
//Settings Shortcut = Ctrl + Alt + S


enum class  GenderType{
    MALE,FEMALE, ANY,
    JOB_SEEKER, CAN_SEEKER
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
        var gender by rememberSaveable { mutableStateOf(GenderType.valueOf(g)) }
        var genderPreference by rememberSaveable { mutableStateOf(GenderType.valueOf(gPref)) }
        var yearsExperience by rememberSaveable { mutableStateOf(userData?.yearsExperience ?: "") }
        var minEducationLevel by rememberSaveable { mutableStateOf(userData?.minEducationLevel ?: "") }
        var minSalary by rememberSaveable { mutableStateOf(userData?.minSalary ?: "") }
        var location by rememberSaveable { mutableStateOf(userData?.location ?: "") }
        var programmingLanguages by rememberSaveable { mutableStateOf(userData?.programmingLanguages ?: "") }
        var speakingLanguages by rememberSaveable { mutableStateOf(userData?.speakingLanguages ?: "") }
        var weekendsAvailable by rememberSaveable { mutableStateOf(userData?.weekendsAvailable ?: "") }
        var mondayToFriday by rememberSaveable { mutableStateOf(userData?.mondayToFriday?: "") }
        var interviewRounds by rememberSaveable { mutableStateOf(userData?.interviewRounds?: "") }
        var workInOfficeDays by rememberSaveable { mutableStateOf(userData?.workInOfficeDays?: "") }
        var positionTitle by rememberSaveable { mutableStateOf(userData?.positionTitle?: "") }

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

                positionTitle = positionTitle,
                yearsExperience = yearsExperience,
                minEducationLevel = minEducationLevel,
                minSalary = minSalary,
                location = location,
                programmingLanguages = programmingLanguages,
                speakingLanguages = speakingLanguages,
                weekendsAvailable = weekendsAvailable,
                mondayToFriday = mondayToFriday,
                interviewRounds = interviewRounds,
                workInOfficeDays = workInOfficeDays,

                onPositionTitle = { positionTitle = it },
                onYearsExperience = { yearsExperience= it },
                onMinEducationLevel = { minEducationLevel = it },
                onMinSalary = { minSalary = it },
                onWorkInOfficeDays = { workInOfficeDays = it },
                onLocation = { location = it },
                onProgrammingLanguages = { programmingLanguages = it },
                onSpeakingLanguages = { speakingLanguages = it },
                onWeekendsAvailable = { weekendsAvailable = it },
                onInterviewRounds = { interviewRounds  = it },
                onMondayToFriday = { mondayToFriday  = it },

                onSave = {
                    vm.updateProfileData(
                        name, username, bio, gender, genderPreference, positionTitle,
                        yearsExperience,
                        minEducationLevel,
                        minSalary,
                        workInOfficeDays,
                        location,
                        programmingLanguages,
                        speakingLanguages,
                        weekendsAvailable,
                        mondayToFriday,
                        interviewRounds,

                        )
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
    gender: GenderType,
    genderPreference: GenderType,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onGenderChange: (GenderType) -> Unit,
    onGenderPreferenceChange: (GenderType) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,

    positionTitle: String,
    yearsExperience: String,
    minEducationLevel: String,
    minSalary: String,
    workInOfficeDays: String,
    location: String,
    programmingLanguages: String,
    speakingLanguages: String,
    weekendsAvailable: String,
    mondayToFriday: String,
    interviewRounds: String,

    onPositionTitle: (String) -> Unit,
    onYearsExperience: (String) -> Unit,
    onMinEducationLevel: (String) -> Unit,
    onMinSalary: (String) -> Unit,
    onWorkInOfficeDays: (String) -> Unit,
    onLocation: (String) -> Unit,
    onProgrammingLanguages: (String) -> Unit,
    onSpeakingLanguages: (String) -> Unit,
    onWeekendsAvailable: (String) -> Unit,
    onMondayToFriday: (String) -> Unit,
    onInterviewRounds: (String) -> Unit,
) {
    val imageUrl = vm.userData.value?.imageURL

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

        CommonDivider()

        ProfileImage(imageUrl = imageUrl, vm = vm)

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(110.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "Username", modifier = Modifier.width(120.dp))
//            TextField(
//                value = username,
//                onValueChange = onUsernameChange,
//                colors = TextFieldDefaults.textFieldColors(
//                    textColor = Color.Black,
//                    containerColor = Color.Transparent
//                )
//            )
//        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "I am a:", modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)

            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == GenderType.JOB_SEEKER,
                        onClick = { onGenderChange(GenderType.JOB_SEEKER); onGenderPreferenceChange(GenderType.CAN_SEEKER) })
                    Text(
                        text = "Job Seeker",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderChange(GenderType.JOB_SEEKER); onGenderPreferenceChange(GenderType.CAN_SEEKER) })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == GenderType.CAN_SEEKER,
                        onClick = { onGenderChange(GenderType.CAN_SEEKER);onGenderChange(GenderType.JOB_SEEKER); })
                    Text(
                        text = "Candidate Seeker",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onGenderChange(GenderType.CAN_SEEKER); onGenderPreferenceChange(GenderType.JOB_SEEKER) })
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {

            Text(text = "Looking for:", modifier = Modifier.width(110.dp))
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "An Amazing Job Opportunity", modifier = Modifier.width(300.dp))
            }
            else {
                Text(  text = "The Perfect Candidate", modifier = Modifier.width(300.dp))
            }


        }

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp),
//            verticalAlignment = Alignment.Top
//        ) {
//            Text(
//                text = "Looking for:", modifier = Modifier
//                    .width(100.dp)
//                    .padding(8.dp)
//            )
//            Column(modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RadioButton(
//                        selected = genderPreference == GenderType.MALE,
//                        onClick = { onGenderPreferenceChange(GenderType.MALE) })
//                    Text(
//                        text = "Men",
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .clickable { onGenderPreferenceChange(GenderType.MALE) })
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RadioButton(
//                        selected = genderPreference == GenderType.FEMALE,
//                        onClick = { onGenderPreferenceChange(GenderType.FEMALE) })
//                    Text(
//                        text = "Women",
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .clickable { onGenderPreferenceChange(GenderType.FEMALE) })
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RadioButton(
//                        selected = genderPreference == GenderType.ANY,
//                        onClick = { onGenderPreferenceChange(GenderType.ANY) })
//                    Text(
//                        text = "Any",
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .clickable { onGenderPreferenceChange(GenderType.ANY) })
//                }
//            }
//        }
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "About me", modifier = Modifier.width(110.dp))
            }
            else {
                Text(  text = "Job Description", modifier = Modifier.width(110.dp))
            }

            TextField(
                value = bio,
                onValueChange = onBioChange,
                modifier = Modifier
                    .height(100.dp)
                    .border(width = 0.5.dp, Color.Black, RectangleShape),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                ),
                singleLine = false
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Position Desired", modifier = Modifier.width(110.dp))
            }
            else {
                Text(  text = "Position Available", modifier = Modifier.width(110.dp))
            }
            TextField(
                value = positionTitle,
                onValueChange = onPositionTitle,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Years Experience", modifier = Modifier.width(110.dp))
            }
            else {
                Text(  text = "Minimum Years Experience", modifier = Modifier.width(110.dp))
            }

            TextField(
                value = yearsExperience,
                onValueChange = onYearsExperience,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Highest Education Level", modifier = Modifier.width(110.dp))
            }
            else {
                Text(  text = "Minimum Education Level", modifier = Modifier.width(110.dp))
            }

            TextField(
                value = minEducationLevel,
                onValueChange = onMinEducationLevel,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Minimum Starting Salary", modifier = Modifier.width(120.dp))
            }
            else {
                Text(  text = "Starting Salary", modifier = Modifier.width(120.dp))
            }
            TextField(
                value = minSalary,
                onValueChange = onMinSalary,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Max Days In Office", modifier = Modifier.width(120.dp))
            }
            else {
                Text(  text = "Minimum Days In Office", modifier = Modifier.width(120.dp))
            }

            TextField(
                value = workInOfficeDays,
                onValueChange = onWorkInOfficeDays,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Location", modifier = Modifier.width(120.dp))
            TextField(
                value = location,
                onValueChange = onLocation,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Proficient Technologies", modifier = Modifier.width(120.dp))
            }
            else {
                Text(  text = "Technologies Required", modifier = Modifier.width(120.dp))
            }

            TextField(
                value = programmingLanguages,
                onValueChange = onProgrammingLanguages,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Speaking Languages", modifier = Modifier.width(120.dp))
            TextField(
                value = speakingLanguages,
                onValueChange = onSpeakingLanguages,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Available Weekends", modifier = Modifier.width(120.dp))
            }
            else {
                Text(  text = "Weekends Required", modifier = Modifier.width(120.dp))
            }

            TextField(
                value = weekendsAvailable,
                onValueChange = onWeekendsAvailable,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "Monday To Friday", modifier = Modifier.width(120.dp))
//            TextField(
//                value = mondayToFriday,
//                onValueChange = onMondayToFriday,
//                colors = TextFieldDefaults.textFieldColors(
//                    textColor = Color.Black,
//                    containerColor = Color.Transparent
//                )
//            )
//        }
//
//        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(gender == GenderType.JOB_SEEKER) {
                Text(text = "Maximum Interview Rounds", modifier = Modifier.width(120.dp))
            }
            else {
                Text(  text = "Interview Rounds", modifier = Modifier.width(120.dp))
            }

            TextField(
                value = interviewRounds,
                onValueChange = onInterviewRounds,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
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
}


@Composable
fun ProfileImage(imageUrl: String?, vm: TGFViewModel) {

    // lAUNCHER TO UPLOAD PROFILE IMAGE TO DATABASE
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let { vm.uploadProfileImage(uri) }
    }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(150.dp)) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Edit")
        }

        val isLoading = vm.inProgress.value

        if (isLoading)
            CommonProgressSpinner()
    }
}




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
