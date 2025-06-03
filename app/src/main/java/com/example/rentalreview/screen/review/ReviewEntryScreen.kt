package com.example.rentalreview.screen.review

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.ui.theme.RentalReviewTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEntryScreen(
    onSaved: () -> Unit = {},
    viewModel: ReviewScreenViewModel = hiltViewModel(),
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var star = uiState.rating

    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()

    ReviewEntryForm(
        uiState = uiState,
        onSaved = { viewModel.onSave(onSaved) },
        startDate = startDate,
        endDate = endDate,
        star = star,
        updateExpandedOptions = viewModel::updateExpandedOptions,
        typeRental = viewModel::typeRental,
        openDialog = viewModel::openDialog,
        closeDialog = viewModel::closeDialog,
        onRatingChanged = viewModel::onRatingChanged,
        updateReview = viewModel::updateReview,
        onDateRangeSelected = viewModel::onDateRangeSelected,
        onTitleChanged = viewModel::onTitleChanged
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEntryForm(
    updateExpandedOptions: (Boolean) -> Unit = {},
    typeRental: (String) -> Unit = {},
    openDialog: () -> Unit = {},
    closeDialog: () -> Unit = {},
    uiState: ReviewScreenState,
    onRatingChanged: (Int) -> Unit = {},
    updateReview: (String) -> Unit = {},
    onDateRangeSelected: (Long?, Long?) -> Unit = { _, _ -> },
    onSaved: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    startDate: LocalDate?,
    endDate: LocalDate?,
    star: Int = 0
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 70.dp, start = 20.dp, end = 20.dp)
            .testTag("reviewEntryScreen")
    ) {
        Text("Rate Your Stay",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text("Title of your review",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChanged,
            modifier = Modifier
                .fillMaxWidth(0.85F)
        )
        Text("Address",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChanged,
            modifier = Modifier
                .fillMaxWidth(0.85F)
        )
        Text("Property type",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        PropertyTypeDropMenu(
            updateExpandedOptions = updateExpandedOptions,
            expanded = uiState.expandedOptions,
            updateType = typeRental,
            type = uiState.type,
            options = uiState.options
        )
        Text("Rental period",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        DateRangeSelector(
            openDialog = uiState.openDialog,
            onOpenDialog = openDialog,
            onDismiss = closeDialog,
            startDate = startDate,
            endDate = endDate,
            onDateRangeSelected = { (startMillis, endMillis) ->
                onDateRangeSelected(startMillis, endMillis)
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
                                onRatingChanged(i)
                            }
                            .testTag(i.toString()),
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
                                onRatingChanged(i)
                            }
                            .testTag(i.toString())
                    )
                }
            }
        }
        Text("Review",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        OutlinedTextField(
            value = uiState.review,
            onValueChange = updateReview,
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .padding(top = 5.dp)
                .testTag("reviewEntry"),
        )
        Button(
            onClick = onSaved,
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(top = 30.dp)
                .testTag("saveReview")
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
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dateRangeSelector")
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
fun PropertyTypeDropMenu(
    updateExpandedOptions: (Boolean) -> Unit = {},
    expanded: Boolean = false,
    updateType: (String) -> Unit = {},
    type: String = "",
    options: List<String>
){

    ExposedDropdownMenuBox(
        onExpandedChange =
            updateExpandedOptions,
        modifier = Modifier
            .fillMaxWidth(0.85F)
            .padding(top = 5.dp),
        expanded = expanded,
    ) {
        OutlinedTextField(
            value = type,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .testTag("propertyType"),
            label = {
                Text(
                    "Select the property type",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {}
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption,color = MaterialTheme.colorScheme.primary) },
                    onClick = {
                        updateType(selectionOption)
                        updateExpandedOptions(false)
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
            ReviewEntryForm(
                uiState = ReviewScreenState(),
                startDate = null,
                endDate = null,
                star = 0,
                onSaved = {}
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ReviewEntryDarkScreenPreview(){
    Surface{
        RentalReviewTheme(darkTheme = true, dynamicColor = false) {
            ReviewEntryForm(
                uiState = ReviewScreenState(),
                startDate = null,
                endDate = null,
                star = 0,
                onSaved = {})
        }
    }
}