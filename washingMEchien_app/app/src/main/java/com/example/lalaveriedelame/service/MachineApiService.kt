package com.example.lalaveriedelame.service

import com.example.lalaveriedelame.model.MachineCommandDto
import com.example.lalaveriedelame.model.MachineDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path



interface MachinesApiService {

    @GET("machines/{id}")
    suspend fun findById(@Path("id") id: Long): MachineDto

    @GET("machines")
    suspend fun findAll(): List<MachineDto>
}

