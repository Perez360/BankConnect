package com.codex.repos

import com.codex.models.BankAccount
import com.codex.models.FilterBankAccountRequest
import org.bson.types.ObjectId

interface BankAccountRepo {
    fun create(bankAccount: BankAccount): BankAccount?
    fun get(id: ObjectId): BankAccount?
    fun exists(id: ObjectId): Boolean
    fun update(updateBankAccount: BankAccount): BankAccount
    fun list(page: Int, size: Int): List<BankAccount>
    fun filter(filterBankAccountRequest: FilterBankAccountRequest): List<BankAccount>
    fun delete(id: ObjectId): BankAccount?
    fun deleteAll(): Long
}