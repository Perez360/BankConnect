package com.codex.controllers.impl

import com.codex.controllers.BankAccountController
import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount
import com.codex.models.FilterBankAccountRequest
import com.codex.repos.BankAccountRepo
import com.codex.shared.APIResponse
import com.codex.util.validators.BankAccountValidator
import com.codex.util.wrapFailureInResponse
import com.codex.util.wrapSuccessInResponse
import org.bson.types.ObjectId
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class BankAccountControllerImpl(override val di: DI) : BankAccountController, DIAware {
    private val bankAccountDAO: BankAccountRepo by di.instance<BankAccountRepo>()
    override fun createBankAccount(bankAccount: BankAccount): APIResponse<BankAccount> {
        //Validating bank account details
        BankAccountValidator.validate(bankAccount)

        val newUser = bankAccountDAO.create(bankAccount) ?: throw ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "Failed to save bank account"
        )
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
            bankAccountDAO.update(bankAccount) ?: throw ServiceException(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Failed to update bank account"
            )
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

    override fun deleteBankAccount(id: ObjectId): APIResponse<BankAccount> {
        val isUserExists = bankAccountDAO.exists(id)
        if (!isUserExists) return wrapFailureInResponse("BankAccount does not exist with this ID: $id")

        val deletedAccount = bankAccountDAO.delete(id)
            ?: throw ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to delete bank account")
        return wrapSuccessInResponse(deletedAccount)
    }

    override fun deleteAllBankAccounts(): APIResponse<Boolean> {
        val deleteCount = bankAccountDAO.deleteAll()
        if (deleteCount < 1) throw ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "Failed to delete all bank accounts"
        )
        return wrapSuccessInResponse(true)
    }
}