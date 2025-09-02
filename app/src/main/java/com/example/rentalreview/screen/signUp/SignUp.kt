package com.example.rentalreview.screen.signUp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.ui.theme.RentalReviewTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    onLoginClick:() -> Unit = {},
    onSignUpSuccess: () -> Unit = {},
    viewModel: SignUpScreenViewModel = hiltViewModel()
){

    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage by SnackbarManager.snackbarMessages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let {
            val message = when (it) {
                is SnackbarMessage.StringSnackbar -> it.message
                is SnackbarMessage.ResourceSnackbar -> context.getString(it.message)
            }
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message)
                SnackbarManager.clearSnackbarMessage()
            }
        }
    }

    val uiSate by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState)}
    ) {
        SignUpForm(
            signUpUiState = uiSate,
            onNameChange = viewModel::onNameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
            onLoginClick = onLoginClick,
            onSignUpClick = { viewModel.onSignUpClick(onSignUpSuccess) }
        )
    }
}

@Composable
fun SignUpForm(signUpUiState: SignUpUiState,
               onNameChange: (String) -> Unit = {},
               onEmailChange: (String) -> Unit = {},
               onPasswordChange: (String) -> Unit = {},
               onRepeatPasswordChange: (String) -> Unit = {},
               onLoginClick: () -> Unit = {},
               onSignUpClick: () -> Unit = {},
               )
{
    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background).testTag("signUpScreen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Sign Up", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = signUpUiState.name,
            onValueChange = onNameChange,
            label = {
                Text(text = "Name", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.padding(top = 10.dp).testTag("nameTextField")
        )
        OutlinedTextField(
            value = signUpUiState.email,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.testTag("emailTextField")
        )
        OutlinedTextField(
            value = signUpUiState.password,
            onValueChange = onPasswordChange,
            label = {
                Text(text = "Password", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.testTag("passwordTextField"),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = _root_ide_package_.androidx.compose.ui.text.input.PasswordVisualTransformation()

        )
        OutlinedTextField(
            value = signUpUiState.repeatPassword,
            onValueChange = onRepeatPasswordChange,
            label = {
                Text(text = "Repeat Password", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.testTag("repeatPasswordTextField"),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = _root_ide_package_.androidx.compose.ui.text.input.PasswordVisualTransformation()
        )
        Text(
            text = "Forgot Password?",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 5.dp)
        )
        Button(
            onClick = onSignUpClick ,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(top = 10.dp).testTag("signUpButton")
        ){
            Text(text = "Sign Up")
        }
        OutlinedButton(
            onClick = onLoginClick,
            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Text(text = "Login", color = MaterialTheme.colorScheme.secondary)
        }

    }
}
@Composable
@Preview
fun LoginScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            SignUpScreen()
        }
    }
}

@Composable
@Preview
fun LoginDarkScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            SignUpScreen()
        }
    }
}