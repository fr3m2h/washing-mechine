package com.example.lalaveriedelame.model

data class MachineCommandDto(
    val id: Long,
    val isUsed: Boolean,
    val tempsRestant: Double?
)