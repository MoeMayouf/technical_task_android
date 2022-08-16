package com.example.data.mapper

import com.example.data.entities.data.DataUsers
import com.example.data.entities.response.ResponseUsers
import javax.inject.Inject

interface ResponseUsersToDataUsersMapper {
    fun toDataModel(responseUsers: ResponseUsers): DataUsers
}

class ResponseUsersToDataUsersMapperImpl @Inject constructor() : ResponseUsersToDataUsersMapper {
    override fun toDataModel(responseUsers: ResponseUsers): DataUsers = DataUsers(
        id = responseUsers.id,
        name = responseUsers.name,
        email = responseUsers.email,
        gender = responseUsers.gender,
        status = responseUsers.status
    )
}