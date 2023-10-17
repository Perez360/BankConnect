package com.codex.controllers

import com.codex.models.FilterUserRequest
import com.codex.models.Pagination
import com.codex.models.User
import com.codex.shared.APIResponse

interface UserController {
    fun createUser(user: User): APIResponse<User>
    fun getUserById(id: String): APIResponse<User>
    fun updateUser(updatedUser: User): APIResponse<User>
    fun listUsers(page: Int?, size: Int?): APIResponse<Pagination<List<User>>>
    fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<Pagination<List<User>>>
    fun deleteUser(id: String): APIResponse<Boolean>
    fun deleteAllUsers(): APIResponse<Boolean>
}