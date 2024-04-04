package org.wit.thegreatfilter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wit.thegreatfilter.R
import org.wit.thegreatfilter.TGFViewModel
import org.wit.thegreatfilter.ui.navigation.NavigationScreen
import org.wit.thegreatfilter.utils.CheckSignedIn
import org.wit.thegreatfilter.utils.CommonProgressSpinner
import org.wit.thegreatfilter.utils.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun Login(navController: NavController, vm: TGFViewModel) {

    CheckSignedIn(vm = vm, navController = navController)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //val usernameState = remember { mutableStateOf(TextFieldValue()) }
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }

            //val focus = LocalFocusManager.current


            Image(
                painter = painterResource(id = R.drawable.filterlogo),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .padding(top = 36.dp)
                    .padding(8.dp)
            )
            Text(
                text = "The Great Filter",
                modifier = Modifier
                    .padding(bottom = 20.dp),
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Gray
            )

            Text(
                text = "Sign in",
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
            )

            Text(
                text = "Hi there! Have we got job for you!",
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Gray,
                textAlign = TextAlign.End
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Email") })

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                      vm.onLogin(
                          emailState.value.text,
                          passwordState.value.text,

                      )

                },
                modifier = Modifier.padding(8.dp)
                    .width(280.dp),
                shape = RoundedCornerShape(10),
            ) {
                Text(text = "Login")
            }

            Text(text = "Forgot Password                      Sign Up",
                color = Color.Gray,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 180.dp)
                    .clickable{ navigateTo(navController, NavigationScreen.Signup.route)}
            )
        }

        val isLoading = vm.inProgress.value
        if(isLoading)
            CommonProgressSpinner()

    }
}