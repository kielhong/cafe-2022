package com.widehouse.cafe.cafe.adapter.out.persisitence

import com.widehouse.cafe.cafe.application.port.out.CafeRepository
import com.widehouse.cafe.cafe.domain.Cafe
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class CafePersistenceAdapter(
    private val cafeMongoRepository: CafeMongoRepository
) : CafeRepository {
    override fun loadCafe(id: String): Mono<Cafe> =
        cafeMongoRepository.findById(id)

    override fun loadCafeByCategory(categoryId: Long): Flux<Cafe> =
        cafeMongoRepository.findByCategoryId(categoryId)

    override fun createCafe(cafe: Cafe): Mono<Cafe> =
        cafeMongoRepository.insert(cafe)

    override fun updateCafe(cafe: Cafe): Mono<Cafe> =
        cafeMongoRepository.save(cafe)

    override fun deleteCafe(id: String): Mono<Void> =
        cafeMongoRepository.deleteById(id)
}
