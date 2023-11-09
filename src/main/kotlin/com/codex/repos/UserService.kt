package com.codex.repos

import com.codex.models.FilterUserRequest
import com.codex.models.PaginationModel
import com.codex.models.User
import com.codex.models.toPaginationModel
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.Sort
import dev.morphia.query.experimental.filters.Filter
import dev.morphia.query.experimental.filters.Filters
import org.bson.types.ObjectId

class UserService(private val dataStore: Datastore) : UserRepo {


    override fun create(user: User): User? = dataStore.withTransaction {
        dataStore.save(user)
    }

    override fun get(email: String): User? = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .filter(Filters.eq("email", email))
            .first()
    }

    override fun get(id: ObjectId): User? = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .filter(Filters.eq("id", id))
            .first()
    }

    override fun exists(id: ObjectId): Boolean = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .filter(Filters.eq("id", id))
            .any()
    }

    override fun update(updateUser: User): User = dataStore.withTransaction {
        dataStore.merge(updateUser, InsertOneOptions().unsetMissing(false))
    }

    override fun list(page: Int, size: Int): PaginationModel<List<User>> = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .iterator(FindOptions().skip((page - 1)).limit(size))
            .toList().toPaginationModel(page, size)
    }

    override fun filter(filterUserRequest: FilterUserRequest): PaginationModel<List<User>> =
        dataStore.withTransaction { _ ->
            val query: Query<User> = dataStore.find(User::class.java)
            val filters = mutableListOf<Filter>()

            filterUserRequest.firstName?.let { filters.add(Filters.regex("fName").pattern(it).caseInsensitive()) }
            filterUserRequest.middleName?.let { filters.add(Filters.regex("mName").pattern(it).caseInsensitive()) }
            filterUserRequest.lastName?.let { filters.add(Filters.regex("lName").pattern(it).caseInsensitive()) }
            filterUserRequest.status?.let { filters.add(Filters.eq("status", it)) }
            filterUserRequest.gender?.let { filters.add(Filters.eq("gender", it)) }
            filterUserRequest.placeOfBirth?.let { filters.add(Filters.eq("pob", it)) }
            filterUserRequest.homeTown?.let { filters.add(Filters.eq("homeTown", it)) }
            filterUserRequest.dateOfBirth?.let { filters.add(Filters.eq("dob", it)) }

            query.filter(Filters.and(*filters.toTypedArray())).iterator(
                FindOptions().skip((filterUserRequest.page - 1)).limit(filterUserRequest.size)
                    .sort(Sort.descending("dob"))
            ).toList().toPaginationModel(filterUserRequest.page, filterUserRequest.size)
        }

    override fun count(): Long = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .count()
    }

    override fun delete(id: ObjectId): User? = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .filter(Filters.eq("id", id))
            .findAndDelete()
    }

    override fun deleteAll(): Long = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .delete(DeleteOptions().multi(true)).deletedCount
    }

}