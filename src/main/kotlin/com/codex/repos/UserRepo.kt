package com.codex.repos

import com.codex.models.FilterUserRequest
import com.codex.models.PaginationModel
import com.codex.models.User
import org.bson.types.ObjectId

interface UserRepo {
    fun create(user: User): User?
    fun get(id: ObjectId): User?
    fun get(email: String): User?
    fun exists(id: ObjectId): Boolean
    fun update(updateUser: User): User
    fun list(page: Int = 1, size: Int = 10): PaginationModel<List<User>>
    fun filter(filterUserRequest: FilterUserRequest): PaginationModel<List<User>>
    fun count(): Long
    fun delete(id: ObjectId): User?
    fun deleteAll(): Long

}