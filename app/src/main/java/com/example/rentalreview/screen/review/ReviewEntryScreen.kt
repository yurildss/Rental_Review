package com.example.rentalreview.screen.review

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.rentalreview.common.SnackbarManager
import com.example.rentalreview.common.SnackbarMessage
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.State
import com.example.rentalreview.screen.search.FilterCityCard
import com.example.rentalreview.screen.search.FilterCountryCard
import com.example.rentalreview.screen.search.FilterStateCard
import com.example.rentalreview.ui.theme.RentalReviewTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEntryScreen(
    onSaved: () -> Unit = {},
    viewModel: ReviewScreenViewModel = hiltViewModel(),
){

    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage by SnackbarManager.snackbarMessages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let {
            val message = when (it) {
                is SnackbarMessage.StringSnackbar -> it.message
                is SnackbarMessage.ResourceSnackbar -> context.getString(it.message)
            }
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message)
                SnackbarManager.clearSnackbarMessage()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var star = uiState.rating

    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        LazyColumn {
            item {
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
                    onTitleChanged = viewModel::onTitleChanged,
                    onStreetChanged = viewModel::onStreetChanged,
                    onNumberChanged = viewModel::onNumberChanged,
                    onCountrySelected = viewModel::onCountryChanged,
                    expandedCountryOptions = uiState.expandedCountryOptions,
                    onCountryExpandedOptions = viewModel::onCountryExpandedOptions,
                    countryList = uiState.listOfCountries,
                    onStateExpandedOptions = viewModel::onStateExpandedOptions,
                    onCityExpandedOptions = viewModel::onCityExpandedOptions,
                    onStateSelected = viewModel::onStateSelected,
                    onCitySelected = viewModel::onCitySelected,
                    onImageSelect = viewModel::onImageSelect,
                    imageGallery = uiState.imageGallery,
                    enableButton = uiState.buttonClick
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewEntryForm(
    onStreetChanged: (String) -> Unit,
    onNumberChanged: (String) -> Unit,
    expandedCountryOptions: Boolean,
    onCountryExpandedOptions: () -> Unit,
    onStateExpandedOptions: () -> Unit,
    onCityExpandedOptions: () -> Unit,
    onCountrySelected: (Country) -> Unit,
    onStateSelected: (State) -> Unit,
    onCitySelected: (City) -> Unit,
    countryList: List<Country>,
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
    star: Int = 0,
    onImageSelect: (Uri) -> Unit,
    imageGallery: Uri,
    enableButton: Boolean
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            .testTag("reviewEntryScreen"),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
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
                .testTag("titleEntry"),
        )
        PropertyImageSelect(imageGallery,onImageSelect)
        Text("Address",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 15.dp)
        )
        FilterCountryCard(
            label = "Country",
            list = countryList,
            selectedItem = uiState.selectedCountryItem,
            selectedIndex = {},
            expandedDropMenu = expandedCountryOptions,
            updateExpandedOptions = onCountryExpandedOptions,
            onSelected = onCountrySelected,
        )
        FilterStateCard(
            label = "State",
            list = uiState.listOfStates,
            selectedItem = uiState.selectedStateItem,
            selectedIndex = {},
            expandedDropMenu = uiState.expandedStateOptions,
            updateExpandedOptions = onStateExpandedOptions,
            onSelected = onStateSelected
        )

        FilterCityCard(
            label = "City",
            list = uiState.listOfCities,
            selectedItem = uiState.selectedCityItem,
            selectedIndex = {},
            expandedDropMenu = uiState.expandedCityOptions,
            updateExpandedOptions = onCityExpandedOptions,
            onSelected = onCitySelected
        )
        Row(modifier = Modifier.fillMaxWidth(0.85F) ,horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = uiState.street,
                onValueChange = onStreetChanged,
                modifier = Modifier
                    .fillMaxWidth(0.65F)
                    .testTag("streetEntry"),
                label = { Text("Street", color = MaterialTheme.colorScheme.primary) }
            )
            OutlinedTextField(
                value = uiState.number,
                onValueChange = onNumberChanged,
                modifier = Modifier
                    .fillMaxWidth(0.95F)
                    .testTag("numberEntry"),
                label = { Text("n", color = MaterialTheme.colorScheme.primary) }
            )
        }
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
            enabled = !enableButton,
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
                    },
                    modifier = Modifier.testTag(selectionOption)
                )
            }
        }
    }
}

@Composable
fun PropertyImageSelect(
    imageGallery: Uri,
    onImageSelected: (Uri) -> Unit
){

    var launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ){
        it?.let { p1 -> onImageSelected(p1) }
    }
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageGallery != Uri.EMPTY) {
            AsyncImage(
                model = imageGallery,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Select image")
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
                onSaved = {},
                onStreetChanged = {},
                onNumberChanged = { },
                onCountrySelected = {},
                updateExpandedOptions = {},
                typeRental = {},
                openDialog = { },
                closeDialog = {},
                onRatingChanged = {},
                updateReview = {},
                onDateRangeSelected = { _, _ -> },
                onTitleChanged = {},
                expandedCountryOptions = false,
                onCountryExpandedOptions = { },
                countryList = emptyList(),
                onStateExpandedOptions = {},
                onCityExpandedOptions = {},
                onStateSelected = {},
                onCitySelected = {},
                onImageSelect = {},
                imageGallery = TODO(),
                enableButton = false
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
                onSaved = {},
                onStreetChanged = {},
                onNumberChanged = { },
                onCountrySelected = {},
                updateExpandedOptions = {},
                typeRental = {},
                openDialog = { },
                closeDialog = {},
                onRatingChanged = {},
                updateReview = {},
                onDateRangeSelected = { _, _ -> },
                onTitleChanged = {},
                expandedCountryOptions = false,
                onCountryExpandedOptions = { },
                countryList = listOf(),
                onStateExpandedOptions = {},
                onCityExpandedOptions = {},
                onStateSelected = {},
                onCitySelected = {},
                onImageSelect = {  },
                imageGallery = TODO(),
                enableButton = false
            )
        }
    }
}