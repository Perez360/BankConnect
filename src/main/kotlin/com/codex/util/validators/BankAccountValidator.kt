package com.codex.util.validators

import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount

class BankAccountValidator {
    companion object {
        fun validate(bankAccount: BankAccount) {
            if (bankAccount.bankName.isEmpty()) throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid bank name")
        }
    }
}