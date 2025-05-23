package com.example.rentalreview.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    viewModel: LoginScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginForm(uiState,
        viewModel::onEmailChange,
        viewModel::onPasswordChange,
        { viewModel.onLoginClick(onLoginSuccess) },
        onSignUpClick)
}

@Composable
fun LoginForm(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLoginClick: (onLoginSuccess: () -> Unit) -> Unit = {},
    onSignUpClick: () -> Unit = {}
){
    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Login", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email", color = MaterialTheme.colorScheme.primary)
            },
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
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = _root_ide_package_.androidx.compose.ui.text.input.PasswordVisualTransformation()
        )
        Text(text = "Forgot Password?", textAlign = TextAlign.Center)
        Button(
            onClick = {
                onLoginClick
            },
            modifier = Modifier.fillMaxWidth(0.75f)
        ){
            Text(text = "Login")
        }
        OutlinedButton(
            onClick = onSignUpClick,
            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Text(text = "Sign Up", color = MaterialTheme.colorScheme.secondary)
        }

    }
}
@Composable
@Preview
fun LoginScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            LoginScreen()
        }
    }
}

@Composable
@Preview
fun LoginDarkScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            LoginScreen()
        }
    }
}