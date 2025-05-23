package com.example.rentalreview.screen.signUp

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
import com.example.rentalreview.ui.theme.RentalReviewTheme
import androidx.compose.runtime.getValue


@Composable
fun SignUpScreen(
    onLoginClick:() -> Unit = {},
    onSignUpSuccess: () -> Unit = {},
    viewModel: SignUpScreenViewModel = hiltViewModel()
){
    val uiSate by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpForm(
        uiSate,
        viewModel::onNameChange,
        viewModel::onEmailChange,
        viewModel::onPasswordChange,
        viewModel::onRepeatPasswordChange,
        onLoginClick
    ) { viewModel.onSignUpClick(onSignUpSuccess) }
}

@Composable
fun SignUpForm(signUpUiState: SignUpUiState,
               onNameChange: (String) -> Unit = {},
               onEmailChange: (String) -> Unit = {},
               onPasswordChange: (String) -> Unit = {},
               onRepeatPasswordChange: (String) -> Unit = {},
               onLoginClick: () -> Unit = {},
               onSignUpClick: (onSignUpSuccess: () -> Unit ) -> Unit = {},
               )
{
    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Sign Up", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = signUpUiState.name,
            onValueChange = onNameChange,
            label = {
                Text(text = "Name", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.padding(top = 10.dp)
        )
        OutlinedTextField(
            value = signUpUiState.email,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email", color = MaterialTheme.colorScheme.primary)
            },
        )
        OutlinedTextField(
            value = signUpUiState.password,
            onValueChange = onPasswordChange,
            label = {
                Text(text = "Password", color = MaterialTheme.colorScheme.primary)
            },
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
            onClick = { onSignUpClick },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(top = 10.dp)
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