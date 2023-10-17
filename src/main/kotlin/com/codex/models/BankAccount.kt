package com.codex.models

import com.codex.enums.*
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
    val lastDeposit: LocalDateTime,
    val dateCreated: LocalDateTime,
)

data class FilterBankAccountRequest(
    val bankName: String? = null,
    val userId: ObjectId? = null,
    val accountStatus: AccountStatus? = null,
    val accountType: AccountType? = null,
    val accountBalance: Double? = null,
    val dataCreated: LocalDateTime? = null,
    val page: Int = 1,
    val size: Int = 10
)