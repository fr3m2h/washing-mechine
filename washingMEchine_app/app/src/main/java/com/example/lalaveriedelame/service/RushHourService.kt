package com.example.lalaveriedelame.service


import com.example.lalaveriedelame.model.JourDto
import kotlin.random.Random


object RushHourService {

    // Génère des données fictives pour une semaine
    fun genererDonneesSemaine(): List<JourDto> {
        val jours = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")
        return jours.map { jour ->
            val utilisationsParHeure = List(24) { Random.nextInt(0, 8) } // Génère un tableau aléatoire de 24 heures sachant qu'une machine peut etre utilisé une fois par heure au maximum il y a 8 utilisation par heure
            JourDto(
                jour = jour,
                utilisationsParHeure = utilisationsParHeure
            )
        }
    }
}
