package com.example.data.datastore.remote

import com.example.data.entities.data.DataUsers
import kotlinx.coroutines.flow.Flow

interface GoRestRemoteDataStore {
    suspend fun getUsers(page: String): Flow<DataUsers>

}