package com.example.attendance_tracker_2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.attendance_tracker_2.Components.HorizontalCard
import com.example.attendance_tracker_2.Screen
import com.example.attendance_tracker_2.ViewModels.AttendanceViewModel
import com.example.attendance_tracker_2.viewModels.WorkerViewModel


@Composable
fun AttendanceCard(attended: Int, total: Int, onClick: () -> Unit = {}) {
    HorizontalCard(
        title = "Attendance",
        subtitle = "$attended / $total",
        icon = Icons.Default.Check,
        onClick = onClick
    )
}

@Composable
fun WorkerInfoCard(workerCount: Int, onClick: () -> Unit = {}) {
    HorizontalCard(
        title = "Worker’s Info",
        subtitle = "$workerCount workers",
        icon = Icons.Default.Person,
        onClick = onClick
    )
}

@Composable
fun ExportDataCard(lastExportDate: String, onClick: () -> Unit = {}) {
    HorizontalCard(
        title = "Export Data",
        subtitle = "last exported - $lastExportDate",
        icon = Icons.Default.Share,
        onClick = onClick
    )
}

@Composable
fun HomeScreen(
    navController: NavController,
    workerViewModel: WorkerViewModel,
    attendanceViewModel: AttendanceViewModel
) {

    val checkStates = attendanceViewModel.checkStates
    val checkedCount = checkStates.count { it.value }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(112.dp))
        AttendanceCard(
            attended = checkedCount,
            total = attendanceViewModel.checkStates.size
        ) {
            navController.navigate(Screen.Attendance.route)
        }
        Spacer(modifier = Modifier.height(24.dp))
        WorkerInfoCard(workerCount = 4) {
            navController.navigate(Screen.WorkerInfo.route)
        }
        Spacer(modifier = Modifier.height(24.dp))
        ExportDataCard(lastExportDate = "31 Mar 25") {
            navController.navigate(Screen.ExportData.route)

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "By: Karthikeya Pila",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}