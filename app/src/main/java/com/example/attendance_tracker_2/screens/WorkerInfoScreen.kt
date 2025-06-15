package com.example.attendance_tracker_2.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendance_tracker_2.Components.AddWorkerDialog
import com.example.attendance_tracker_2.Components.WorkerCard
import com.example.attendance_tracker_2.viewModels.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerInfoScreen(
    navController: NavController,
    workerViewModel: WorkerViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val workers = workerViewModel.workers

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Info") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Home screen")
                    }
                },

                actions = {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Worker"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(workers) { worker ->
                WorkerCard(
                    workerId = worker.workerId,
                    name = worker.name,
                    absence = worker.absence,
                    dailypay = worker.pay,
                    basepay = worker.basepay,
                    absentDates = worker.absentDates
                )
            }
        }

        if (showDialog) {
            AddWorkerDialog(
                onDismiss = { showDialog = false },
                onAdd = { name, basePay ->
                    workerViewModel.addWorker(name, basePay)
                    showDialog = false
                }
            )
        }
    }
}