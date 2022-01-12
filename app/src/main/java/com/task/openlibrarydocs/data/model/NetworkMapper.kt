package com.task.openlibrarydocs.data.model

import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.data.model.response.NetworkDoc
import com.task.openlibrarydocs.utils.EntityMapper
import javax.inject.Inject

/**
 * This class is basically used to map the response from the network entity to the domain entity
 */
class NetworkMapper @Inject constructor() : EntityMapper<NetworkDoc, Document> {

    override fun mapFromEntity(entity: NetworkDoc): Document {
        return Document(
            title = entity.title,
            isbn = entity.isbn,
            authorName = entity.authorName,
        )
    }

    override fun mapToEntity(domainEntity: Document): NetworkDoc {
        return NetworkDoc(
            title = domainEntity.title,
            isbn = domainEntity.isbn,
            authorName = domainEntity.authorName,
        )
    }

    fun mapFromEntityList(entities: List<NetworkDoc>): List<Document> {
        return entities.map { mapFromEntity(it) }
    }
}