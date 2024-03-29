package com.widehouse.cafe.cafe.adapter.out.persisitence

import com.widehouse.cafe.cafe.domain.Cafe
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface CafeMongoRepository : ReactiveMongoRepository<Cafe, String> {
    fun findByCategoryId(categoryId: Long): Flux<Cafe>
}
