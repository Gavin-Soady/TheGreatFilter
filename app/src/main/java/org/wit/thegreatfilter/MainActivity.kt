package org.wit.thegreatfilter

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.wit.thegreatfilter.models.FilterModel

import org.wit.thegreatfilter.ui.theme.TheGreatFilterTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           TheGreatFilterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(modifier = Modifier.fillMaxSize())
                    {
                        //ShowToolBar()
                        //ShowAddFilter()
                        SignupPage()
                    }
                    Column(modifier = Modifier.fillMaxSize())
                    {
                        //ShowGreetingWithToast()
                    }
                }
            }
        }
        Timber.plant(Timber.DebugTree())
        Timber.i("TGF MainActivity started..")

    }
}

@Composable
fun Greeting() {
    Text(
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.window_text),
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,

        fontFamily = FontFamily.SansSerif,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(48.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ShowGreetingWithToast() {
    val context = LocalContext.current
    val message = stringResource(id = R.string.greeting_text)
    TheGreatFilterTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            //Greeting()
            Button(
                elevation = ButtonDefaults.buttonElevation(20.dp),
                onClick = {
                    // Update the message text when the button is clicked
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.button_label)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheGreatFilterTheme {
        Greeting()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowAddFilter() {
    var title by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    TheGreatFilterTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    placeholderColor = MaterialTheme.colorScheme.onPrimary
                ),
                value = title,
                onValueChange = {
                    title = it
                    showError = false
                },
                isError = showError,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.text_titleHint)) },
                trailingIcon = {
                    if (showError)
                        Icon(
                            Icons.Filled.Warning, "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    else
                        Icon(
                            Icons.Default.Edit, contentDescription = "add/edit",
                            tint = Color.Black
                        )
                } ,
                supportingText = { ShowSupportText(showError) }
            )

            Button(
                onClick = {
                    if (title.isEmpty()) {
                        showError = true
                    } else {
                        addFilter(title)
                    }
                },
                elevation = ButtonDefaults.buttonElevation(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(width = 4.dp))
                Text(stringResource(id = R.string.button_addFilter))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowToolBar() {
    TheGreatFilterTheme {
        TopAppBar(
            title = {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.app_name),
                    color = Color.White,
                    modifier = Modifier.padding( start = 90.dp)

                )
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                Icon(imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White)
            }
        )
    }
}

fun addFilter(title: String) {
    val filter = FilterModel()

    filter.title = title
    Timber.i("Title Entered is : $filter.title")

}

@Composable
fun ShowSupportText(isError : Boolean)
{
    if (isError)
        Text(
            text = "This Field is Required",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
        )
    else Text(text = "")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage() {


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                ,horizontalAlignment = Alignment.CenterHorizontally

        ) {

            val usernameState = remember { mutableStateOf(TextFieldValue()) }
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }

            val focus = LocalFocusManager.current

            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )

            Text(
                text = "Signup",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )

            OutlinedTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Username") })

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
                    focus.clearFocus(force = true)

                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "SIGN UP")
            }

            Text(
                text = "Go to login",
                color = Color.Black,
                modifier = Modifier
                    .padding(8.dp)

            )
        }

    }

}