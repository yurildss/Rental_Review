package com.example.rentalreview.screen.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentalreview.ui.theme.RentalReviewTheme

@Composable
fun PerfilScreen(){

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Perfil",
            modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally).size(60.dp)
        )
        Text("John Doe",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp).align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 20.dp)
                .height(56.dp)
                .align(Alignment.CenterHorizontally),

        ) {
            Text("My Reviews")
        }
        Button(onClick = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 10.dp)
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Text("Favorites")
        }
        Button(onClick = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 10.dp)
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Text("Settings")
        }
        OutlinedButton(onClick = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 10.dp)
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Text("My Reviews", color = MaterialTheme.colorScheme.secondary)
        }
    }

}

@Composable
@Preview
fun PerfilScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            PerfilScreen()
        }
    }
}

@Composable
@Preview
fun PerfilBlackScreenPreview(){
    Surface {
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            PerfilScreen()
        }
    }
}