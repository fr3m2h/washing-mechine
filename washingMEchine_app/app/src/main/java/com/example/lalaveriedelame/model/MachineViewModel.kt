package com.example.lalaveriedelame.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.lalaveriedelame.service.ApiServices
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MachineViewModel : ViewModel() {

    var machines = mutableStateOf<List<MachineDto>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    private val _machine = mutableStateOf<MachineDto?>(null)
    val machine: State<MachineDto?> get() = _machine

    fun setMachine(machineDto: MachineDto) {
        _machine.value = machineDto
    }

    // Fonction pour charger les machines depuis l'API
    fun loadMachines() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiServices.machinesApiService.findAll()
                machines.value = response
                errorMessage.value = null
            } catch (e: Exception) {
                errorMessage.value = "Erreur réseau : ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    // Fonction pour charger une machine spécifique par son ID
    fun loadMachineById(machineId: Long) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiServices.machinesApiService.findById(machineId)
                setMachine(response)
                errorMessage.value = null
            } catch (e: Exception) {
                errorMessage.value = "Erreur lors du chargement de la machine : ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
