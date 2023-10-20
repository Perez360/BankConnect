package com.codex.repos

import com.codex.models.FilterUserRequest
import com.codex.models.PaginationModel
import com.codex.models.User
import com.codex.util.JacksonUtils
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.Sort
import dev.morphia.query.experimental.filters.Filters
import org.bson.types.ObjectId
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import kotlin.math.ceil

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
        dataStore.merge(updateUser, InsertOneOptions().unsetMissing(true))
    }

    override fun list(page: Int?, size: Int?): PaginationModel<List<User>> = dataStore.withTransaction {
        val startIndex = page ?: 1
        val endIndex = size ?: 10


        val query:Query<User> = dataStore.find(User::class.java)
//        val totalItems



//            .iterator(FindOptions().skip(startIndex).limit((startIndex - 1) * endIndex).sort(Sort.descending("dob")))
//            .toList()

        val totalPages: Int = ceil(co / endIndex.toDouble()).toInt()
        PaginationModel.from(startIndex, endIndex, totalPages, totalItems, listOfUsers)
    }

    override fun filter(filterUserRequest: FilterUserRequest): PaginationModel<List<User>> = dataStore.withTransaction {
        val query: Query<User> = dataStore.find(User::class.java)

        query.apply {
            filterUserRequest.firstName?.let { filter(Filters.eq("fName", it)) }
            filterUserRequest.middleName?.let { filter(Filters.eq("mName", it)) }
            filterUserRequest.status?.let { filter(Filters.eq("status", it)) }
            filterUserRequest.lastName?.let { filter(Filters.eq("lName", it)) }
            filterUserRequest.gender?.let { filter(Filters.eq("gender", it)) }
            filterUserRequest.placeOfBirth?.let { filter(Filters.eq("pob", it)) }
            filterUserRequest.homeTown?.let { filter(Filters.eq("homeTown", it)) }
            filterUserRequest.dateOfBirth?.let { filter(Filters.eq("dob", it)) }

            iterator(
                FindOptions().skip(filterUserRequest.page).limit((filterUserRequest.page - 1) * filterUserRequest.size)
                    .sort(Sort.descending("dob"))
            )
        }

        val totalElements = query.count()
        val totalPages: Int = ceil(totalElements / filterUserRequest.size.toDouble()).toInt()

        PaginationModel.from(
            page = filterUserRequest.page,
            size = filterUserRequest.size,
            totalPages = totalPages,
            totalElements = totalElements,
            query.toList()
        )
    }

    override fun count(): Long = dataStore.withTransaction {
        dataStore.find(User::class.java)
            .count()
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