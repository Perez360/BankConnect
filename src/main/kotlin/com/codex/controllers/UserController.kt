package com.codex.controllers

import com.codex.dtos.CreateUserDTO
import com.codex.dtos.LoginDTO
import com.codex.dtos.Session
import com.codex.dtos.UpdateUserDTO
import com.codex.models.FilterUserRequest
import com.codex.models.PaginationModel
import com.codex.models.User
import com.codex.shared.APIResponse

interface UserController {
    fun createUser(createUserDTO: CreateUserDTO): APIResponse<User>
    fun getUserById(id: String): APIResponse<User>
    fun updateUser(updateUser: UpdateUserDTO): APIResponse<User>
    fun login(loginDTO: LoginDTO): APIResponse<Session>
    fun listUsers(page: Int? = 1, size: Int? = 10): APIResponse<PaginationModel<User>>
    fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<PaginationModel<User>>
    fun deleteUser(id: String): APIResponse<User>
    fun deleteAllUsers(): APIResponse<Boolean>
}