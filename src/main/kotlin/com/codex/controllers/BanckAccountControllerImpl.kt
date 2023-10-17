package com.codex.controllers

import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount
import com.codex.models.FilterBankAccountRequest
import com.codex.repos.BankAccountDAO
import com.codex.shared.APIResponse
import com.codex.util.wrapFailureInResponse
import com.codex.util.wrapSuccessInResponse
import org.bson.types.ObjectId
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class BankAccountControllerImpl(override val di: DI) : BankAccountController, DIAware {
    private val bankAccountDAO: BankAccountDAO by di.instance<BankAccountDAO>()
    override fun createBankAccount(user: BankAccount): APIResponse<BankAccount> {
        val newUser = bankAccountDAO.create(user) ?: throw ServiceException(-4, "Failed to save bank account")
        return wrapSuccessInResponse(newUser)
    }

    override fun getBankAccountById(id: ObjectId): APIResponse<BankAccount> {
        val oneBankAccount =
            bankAccountDAO.get(id) ?: return wrapFailureInResponse("Failed to fetch bank account by ID: $id")
        return wrapSuccessInResponse(oneBankAccount)
    }

    override fun updateBankAccount(updatedBankAccount: BankAccount): APIResponse<BankAccount> {
        val isBankAccountExists = bankAccountDAO.exists(updatedBankAccount.id)
        if (!isBankAccountExists) return wrapFailureInResponse("BankAccount does not exist with this ID: ${updatedBankAccount.id}")

        val updatedBankAccount =
            bankAccountDAO.update(updatedBankAccount) ?: throw ServiceException(-4, "Failed to update bank account")
        return wrapSuccessInResponse(updatedBankAccount)
    }

    override fun listBankAccounts(page: Int, size: Int): APIResponse<List<BankAccount>> {
        val listOfBankAccount = bankAccountDAO.list(page, size)
        return wrapSuccessInResponse(listOfBankAccount)
    }

    override fun filterBankAccounts(filterBankAccountRequest: FilterBankAccountRequest): APIResponse<List<BankAccount>> {
        val listOfBankAccount = bankAccountDAO.filter(filterBankAccountRequest)
        return wrapSuccessInResponse(listOfBankAccount)
    }

    override fun deleteBankAccount(id: ObjectId): APIResponse<Boolean> {
        val isUserExists = bankAccountDAO.exists(id)
        if (!isUserExists) return wrapFailureInResponse("BankAccount does not exist with this ID: $id")

        val deleteCount = bankAccountDAO.delete(id)
        if (deleteCount < 1) throw ServiceException(-4, "Failed to delete bank account")
        return wrapSuccessInResponse(true)
    }

    override fun deleteAllBankAccounts(): APIResponse<Boolean> {
        val deleteCount = bankAccountDAO.deleteAll()
        if (deleteCount < 1) throw ServiceException(-4, "Failed to delete all bank accounts")
        return wrapSuccessInResponse(true)
    }
}