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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendance_tracker_2.Components.AddWorkerDialog
import com.example.attendance_tracker_2.Components.WorkerCard
import com.example.attendance_tracker_2.InfoClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerInfoScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    val workers = remember {
        mutableStateListOf(
            InfoClass("Josh", 15, 1500, 45000, listOf("1 Mar", "5 Mar", "12 Mar")),
            InfoClass("Karthikeya", 28, 150, 4500, listOf("1 Mar", "2 Mar", "3 Mar", "4 Mar", "5 Mar")),
            InfoClass("Sahasra", 0, 1000, 30000, listOf(""))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Info") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Homescreen")
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
            items(workers) { (name, absence, pay, basepay, absentDates) ->
                WorkerCard(name, absence, pay, basepay, absentDates)
            }
        }

        if (showDialog) {
            AddWorkerDialog(
                onDismiss = { showDialog = false },
                onAdd = { name, basePay ->
                    workers.add(workers.size, InfoClass(name, absence = 0, pay = basePay / 30, basepay = basePay, listOf("")))
                    showDialog = false
                }
            )
        }
    }
}