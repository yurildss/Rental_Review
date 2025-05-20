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
import com.example.rentalreview.ui.theme.RentalReviewTheme


@Composable
fun SignUpScrenn(){
    SignUpForm()
}

@Composable
fun SignUpForm(){
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Sign Up", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            label = {
                Text(text = "Name", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.padding(top = 10.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            label = {
                Text(text = "Email", color = MaterialTheme.colorScheme.primary)
            },
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            label = {
                Text(text = "Password", color = MaterialTheme.colorScheme.primary)
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            label = {
                Text(text = "Repeat Password", color = MaterialTheme.colorScheme.primary)
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        Text(
            text = "Forgot Password?",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 5.dp)
        )
        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth(0.75f).padding(top = 10.dp)
        ){
            Text(text = "Sign Up")
        }
        OutlinedButton(
            onClick = { },
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
            SignUpScrenn()
        }
    }
}

@Composable
@Preview
fun LoginDarkScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            SignUpScrenn()
        }
    }
}