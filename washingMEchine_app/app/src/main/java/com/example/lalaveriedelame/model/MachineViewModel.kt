package com.example.lalaveriedelame.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MachineViewModel: ViewModel() {
    var machine by mutableStateOf <MachineDto?>(null)
}