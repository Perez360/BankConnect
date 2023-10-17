package com.codex.util.validators

import com.codex.domain.INVALID_ACCOUNT_STATUS_FILTER
import com.codex.domain.INVALID_ACCOUNT_TYPE_FILTER
import com.codex.enums.AccountStatus
import com.codex.enums.AccountType
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
                    throw ServiceException(-4, INVALID_ACCOUNT_STATUS_FILTER)
                }
            }

            accountType?.let {
                try {
                    AccountType.valueOf(it)
                } catch (ex: IllegalArgumentException) {
                    throw ServiceException(-4, INVALID_ACCOUNT_TYPE_FILTER)
                }
            }

            dateCreated?.let { DateTimeOperations.validateLocalDateTimeAndParse(it) }

        }
    }
}