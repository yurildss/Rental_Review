package com.example.rentalreview.screen.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.State
import com.example.rentalreview.screen.favorites.ReviewsList

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize().padding(10.dp),)
    {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            Text("Search some reviews")
        }
        FilterCard(
            label = "Country",
            list = uiState.countries,
            selectedItem = uiState.selectedCountryItem,
            selectedIndex = viewModel::onSelectedItemIndex,
            expandedDropMenu = uiState.expandedCountryOptions,
            updateExpandedOptions = viewModel::updateExpandedOptions,
            onSelected = viewModel::onSelectCountryItem
        )
        FilterStateCard(
            label = "State",
            list = uiState.states,
            selectedItem = uiState.selectedStateItem,
            selectedIndex = viewModel::onSelectedItemIndex,
            expandedDropMenu = uiState.expandedStateOptions,
            updateExpandedOptions = viewModel::updateExpandedStateOptions,
            onSelected = viewModel::onSelectStateItem
        )

        FilterCityCard(
            label = "City",
            list = uiState.cities,
            selectedItem = uiState.selectedCityItem,
            selectedIndex = viewModel::onSelectedItemIndex,
            expandedDropMenu = uiState.expandedCityOptions,
            updateExpandedOptions = viewModel::updateExpandedCityOptions,
            onSelected = viewModel::onSelectCityItem
        )

        OutlinedButton(
            onClick = viewModel::onSearch,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .testTag("searchButton")
        ) {
            Text(text = "Search", color = MaterialTheme.colorScheme.secondary)
        }
        ReviewsList(
            reviews = uiState.reviews,
            userId = uiState.userId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterStateCard(
    label: String,
    updateExpandedOptions: () -> Unit,
    expandedDropMenu: Boolean,
    list: List<State>,
    selectedItem: State,
    selectedIndex: (index: Int) -> Unit,
    onSelected: (item: State) -> Unit
){

    Column(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .testTag("FilterCard"),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        ExposedDropdownMenuBox(
            expanded = expandedDropMenu,
            onExpandedChange = { updateExpandedOptions() },
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                value = selectedItem.name,
                onValueChange = {

                },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(label),
                label = {
                    Text(
                        "Select the $label of the property",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons
                            .AutoMirrored
                            .Filled
                            .KeyboardArrowRight,
                        contentDescription = ""
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expandedDropMenu,
                onDismissRequest = {
                    updateExpandedOptions()
                }
            ){
                list.forEach {
                        item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name, color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            selectedIndex(list.indexOf(item))
                            onSelected(item)
                            updateExpandedOptions()
                        },
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    label: String,
    updateExpandedOptions: () -> Unit = {},
    expandedDropMenu: Boolean,
    list: List<Country>,
    selectedItem: Country,
    selectedIndex: (index: Int) -> Unit,
    onSelected: (item: Country) -> Unit
){

    Column(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .testTag("FilterCard"),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        ExposedDropdownMenuBox(
            expanded = expandedDropMenu,
            onExpandedChange = { updateExpandedOptions() },
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                value = selectedItem.name,
                onValueChange = {

                },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(label),
                label = {
                    Text(
                        "Select the $label of the property",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons
                            .AutoMirrored
                            .Filled
                            .KeyboardArrowRight,
                        contentDescription = ""
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expandedDropMenu,
                onDismissRequest = {
                    updateExpandedOptions()
                }
            ){
                list.forEach {
                    item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name, color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            selectedIndex(list.indexOf(item))
                            onSelected(item)
                            updateExpandedOptions()
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCityCard(
    label: String,
    updateExpandedOptions: () -> Unit = {},
    expandedDropMenu: Boolean,
    list: List<City>,
    selectedItem: City,
    selectedIndex: (index: Int) -> Unit,
    onSelected: (item: City) -> Unit
){

    Column(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .testTag("FilterCard"),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        ExposedDropdownMenuBox(
            expanded = expandedDropMenu,
            onExpandedChange = { updateExpandedOptions() },
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                value = selectedItem.name,
                onValueChange = {

                },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(label),
                label = {
                    Text(
                        "Select the $label of the property",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons
                            .AutoMirrored
                            .Filled
                            .KeyboardArrowRight,
                        contentDescription = ""
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expandedDropMenu,
                onDismissRequest = {
                    updateExpandedOptions()
                }
            ){
                list.forEach {
                        item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name, color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            selectedIndex(list.indexOf(item))
                            onSelected(item)
                            updateExpandedOptions()
                        },
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun FilterCardPreview(){

    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            Text("Search some reviews")
        }
        FilterCard(
            label = "type",
            list = listOf(),
            selectedItem = Country("", "", ""),
            selectedIndex = {},
            expandedDropMenu = false,
            updateExpandedOptions = {},
            onSelected = {}
        )
    }
}