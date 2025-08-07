package com.example.rentalreview.screen.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.rentalreview.screen.signUp.SignUpScreenViewModel
import com.example.rentalreview.ui.theme.RentalReviewTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    viewModel: LoginScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    if(uiState.showEmailPasswordReset){
        ForgotPasswordForm(
            uiState,
            viewModel::onEmailPasswordResetChange,
            viewModel::onForgotPasswordClick
        )
    }else{
        LoginForm(
            uiState,
            viewModel::onEmailChange,
            viewModel::onPasswordChange,
            { viewModel.onLoginClick(onLoginSuccess) },
            onSignUpClick,
            viewModel::onShowEmailPasswordResetChange
        )
    }
}

@Composable
fun LoginForm(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit
){
    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background).testTag("loginScreen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Login", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.fillMaxWidth(0.75f).testTag("emailTextField"),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = {
                Text(text = "Password", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.fillMaxWidth(0.75f).testTag("passwordTextField"),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Text(
            text = "Forgot Password?",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.75f).clickable{
                onForgotPasswordClick()
            }.testTag("forgotPasswordText"),
            color = MaterialTheme.colorScheme.primary
        )
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(0.75f).testTag("loginButton")
        ){
            Text(text = "Login")
        }
        OutlinedButton(
            onClick = onSignUpClick,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth(0.75f).testTag("signUpButton")
        ) {
            Text(text = "Sign Up", color = MaterialTheme.colorScheme.secondary)
        }

    }
}

@Composable
fun ForgotPasswordForm(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit
){
    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background).testTag("forgotPasswordScreen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text = "Reset Password", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        OutlinedTextField(
            value = uiState.emailPasswordReset,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.fillMaxWidth(0.75f).testTag("emailResetTextField"),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Button(
            onClick = onForgotPasswordClick,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(top = 10.dp)
                .testTag("forgotPasswordButton")
        ){
            Text(text = "Forgot Password")
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            LoginForm(
                LoginUiState(),
                onForgotPasswordClick = {}
            )
        }
    }
}

@Composable
@Preview
fun LoginDarkScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            LoginForm(
                LoginUiState(),
                onForgotPasswordClick = {}
            )
        }
    }
}

