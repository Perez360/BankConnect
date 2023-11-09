package com.codex.controllers

import com.codex.models.BankAccount
import com.codex.models.FilterBankAccountRequest
import com.codex.shared.APIResponse
import org.bson.types.ObjectId


interface BankAccountController {
    fun createBankAccount(bankAccount: BankAccount): APIResponse<BankAccount>
    fun getBankAccountById(id: String): APIResponse<BankAccount>
    fun updateBankAccount(bankAccount: BankAccount): APIResponse<BankAccount>
    fun listBankAccounts(page: Int, size: Int): APIResponse<List<BankAccount>>
    fun filterBankAccounts(filterBankAccountRequest: FilterBankAccountRequest): APIResponse<List<BankAccount>>
    fun deleteBankAccount(id: ObjectId): APIResponse<BankAccount>
    fun deleteAllBankAccounts(): APIResponse<Boolean>
}