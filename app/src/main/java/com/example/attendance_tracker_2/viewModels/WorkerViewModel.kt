package com.example.attendance_tracker_2.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.attendance_tracker_2.InfoClass

class WorkerViewModel : ViewModel(){

    private val _workers = mutableStateListOf(
        InfoClass(1,"Josh", 15, 1500, 45000, listOf("1 Mar", "5 Mar", "12 Mar")),
        InfoClass(2, "Karthikeya", 28, 150, 4500, listOf("1 Mar", "2 Mar", "3 Mar", "4 Mar", "5 Mar")),
        InfoClass(3, "Sahasra", 0, 1000, 30000, listOf())
    )

    val workers: List<InfoClass> get() = _workers

    private var _counter = _workers.size

    fun addWorker(name: String, basePay: Int) {
        val workerId = _counter + 1
        val dailyPay = basePay / 30
        _workers.add(InfoClass(workerId, name, absence = 0, pay = dailyPay, basepay = basePay, listOf()))
    }

}
