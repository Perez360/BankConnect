package com.codex.repos

import com.codex.dtos.Session
import org.bson.types.ObjectId

interface SessionRepo {
    fun create(userId: ObjectId): Session
    fun get(userId: ObjectId): Session
    fun getAll(): List<Session>
    fun destroy(userId: ObjectId): Boolean
}