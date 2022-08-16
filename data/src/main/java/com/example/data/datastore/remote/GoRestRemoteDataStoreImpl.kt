package com.example.data.datastore.remote

import com.example.data.api.GoRestApiService
import com.example.data.entities.data.DataUsers
import com.example.data.mapper.ResponseUsersToDataUsersMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoRestRemoteDataStoreImpl @Inject constructor(
    private val apiService: GoRestApiService,
    private val mapper: ResponseUsersToDataUsersMapper
) :
    GoRestRemoteDataStore {
    override suspend fun getUsers(page: String): Flow<DataUsers> {
        //TODO: NEED TO IMPLEMENT TEST CASE FIRST
    }
}