package com.codex.repos

import com.codex.models.FilterUserRequest
import com.codex.models.User
import org.bson.types.ObjectId

interface UserDAO {
    fun create(user: User): User?
    fun get(id: ObjectId): User?
    fun exists(id: ObjectId): Boolean
    fun update(updateUser: User): User?
    fun list(page: Int?, size: Int?): List<User>
    fun filter(filterUserRequest: FilterUserRequest): List<User>
    fun delete(id: ObjectId): Long
    fun deleteAll(): Long

}