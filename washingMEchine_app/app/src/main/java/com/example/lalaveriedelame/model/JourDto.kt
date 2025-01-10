package com.example.lalaveriedelame.model;


data class JourDto(
    val jour: String,
    val utilisationsParHeure: List<Int>
) {
    val totalUtilisations: Int
        get() = utilisationsParHeure.sum()
}
