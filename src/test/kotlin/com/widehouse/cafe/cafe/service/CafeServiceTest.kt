package com.widehouse.cafe.cafe.service

import com.widehouse.cafe.cafe.CafeFixtures
import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.repository.CafeRepository
import com.widehouse.cafe.common.exception.AlreadyExistException
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
internal class CafeServiceTest {
    lateinit var service: CafeService
    @Mock
    lateinit var cafeRepository: CafeRepository

    @BeforeEach
    internal fun setUp() {
        service = CafeService(cafeRepository)
    }

    @Test
    fun given_repository_when_getCafe_then_returnMonoCafe() {
        // given
        val cafe = CafeFixtures.create()
        given(cafeRepository.findById(cafe.id)).willReturn(Mono.just(cafe))
        // when
        val result = service.getCafe(cafe.id)
        // then
        StepVerifier.create(result)
            .assertNext { then(it).isEqualTo(cafe) }
            .verifyComplete()
    }

    @Nested
    inner class CreateCafe {
        val cafe = CafeFixtures.create()

        @Test
        fun given_cafe_when_create_then_SaveCafe() {
            // given
            given(cafeRepository.insert(cafe)).willReturn(Mono.just(cafe))
            // when
            val result = service.create(cafe)
            // then
            StepVerifier.create(result)
                .assertNext { then(it).isEqualTo(cafe) }
                .verifyComplete()
        }

        @Test
        fun given_ExistCafeId_when_create_then_throwDuplicateKeyException() {
            // given
            given(cafeRepository.insert(any(Cafe::class.java))).willReturn(Mono.error(DuplicateKeyException("")))
            // when
            val result = service.create(cafe)
            // then
            StepVerifier.create(result)
                .expectError(AlreadyExistException::class.java)
                .verify()
        }
    }
}
