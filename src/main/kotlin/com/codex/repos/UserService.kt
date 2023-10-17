package com.codex.repos

import com.codex.models.FilterUserRequest
import com.codex.models.User
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.experimental.filters.Filters
import org.bson.types.ObjectId

class UserService(private val dataStore: Datastore) : UserDAO {

    override fun create(user: User): User? = dataStore.withTransaction {
        dataStore.save(user)
    }


    override fun get(id: ObjectId): User? = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .filter(Filters.eq("id", id))
            .first()
    }

    override fun exists(id: ObjectId): Boolean {
        return dataStore.find(User::class.java)
            .filter(Filters.eq("id", id))
            .any()
    }

    override fun update(updateUser: User): User = dataStore.withTransaction {
        dataStore.merge(updateUser, InsertOneOptions().unsetMissing(false))
    }

    override fun list(page: Int?, size: Int?): List<User> = dataStore.withTransaction {
        val startIndex = page ?: 1
        val endIndex = size ?: 10
        dataStore.find(User::class.java)
            .iterator(FindOptions().skip((startIndex - 1) * endIndex).limit(endIndex).sort(Sort.descending("dob")))
            .toList()
    }

    override fun filter(filterUserRequest: FilterUserRequest): List<User> = dataStore.withTransaction {
        dataStore.find(User::class.java).apply {
            filterUserRequest.fName?.let { filter(Filters.eq("fName", it)) }
            filterUserRequest.mName?.let { filter(Filters.eq("mName", it)) }
            filterUserRequest.lName?.let { filter(Filters.eq("lName", it)) }
            filterUserRequest.placeOfBirth?.let { filter(Filters.eq("pob", it)) }
            filterUserRequest.homeTown?.let { filter(Filters.eq("homeTown", it)) }
            filterUserRequest.dob?.let { filter(Filters.eq("dob", it)) }

            iterator(
                FindOptions().skip(filterUserRequest.page).limit(filterUserRequest.size)
                    .sort(Sort.descending("dob"))
            )
        }.toList()
    }

    override fun delete(id: ObjectId): Long = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .filter(Filters.eq("id", id))
            .delete(DeleteOptions()).deletedCount
    }

    override fun deleteAll(): Long = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .delete(DeleteOptions().multi(true)).deletedCount
    }

}