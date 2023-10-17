package com.codex.repos

import com.codex.models.BankAccount
import com.codex.models.FilterBankAccountRequest
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.experimental.filters.Filters
import org.bson.types.ObjectId
import java.util.regex.Pattern

class BankAccountService(private val dataStore: Datastore) : BankAccountDAO {

    override fun create(bankAccount: BankAccount): BankAccount? = dataStore.withTransaction {
        dataStore.save(bankAccount)
    }


    override fun get(id: ObjectId): BankAccount? = dataStore.withTransaction {
        dataStore.find(BankAccount::class.java)
            .filter(Filters.eq("_id", id))
            .first()
    }

    override fun exists(id: ObjectId): Boolean {
        return dataStore.find(BankAccount::class.java)
            .filter(Filters.eq("_id", id))
            .any()
    }

    override fun update(updateBankAccount: BankAccount): BankAccount = dataStore.withTransaction {
        dataStore.merge(updateBankAccount, InsertOneOptions().unsetMissing(false))
    }

    override fun list(page: Int, size: Int): List<BankAccount> = dataStore.withTransaction {
        dataStore.find(BankAccount::class.java)
            .iterator(FindOptions().skip(page).limit(size).sort(Sort.descending(BankAccount::accountStatus.name)))
            .toList()
    }

    override fun filter(filterBankAccountRequest: FilterBankAccountRequest): List<BankAccount> =
        dataStore.withTransaction {
            dataStore.find(BankAccount::class.java).apply {
                filterBankAccountRequest.bankName?.let {
                    filter(Filters.regex("bankName").caseInsensitive().pattern(Pattern.compile("%$it%")))
                }
                filterBankAccountRequest.accountType?.let { filter(Filters.eq("accType", it)) }
                filterBankAccountRequest.accountStatus?.let { filter(Filters.eq("accStatus", it)) }
                filterBankAccountRequest.accountBalance?.let { filter(Filters.eq("accBal", it)) }
                filterBankAccountRequest.dataCreated?.let { filter(Filters.eq("datCreated", it)) }

                iterator(
                    FindOptions().skip(filterBankAccountRequest.page).limit(filterBankAccountRequest.size)
                        .sort(Sort.descending(BankAccount::dateCreated.name))
                )
            }.toList()
        }

    override fun delete(id: ObjectId): Long = dataStore.withTransaction {
        dataStore.find(BankAccount::class.java)
            .filter(Filters.eq("id", id))
            .delete(DeleteOptions()).deletedCount
    }

    override fun deleteAll(): Long = dataStore.withTransaction {
        dataStore.find(BankAccount::class.java)
            .delete(DeleteOptions().multi(true)).deletedCount
    }

}