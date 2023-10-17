package com.codex.controllers

import com.codex.models.FilterUserRequest
import com.codex.models.User
import com.codex.shared.APIResponse
import org.bson.types.ObjectId

interface UserController {
    fun createUser(user: User): APIResponse<User>
    fun getUserById(id: ObjectId): APIResponse<User>
    fun updateUser(updatedUser: User): APIResponse<User>
    fun listUsers(page: Int, size: Int): APIResponse<List<User>>
    fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<List<User>>
    fun deleteUser(id: ObjectId): APIResponse<Boolean>
    fun deleteAllUsers(): APIResponse<Boolean>
}