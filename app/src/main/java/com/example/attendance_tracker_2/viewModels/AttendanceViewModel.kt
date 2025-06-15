package com.example.attendance_tracker_2.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendance_tracker_2.Helpers.convertMillisToDate

class AttendanceViewModel : ViewModel() {

    val todayMillis = System.currentTimeMillis()
    var selectedDate = mutableStateOf(convertMillisToDate(todayMillis))

    // Let's assume 5 workers for now
    val workerList = (1..5).map { "Worker $it" }

    val checkStates = workerList.map { mutableStateOf(false) }

    var inputText = mutableStateOf("")

    val notes = mutableStateListOf<String>()
}
