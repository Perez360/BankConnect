package com.codex.core.config

import com.codex.models.BankAccount
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object BankAccountCacheManager {
    private lateinit var storage: Cache<ObjectId, BankAccount>
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    init {
        if (Configuration.CACHING_STATUS) {
            storage =
                Caffeine.newBuilder()
                    .expireAfterAccess(30, TimeUnit.MINUTES)
                    .removalListener<ObjectId, BankAccount> { key, value, removalCause: RemovalCause ->
                        log.info("Bank account $value] with Id= [$key], has been removed from cache because of $removalCause")
                    }
                    .build()

            log.info("Caching for bank accounts is enabled. Initializing caching mechanism...")
        }
    }

    fun cache(bankAccount: BankAccount) {
        if (Configuration.CACHING_STATUS) {
            storage.put(bankAccount.id, bankAccount)
            log.info("Bank account has been cached successfully after being persisted in datastore")
        }
    }

    fun get(id: ObjectId): BankAccount? {
        if (Configuration.CACHING_STATUS) {
            log.info("Found a bank account in cache. Returning user from cache")
            return storage.getIfPresent(id)
        }
        return null
    }

    fun invalidate(id: ObjectId) {
        if (Configuration.CACHING_STATUS) {
            log.info("Bank account with id [$id] has being removed from cache")
            storage.invalidate(id)
        }
    }
}
