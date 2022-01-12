package com.task.openlibrarydocs.utils

/**
 * A generic interface for mapping entities
 */
interface EntityMapper<Entity, DomainEntity> {

    fun mapFromEntity(entity: Entity): DomainEntity

    fun mapToEntity(domainEntity: DomainEntity): Entity

}