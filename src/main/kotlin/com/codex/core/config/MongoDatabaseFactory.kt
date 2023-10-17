package com.codex.core.config

import com.codex.core.config.Configuration.getSystemProperties
import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia


object MongoDatabaseFactory {
    private lateinit var datastore: Datastore
    fun connect() {
        val connectionString = getSystemProperties().getProperty("datasource.database.url")
        val dbName = getSystemProperties().getProperty("datasource.database.name")
        datastore = if (connectionString.isNullOrEmpty()) {
            Morphia.createDatastore(dbName)
        } else {
            Morphia.createDatastore(MongoClients.create(connectionString), dbName)
        }
        prepareMappedPackages()
        datastore.ensureIndexes()
    }

    private fun prepareMappedPackages() {
        datastore.mapper.mapPackage("com.codex.models")
    }

    fun getDataStore(): Datastore {
        return datastore
    }
}
