package com.codex.util.validators

import com.codex.enums.SystemErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount

class BankAccountValidator {
    companion object {
        fun validate(bankAccount: BankAccount) {
            if (bankAccount.bankName.isEmpty()) throw ServiceException(SystemErrorCode.BAD_REQUEST, "Invalid bank name")
        }
    }
}