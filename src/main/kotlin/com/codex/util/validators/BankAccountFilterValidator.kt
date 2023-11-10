package com.codex.util.validators

import com.codex.enums.AccountStatus
import com.codex.enums.AccountType
import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException

class BankAccountFilterValidator {

    companion object {
        fun validate(map: Map<String, Any>) {
            val accountStatus: String? by map
            val accountType: String? by map
            val dateCreated: String? by map

            accountStatus?.let {
                try {
                    AccountStatus.valueOf(it)
                } catch (ex: IllegalArgumentException) {
                    throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid filter provided for account status")
                }
            }

            accountType?.let {
                try {
                    AccountType.valueOf(it)
                } catch (ex: IllegalArgumentException) {
                    throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid filter provided for account type")
                }
            }

            dateCreated?.let { DateTimeOperations.validateLocalDateTimeAndParse(it) }

        }
    }
}