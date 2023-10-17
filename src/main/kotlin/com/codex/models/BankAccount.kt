package com.codex.models

import com.codex.enums.AccountStatus
import com.codex.enums.AccountType
import com.codex.util.validators.DateTimeOperations
import dev.morphia.annotations.*
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity("bank_accounts")
@Indexes(
    Index(
        fields = [
            Field("accountNumber"),
            Field("accountStatus"),
            Field("accountType"),
            Field("accountNumber"),
            Field("accountBalance"),
            Field("dateCreated"),
        ], options = IndexOptions(background = true)
    )
)
data class BankAccount(
    @Id
    val id: ObjectId,
    val userId: ObjectId,
    val bankName: String,
    @NotNull
    @Property("accType")
    val accountType: AccountType = AccountType.SAVINGS_ACCOUNT,
    @Property("accStatus")
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,
    @Property("accNum")
    val accountNumber: ObjectId,
    @Property("accBal")
    val accountBalance: Double,
    val pin: String,
    val lastDeposit: LocalDateTime,
    val dateCreated: LocalDateTime,
)

data class FilterBankAccountRequest(
    val bankName: String? = null,
    val userId: String? = null,
    val accountStatus: AccountStatus? = null,
    val accountType: AccountType? = null,
    val accountBalance: Double? = null,
    val dataCreated: LocalDateTime? = null,
    val page: Int = 1,
    val size: Int = 10
) {
    companion object {
        fun fromMap(map: Map<String, Any>): FilterBankAccountRequest {
            val bankName: String? by map.withDefault { null }
            val userId: String? by map.withDefault { null }
            val accountStatus: String? by map.withDefault { null }
            val accountType: String? by map.withDefault { null }
            val accountBalance: Double? by map.withDefault { null }
            val dataCreated: String? by map.withDefault { null }
            val page: Int? by map.withDefault { 1 }
            val size: Int? by map.withDefault { 10 }

            return FilterBankAccountRequest(
                bankName = bankName,
                userId = userId,
                accountBalance = accountBalance,
                accountStatus = accountStatus?.let { AccountStatus.valueOf(it) },
                accountType = accountType?.let { AccountType.valueOf(it) },
                dataCreated = DateTimeOperations.validateLocalDateTimeAndParse(dataCreated!!),
                page = page!!,
                size = size!!

            )

        }
    }
}