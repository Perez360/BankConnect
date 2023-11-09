package com.codex.dtos

import dev.morphia.annotations.Entity
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Entity
data class Session(
    val id: ObjectId,
    val userId: ObjectId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastAccessTime: LocalDateTime = LocalDateTime.now(),
    val expiry: LocalDateTime = LocalDateTime.now().plusMinutes(30)
)