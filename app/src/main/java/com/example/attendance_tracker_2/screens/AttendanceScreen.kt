package com.example.attendance_tracker_2.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.attendance_tracker_2.Helpers.convertMillisToDate
import com.example.attendance_tracker_2.ViewModels.AttendanceViewModel
import com.example.attendance_tracker_2.viewModels.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    attendanceViewModel: AttendanceViewModel,
    workerViewModel: WorkerViewModel
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    val workerList = attendanceViewModel.workerList
    val checkStates = attendanceViewModel.checkStates
    val inputText = attendanceViewModel.inputText
    val notes = attendanceViewModel.notes
    val selectedDate = attendanceViewModel.selectedDate

    val checkedCount = checkStates.count { it.value }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Attendance") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Arrow")
                    }
                },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Calendar")
                    }

                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    showDatePicker = false
                                    datePickerState.selectedDateMillis?.let {
                                        selectedDate.value = convertMillisToDate(it)
                                    }
                                }) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("Cancel")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = selectedDate.value,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$checkedCount / ${checkStates.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(checkStates) { index, state ->
                        ListItem(
                            headlineContent = {
                                Text("Worker ${index + 1}")
                            },
                            trailingContent = {
                                Checkbox(
                                    checked = state.value,
                                    onCheckedChange = { state.value = it }
                                )
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputText.value,
                                onValueChange = { newText ->
                                    inputText.value = newText.filter { it.isDigit() }
                                },
                                label = { Text("Today's Expense") },
                                placeholder = { Text("Enter number") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = {
                                if (inputText.value.isNotBlank()) {
                                    notes.add(inputText.value)
                                    inputText.value = ""
                                }
                            }) {
                                Text("OK")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (notes.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Logged Expenses:", style = MaterialTheme.typography.bodyLarge)
                        notes.forEach {
                            Text("- ₹$it")
                        }
                    }
                }
            }
        }
    )
}
