package com.example.attendance_tracker_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendance_tracker_2.ui.theme.Attendance_tracker_2Theme
import androidx.navigation.compose.rememberNavController as rememberNavController1

import com.example.attendance_tracker_2.screens.AttendanceScreen
import com.example.attendance_tracker_2.screens.HomeScreen
import com.example.attendance_tracker_2.screens.ExportDataScreen
import com.example.attendance_tracker_2.screens.WorkerInfoScreen




data class InfoClass(
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

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController)
                        }
                        composable(Screen.Attendance.route) {
                            AttendanceScreen(navController)
                        }
                        composable(Screen.WorkerInfo.route) {
                            WorkerInfoScreen(navController)
                        }
                        composable(Screen.ExportData.route) {
                            ExportDataScreen()
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
        HomeScreen(navController)
        AttendanceScreen(navController)
    }
}
