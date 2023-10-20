package com.codex.controllers.impl

import com.codex.controllers.BankAccountController
import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount
import com.codex.models.FilterBankAccountRequest
import com.codex.repos.BankAccountDAO
import com.codex.shared.APIResponse
import com.codex.util.validators.BankAccountValidator
import com.codex.util.wrapFailureInResponse
import com.codex.util.wrapSuccessInResponse
import org.bson.types.ObjectId
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class BankAccountControllerImpl(override val di: DI) : BankAccountController, DIAware {
    private val bankAccountDAO: BankAccountDAO by di.instance<BankAccountDAO>()
    override fun createBankAccount(bankAccount: BankAccount): APIResponse<BankAccount> {
        //Validating bank account details
        BankAccountValidator.validate(bankAccount)

        val newUser = bankAccountDAO.create(bankAccount) ?: throw ServiceException(-4, "Failed to save bank account")
        return wrapSuccessInResponse(newUser)
    }

    override fun getBankAccountById(id: String): APIResponse<BankAccount> {
        val bankAccountID = ObjectId(id)
        val oneBankAccount =
            bankAccountDAO.get(bankAccountID) ?: return wrapFailureInResponse("No bank account found with this id: $id")
        return wrapSuccessInResponse(oneBankAccount)
    }

    override fun updateBankAccount(bankAccount: BankAccount): APIResponse<BankAccount> {
        //Validating bank account details
        BankAccountValidator.validate(bankAccount)

        val isBankAccountExists = bankAccountDAO.exists(bankAccount.id)
        if (!isBankAccountExists) return wrapFailureInResponse("BankAccount does not exist with this ID: ${bankAccount.id}")

        val updatedBankAccount =
            bankAccountDAO.update(bankAccount) ?: throw ServiceException(-4, "Failed to update bank account")
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