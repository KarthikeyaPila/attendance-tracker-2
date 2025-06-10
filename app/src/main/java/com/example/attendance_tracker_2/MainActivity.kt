package com.example.attendance_tracker_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.attendance_tracker_2.ui.theme.Attendance_tracker_2Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                    val navController = rememberNavController()

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


@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(112.dp))
        AttendanceCard(attended = 0, total = 4) {
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
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){
        Text(
            text = "By: Karthikeya Pila",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}


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
fun HorizontalCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController) {
    var showDatePicker by remember { mutableStateOf(false) }
    val todayMillis = remember { System.currentTimeMillis() }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = todayMillis)
    var selectedDate by remember { mutableStateOf(convertMillisToDate(todayMillis)) }

    val workerList = (1..5).map { "Worker $it" }
    val checkStates = remember { workerList.map { mutableStateOf(false) } }

    var inputText by remember { mutableStateOf("") }
    val notes = remember { mutableStateListOf<String>() }

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
                                        selectedDate = convertMillisToDate(it)
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

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selectedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green,
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
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
                                value = inputText,
                                onValueChange = { newText ->
                                    inputText = newText.filter { it.isDigit() }
                                },
                                label = { Text("Today's Expense") },
                                placeholder = { Text("Enter number") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(onClick = {
                                if (inputText.isNotBlank()) {
                                    notes.add(inputText)
                                    inputText = ""
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

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


@Composable
fun WorkerCard(name: String, absence: Int, dailypay: Int, basepay: Int, absentDates: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.first().uppercaseChar().toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Absence: $absence",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = "Daily Pay: ₹$dailypay",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Total Pay: ₹${dailypay * (30 - absence)} / ₹$basepay",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Absent dates:",
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    absentDates.forEach { date ->
                        Text("- $date", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}


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

@Composable
fun AddWorkerDialog(onDismiss: () -> Unit, onAdd: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var basePay by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && basePay.toIntOrNull() != null) {
                        onAdd(name, basePay.toInt())
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Worker") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = basePay,
                    onValueChange = { basePay = it.filter { c -> c.isDigit() } },
                    label = { Text("Base Pay") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    )
}


@Composable
fun ExportDataScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Export Data Screen")
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Attendance_tracker_2Theme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}
