package com.codex.util.listeners

import com.codex.logger
import com.codex.models.User
import dev.morphia.annotations.PostPersist
import dev.morphia.annotations.PrePersist

class UserEntityListener {
    @PrePersist
    fun prePersist(user: User) {
        //TODO
        logger.info("User entity about to saved. $user")
    }

    @PostPersist
    fun postPersist(user: User) {
        //TODO
        logger.info("User entity saved $user")
    }
}