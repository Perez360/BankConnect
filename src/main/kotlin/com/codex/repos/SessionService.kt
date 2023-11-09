package com.codex.repos

import com.codex.dtos.Session
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.experimental.filters.Filters
import org.bson.types.ObjectId
import java.util.*

class SessionService(private val datastore: Datastore) : SessionRepo {

    override fun create(userId: ObjectId): Session = datastore.withTransaction {
        Session(
            id = ObjectId(Date()),
            userId = userId
        )
    }

    override fun getAll(): List<Session> = datastore.withTransaction {
        datastore.find(Session::class.java)
            .iterator(FindOptions().sort(Sort.descending("createdAt")))
            .toList()
    }

    override fun get(userId: ObjectId): Session = datastore.withTransaction {
        datastore.find(Session::class.java)
            .filter(Filters.eq("userId", userId))
            .first()
    }

    override fun destroy(userId: ObjectId): Boolean = datastore.withTransaction {
        datastore.find(Session::class.java)
            .filter(Filters.eq("userId", userId))
            .delete().wasAcknowledged()
    }
}