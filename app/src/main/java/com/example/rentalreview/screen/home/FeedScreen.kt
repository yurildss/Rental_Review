package com.example.rentalreview.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentalreview.ui.theme.RentalReviewTheme

@Composable
fun ReviewCard(){
    Column(
        Modifier.height(530.dp).background(MaterialTheme.colorScheme.background).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = com.example.rentalreview.R.drawable.chatgpt_image_12_de_mai__de_2025__21_11_19),
            contentDescription = "House",
            modifier = Modifier.size(300.dp),
        )
        Text("Avoid this place!",
            fontSize = 23.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(start = 10.dp).fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text("123 Maple St, Anytown, USA",
            fontSize = 19.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(start = 10.dp, top = 5.dp).fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
        Row(Modifier.padding(start = 10.dp, top = 5.dp).fillMaxWidth()) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
            Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
            Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
            Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
            Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
        }
        Text(
            "The landlord was very unresponsive to maintenance requests. Plumbing issues persisted throughout my stay",
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            Modifier.padding(start = 10.dp, top = 5.dp).fillMaxWidth().height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Like")
                Text("Like", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Like")
                Text("Comment", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Like")
                Text("Like", modifier = Modifier.padding(start = 5.dp), color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
@Preview
fun ReviewCardPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            ReviewCard()
        }
    }
}

@Composable
@Preview
fun ReviewBlackCardPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            ReviewCard()
        }
    }
}