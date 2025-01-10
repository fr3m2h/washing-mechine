package com.example.lalaveriedelame.model

import androidx.core.text.isDigitsOnly
import kotlin.random.Random

object MachineService {

    // Simulated list of machines (this would normally be replaced with actual data, like a database or network call)
    private val MACHINES = mutableListOf<MachineDto>()

    // Fonction pour générer une machine aléatoire
    fun generateRandomMachine(): MachineDto {
        val id = Random.nextLong(1, 8) // Génère un id aléatoire entre 1 et 8
        val isUsed = Random.nextBoolean() // Génère un état aléatoire pour savoir si la machine est utilisée ou non
        val tempsRestant = if (isUsed) Random.nextDouble(1.0, 60.0) else null // Si la machine est utilisée, générer un temps restant aléatoire

        return MachineDto(
            id = id,
            isUsed = isUsed,
            tempsRestant = tempsRestant
        )
    }

    // Fonction pour générer une liste de machines aléatoires
    fun generateRandomMachines(count: Int): List<MachineDto> {
        val machines = List(count) { generateRandomMachine() }
        MACHINES.addAll(machines)
        return machines
    }

    // Fonction pour récupérer toutes les machines triées par ID
    fun findAll(): List<MachineDto> {
        return MACHINES.sortedBy { it.id }
    }

    // Fonction pour récupérer une machine par son ID
    fun findById(id: Long): MachineDto? {
        return MACHINES.find { it.id == id }
    }

    // Fonction pour récupérer une machine par son état (utilisée ou non)
    fun findByName(name: String): MachineDto? {
        return MACHINES.find { it.id.toString() == name }
    }

    // Fonction pour mettre à jour une machine par son ID
    fun updateMachine(id: Long, updatedMachine: MachineDto): MachineDto? {
        val index = MACHINES.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = updatedMachine.copy(id = id)
            MACHINES[index] = updated
            return updated
        }
        return null
    }

    // Fonction pour récupérer une machine soit par ID soit par nom (id sous forme de chaîne ou nom sous forme de chaîne)
    fun findByNameOrId(nameOrId: String?): MachineDto? {
        if (!nameOrId.isNullOrEmpty()) {
            return if (nameOrId.isDigitsOnly()) {
                findById(nameOrId.toLong())
            } else {
                findByName(nameOrId)
            }
        }
        return null
    }
}
