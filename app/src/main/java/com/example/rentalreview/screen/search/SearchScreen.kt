package com.example.rentalreview.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalreview.model.Country

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
        Row {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            Text("Search some reviews")
        }
        FilterCard(
            label = "Country",
            list = uiState.countries,
            selectedItem = uiState.selectedItem,
            selectedIndex = viewModel::onSelectedItem
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    label: String,
    updateExpandedOptions: (Boolean) -> Unit = {},
    expandedDropMenu: Boolean = false,
    list: List<Country>,
    selectedItem: String = "",
    selectedIndex: (index: Int) -> Unit,
    onEndOfListReached: (index: Int) -> Unit = {}
){

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.value) {
        if (scrollState.value == scrollState.maxValue && list.isNotEmpty()) {
            onEndOfListReached(list.lastIndex)
        }
    }

    Column(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp).testTag("FilterCard"),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        ExposedDropdownMenuBox(
            expanded = expandedDropMenu,
            onExpandedChange = updateExpandedOptions,
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
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
                expanded = true,
                onDismissRequest = {}
            ){
                list.forEach {
                    item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name, color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            selectedIndex(list.indexOf(item))
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
    FilterCard(
        label = "type",
        list = listOf(),
        selectedItem = "",
        selectedIndex = {}
    )
}