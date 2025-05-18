package com.example.rentalreview.screen.openScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentalreview.R
import com.example.rentalreview.ui.theme.RentalReviewTheme

@Composable
fun OpenScree(
    onGetStarted: () -> Unit = {}
){
    Column(
        Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.chatgpt_image_12_de_mai__de_2025__21_11_19),
            contentDescription = "House",
            modifier = Modifier.size(400.dp).padding(top= 100.dp)
        )
        Text(
            text = stringResource(R.string.rental_reviews),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 100.dp, bottom = 30.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.welcome),
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.description),
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 20.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = onGetStarted,
            Modifier.fillMaxWidth(0.75f)
        ) {
            Text(text = stringResource(R.string.get_started))
        }
    }
}

@Composable
@Preview
fun OpenScreenPreview(){
    Surface(modifier = Modifier.fillMaxSize()) {
        RentalReviewTheme {
            OpenScree()
        }
    }
}


@Composable
@Preview
fun OpenScreenDarkPreview(){
    Surface(modifier = Modifier.fillMaxSize()) {
        RentalReviewTheme (darkTheme = true, dynamicColor = false) {
            OpenScree()
        }
    }
}