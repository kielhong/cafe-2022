package com.widehouse.cafe.cafe.adapter.out.persisitence

import com.widehouse.cafe.cafe.CafeFixtures
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class CafePersistenceAdapterTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val cafeMongoRepository = mockk<CafeMongoRepository>()
    val adapter = CafePersistenceAdapter(cafeMongoRepository)

    describe("CafePersistenceAdapter load cafe of id") {
        context("repository returns cafe") {
            val cafe = CafeFixtures.create()
            val cafeId = cafe.id
            every { cafeMongoRepository.findById(cafeId) } returns Mono.just(cafe)

            it("should be cafe") {
                adapter.loadCafe(cafeId)
                    .`as`(StepVerifier::create)
                    .assertNext { it shouldBe cafe }
                    .verifyComplete()
            }
        }
    }

    describe("CafePersistenceAdapter load cafe by theme") {
        context("repository findByTheme returns cafes") {
            val theme = "theme"
            val cafe1 = CafeFixtures.create("1")
            val cafe2 = CafeFixtures.create("2")
            every { cafeMongoRepository.findByTheme(theme) } returns Flux.just(cafe1, cafe2)

            it("should be list cafe of theme") {
                adapter.loadCafeByTheme(theme)
                    .`as`(StepVerifier::create)
                    .assertNext { it shouldBe cafe1 }
                    .assertNext { it shouldBe cafe2 }
                    .verifyComplete()
            }
        }
    }

    describe("CafePersistenceAdapter create cafe") {
        context("repository insert and returns cafes") {
            val cafe = CafeFixtures.create()
            every { cafeMongoRepository.insert(cafe) } returns Mono.just(cafe)

            it("should be cafe") {
                adapter.createCafe(cafe)
                    .`as`(StepVerifier::create)
                    .assertNext { it shouldBe cafe }
                    .verifyComplete()
            }
        }
    }
})
