package com.example.attendance_tracker_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendance_tracker_2.ViewModels.AttendanceViewModel
import com.example.attendance_tracker_2.ui.theme.Attendance_tracker_2Theme
import androidx.navigation.compose.rememberNavController as rememberNavController1

import com.example.attendance_tracker_2.screens.AttendanceScreen
import com.example.attendance_tracker_2.screens.ExportDataScreen
import com.example.attendance_tracker_2.screens.HomeScreen
import com.example.attendance_tracker_2.screens.WorkerInfoScreen
import com.example.attendance_tracker_2.viewModels.WorkerViewModel


data class InfoClass(
    val workerId: Int,
    val name: String,
    val absence: Int,
    val pay: Int,
    val basepay: Int,
    val absentDates: List<String>
)


sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Attendance : Screen("attendance")
    data object WorkerInfo : Screen("worker_info")
    data object ExportData : Screen("export_data")
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Attendance_tracker_2Theme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController1()
                    val workerViewModel: WorkerViewModel = viewModel()
                    val attendanceViewModel: AttendanceViewModel = viewModel()

                    NavHost(navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                navController = navController,
                                workerViewModel = workerViewModel,
                                attendanceViewModel = attendanceViewModel
                            )
                        }
                        composable("worker_info") {
                            WorkerInfoScreen(
                                navController = navController,
                                workerViewModel = workerViewModel
                            )
                        }
                        composable("attendance") {
                            AttendanceScreen(
                                navController = navController,
                                attendanceViewModel = attendanceViewModel,
                                workerViewModel = workerViewModel
                            )
                        }
                        composable("export_data"){
                            ExportDataScreen(
                                navController = navController,
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Attendance_tracker_2Theme {
        val navController = rememberNavController1()
    }
}
