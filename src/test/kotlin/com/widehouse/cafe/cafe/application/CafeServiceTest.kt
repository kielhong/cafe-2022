package com.widehouse.cafe.cafe.application

import com.widehouse.cafe.cafe.CafeFixtures
import com.widehouse.cafe.cafe.application.port.out.CafeRepository
import com.widehouse.cafe.common.exception.AlreadyExistException
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class CafeServiceTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val cafeRepository = mockk<CafeRepository>()
    val service = CafeService(cafeRepository)

    describe("getCafe") {
        context("cafeRepository load cafe of url") {
            val cafe = CafeFixtures.create("1")
            every { cafeRepository.loadCafe(any()) } returns Mono.just(cafe)
            it("should be cafe of url") {
                service.getCafe("1")
                    .`as`(StepVerifier::create)
                    .assertNext { it shouldBe cafe }
                    .verifyComplete()
                verify { cafeRepository.loadCafe(any()) }
            }
        }
    }

    describe("Cafe list by theme") {
        context("cafeRepository listByTheme listFluxCafe") {
            val theme = "movie"
            val cafe1 = CafeFixtures.create("1", "name1", "desc1", theme)
            val cafe2 = CafeFixtures.create("2", "name2", "desc2", theme)
            every { cafeRepository.loadCafeByTheme(any()) } returns Flux.just(cafe1, cafe2)
            it("should be flux of cafes") {
                service.listByTheme(theme)
                    .`as`(StepVerifier::create)
                    .assertNext { it shouldBe cafe1 }
                    .assertNext { it shouldBe cafe2 }
                    .verifyComplete()
            }
        }
    }

    describe("create Cafe") {
        val cafe = CafeFixtures.create("1")
        context("cafeRepository create and save cafe") {
            every { cafeRepository.createCafe(any()) } returns Mono.just(cafe)
            it("should be cafe of url") {
                service.create(cafe)
                    .`as`(StepVerifier::create)
                    .assertNext { it shouldBe cafe }
                    .verifyComplete()
                verify { cafeRepository.createCafe(any()) }
            }
        }

        context("cafeRepository throwDuplicateKeyException") {
            every { cafeRepository.createCafe(cafe) } returns Mono.error(DuplicateKeyException(""))
            it("should throws AlreadyExistException") {
                service.create(cafe)
                    .`as`(StepVerifier::create)
                    .expectError(AlreadyExistException::class.java)
                    .verify()
                verify { cafeRepository.createCafe(any()) }
            }
        }
    }
})
