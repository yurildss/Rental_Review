package com.example.rentalreview.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentalreview.ui.theme.RentalReviewTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEntryScreen(){
    val star = remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(top = 70.dp, start = 20.dp, end = 20.dp)) {
        Text("Rate Your Stay",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text("Property type",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        PropertyTypeDropMenu()
        Text("Rental period",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        DateRangeSelector()
        Text("Rating",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        Row{
            for (i in 1..5){
                if(i <= star.intValue){
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Star", modifier = Modifier.padding(end = 5.dp).size(40.dp).clickable{
                        star.intValue = i
                    },tint = MaterialTheme.colorScheme.primary)
                }else {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        modifier = Modifier.padding(end = 5.dp).size(40.dp).clickable{
                            star.intValue = i
                        }
                    )
                }
            }
        }
        Text("Review",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        OutlinedTextField("", onValueChange = {}, modifier = Modifier.fillMaxWidth(0.85F).padding(top = 5.dp),)
        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth().align(CenterHorizontally).padding(top = 30.dp)
        ){
            Text(text = "Save")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangeSelector() {
    val openDialog = remember { mutableStateOf(false) }
    val startDate = remember { mutableStateOf<LocalDate?>(null) }
    val endDate = remember { mutableStateOf<LocalDate?>(null) }

    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Column(modifier = Modifier.padding(top = 5.dp).fillMaxWidth(0.85F)) {
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedButton(
            onClick = { openDialog.value = true },
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = when {
                    startDate.value != null && endDate.value != null -> {
                        "${startDate.value!!.format(formatter)} ➝ ${endDate.value!!.format(formatter)}"
                    }

                    else -> "Select dates"
                },
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Modal do Picker
        if (openDialog.value) {
            DateRangePickerModal(
                onDateRangeSelected = { (startMillis, endMillis) ->
                    startDate.value = startMillis?.let { millis ->
                        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    endDate.value = endMillis?.let { millis ->
                        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                },
                onDismiss = { openDialog.value = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (dateRangePickerState.selectedStartDateMillis != null &&
                        dateRangePickerState.selectedEndDateMillis != null
                    ) {
                        onDateRangeSelected(
                            Pair(
                                dateRangePickerState.selectedStartDateMillis,
                                dateRangePickerState.selectedEndDateMillis
                            )
                        )
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = { Text(text = "Select date range") },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyTypeDropMenu(){
    val options = listOf("", "House", "Apartment", "Studio", "Room", "Commercial Space", "Other")
    ExposedDropdownMenuBox(
        onExpandedChange = {

        },
        modifier = Modifier.fillMaxWidth(0.85F).padding(top = 5.dp),
        expanded = false,
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {
        },
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            label = {
                Text("Select the property type", color = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "")
            }
        )
        ExposedDropdownMenu(
            expanded = false,
            onDismissRequest = {}
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption,color = MaterialTheme.colorScheme.primary) },
                    onClick = {
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ReviewEntryScreenPreview(){
    Surface{
        RentalReviewTheme(darkTheme = false, dynamicColor = false) {
            ReviewEntryScreen()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ReviewEntryDarkScreenPreview(){
    Surface{
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            ReviewEntryScreen()
        }
    }
}