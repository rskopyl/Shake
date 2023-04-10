package com.rskopyl.shake.repository.impl.mapper

interface Mapper<A, B> {

    fun from(entity: A): B

    fun from(entities: Iterable<A>): List<B> =
        entities.map(::from)
}

interface BiMapper<A, B> : Mapper<A, B> {

    fun to(entity: B): A

    fun to(entities: Collection<B>): List<A> =
        entities.map(::to)
}