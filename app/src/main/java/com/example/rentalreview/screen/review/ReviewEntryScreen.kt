package com.example.rentalreview.screen.review

import android.os.Build
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.ui.theme.RentalReviewTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEntryScreen(
    viewModel: ReviewScreenViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var star = uiState.rating

    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 70.dp, start = 20.dp, end = 20.dp)
    ) {
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
        DateRangeSelector(
            openDialog = uiState.openDialog,
            onOpenDialog = viewModel::openDialog,
            onDismiss = viewModel::closeDialog,
            startDate = startDate,
            endDate = endDate,
            onDateRangeSelected = { (startMillis, endMillis) ->
                viewModel.onDateRangeSelected(startMillis, endMillis)
            }
        )
        Text("Rating",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        Row{
            for (i in 1..5){
                if(i <= star){
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(40.dp)
                            .clickable {
                                star = i
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }else {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(40.dp)
                            .clickable {
                                star = i
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
        OutlinedTextField("", onValueChange = {}, modifier = Modifier
            .fillMaxWidth(0.85F)
            .padding(top = 5.dp),)
        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(top = 30.dp)
        ){
            Text(text = "Save")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangeSelector(
    openDialog: Boolean = false,
    onOpenDialog: () -> Unit = {},
    onDismiss: () -> Unit = {},
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit = { _ -> }
) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Column(modifier = Modifier
        .padding(top = 5.dp)
        .fillMaxWidth(0.85F)) {

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedButton(
            onClick = onOpenDialog,
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
                    startDate != null && endDate != null -> {
                        "${startDate.format(formatter)} ➝ ${endDate.format(formatter)}"
                    }

                    else -> "Select dates"
                },
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Modal do Picker
        if (openDialog) {
            DateRangePickerModal(
                onDateRangeSelected =  onDateRangeSelected ,
                onDismiss = onDismiss
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
        modifier = Modifier
            .fillMaxWidth(0.85F)
            .padding(top = 5.dp),
        expanded = false,
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {
        },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
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