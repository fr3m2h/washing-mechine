package com.example.lalaveriedelame.service



import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiServices {
    val machinesApiService: MachinesApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create()) // Convertisseur JSON via Moshi
            .baseUrl("http://localhost:8080/api/") // Remplacez par votre URL d'API
            .build()
            .create(MachinesApiService::class.java)
    }
}
