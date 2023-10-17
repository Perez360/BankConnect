package com.codex.util.validators

import com.codex.domain.INVALID_BANK_NAME
import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount

class BankAccountValidator {
    companion object {
        fun validate(bankAccount: BankAccount) {
            if (bankAccount.bankName.isEmpty()) throw ServiceException(-2, INVALID_BANK_NAME)
        }
    }
}