package com.codex.util.listeners

import com.codex.core.config.UserCacheManager
import com.codex.models.User
import dev.morphia.annotations.PostPersist
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserEntityListener {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(user: User) {
        UserCacheManager.cache(user)
    }
}