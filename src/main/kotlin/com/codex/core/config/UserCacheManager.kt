package com.codex.core.config

import com.codex.models.User
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object UserCacheManager {
    private lateinit var storage: Cache<ObjectId, User>
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    init {
        if (Configuration.CACHING_STATUS) {
            storage = Caffeine.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES)
                .removalListener<ObjectId, User> { key, value, removalCause: RemovalCause ->
                    log.info("User $value] with Id= [$key], has been removed from cache because of $removalCause")
                }.build()

            log.info("Caching for users is enabled. Initializing caching mechanism...")
        }
    }

    fun cache(user: User) {
        if (Configuration.CACHING_STATUS) {
            storage.put(user.id, user)
            log.info("User has been cached successfully after being persisted in datastore")
        }
    }

    fun get(id: ObjectId): User? {
        if (Configuration.CACHING_STATUS) {
            log.info("Found a user in cache. Returning user from cache")
            return storage.getIfPresent(id)
        }
        return null
    }

    fun invalidate(id: ObjectId) {
        if (Configuration.CACHING_STATUS) {
            log.info("User with id [$id] has being removed from cache")
            storage.invalidate(id)
        }
    }

    fun invalidateAll() {
        if (Configuration.CACHING_STATUS)
            log.info("All users have been cleared from cache")
        storage.invalidateAll()
    }
}
