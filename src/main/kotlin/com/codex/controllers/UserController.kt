package com.codex.controllers

import com.codex.models.*
import com.codex.shared.APIResponse

interface UserController {
    fun createUser(createUserDTO: CreateUserDTO): APIResponse<User>
    fun getUserById(id: String): APIResponse<User>
    fun updateUser(updateUser: UpdateUserDTO): APIResponse<User>
    fun listUsers(page: Int?, size: Int?): APIResponse<PaginationModel<List<User>>>
    fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<PaginationModel<List<User>>>
    fun deleteUser(id: String): APIResponse<Boolean>
    fun deleteAllUsers(): APIResponse<Boolean>
}