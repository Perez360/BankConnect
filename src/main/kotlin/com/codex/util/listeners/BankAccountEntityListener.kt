package com.codex.util.listeners

import com.codex.core.config.BankAccountCacheManager
import com.codex.models.BankAccount
import dev.morphia.annotations.PostPersist

class BankAccountEntityListener {
    @PostPersist
    fun postPersist(bankAccount: BankAccount) {
        BankAccountCacheManager.cache(bankAccount)
    }
}