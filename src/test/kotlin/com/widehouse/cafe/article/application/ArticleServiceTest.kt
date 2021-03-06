package com.widehouse.cafe.article.application

import com.widehouse.cafe.article.Article
import com.widehouse.cafe.article.ArticleFixtures
import com.widehouse.cafe.article.adapter.`in`.web.dto.ArticleDto
import com.widehouse.cafe.article.adapter.out.persistence.ArticleRepository
import com.widehouse.cafe.article.adapter.out.persistence.BoardRepository
import com.widehouse.cafe.article.domain.Board
import com.widehouse.cafe.common.exception.DataNotFoundException
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
internal class ArticleServiceTest {
    private lateinit var service: ArticleService
    @Mock
    private lateinit var articleRepository: ArticleRepository
    @Mock
    private lateinit var boardRepository: BoardRepository

    private lateinit var boardId: String
    private lateinit var article1: Article
    private lateinit var article2: Article

    @BeforeEach
    internal fun setUp() {
        service = ArticleService(articleRepository, boardRepository)

        boardId = "board"
        article1 = Article("1234", listOf(boardId), "title1", "body1")
        article2 = Article("abcd", listOf(boardId), "title2", "body2")
    }

    @Test
    fun `articleId가 주어지면 해당하는 article을 한 개 반환`() {
        // given
        given(articleRepository.findById(anyString())).willReturn(Mono.just(article1))
        // when
        val result = service.getArticle(article1.id)
        // then
        StepVerifier.create(result)
            .assertNext { then(it).isEqualTo(article1) }
            .verifyComplete()
    }

    @Test
    fun `boardId 가 주어지면 boardId에 연결된 모든 article목록을 반환`() {
        // given
        given(articleRepository.findByBoards(anyString())).willReturn(Flux.just(article1, article2))
        // when
        val result = service.listByBoard(boardId)
        // then
        StepVerifier.create(result)
            .assertNext { then(it).isEqualTo(article1) }
            .assertNext { then(it).isEqualTo(article2) }
            .verifyComplete()
    }

    @Test
    fun `cafeId가 주어지면 cafeId에 연결된 모든 article목록을 반환`() {
        // given
        val cafeUrl = "url"
        given(boardRepository.findByCafeId(anyString())).willReturn(Flux.just(Board("1", cafeUrl), Board("2", cafeUrl)))
        given(articleRepository.findByBoardsIn(anyList())).willReturn(Flux.just(article1, article2))
        // when
        val result = service.listByCafe(cafeUrl)
        // then
        StepVerifier.create(result)
            .assertNext { then(it).isEqualTo(article1) }
            .assertNext { then(it).isEqualTo(article2) }
            .verifyComplete()
    }

    @Test
    fun `article 생성`() {
        // given
        val request = ArticleDto(boards = listOf("boardId"), title = "title", body = "body")
        val article = Article("id", request.boards, request.title, request.body)
        given(articleRepository.save(any(Article::class.java)))
            .willReturn(Mono.just(article))
        // when
        val result = service.create(request)
        // then
        StepVerifier.create(result)
            .assertNext { then(it).isEqualTo(article) }
            .verifyComplete()
    }

    @Nested
    @DisplayName("Article 변경")
    inner class UpdateArticle {
        private val article = ArticleFixtures.create()
        private val request = ArticleDto(article.id, listOf("newBoardId"), "newTitle", "newBody")

        @Test
        fun `존재하는 article이면 변경된 article 반환`() {
            // given
            val updatedArticle = Article(article.id, request.boards, request.title, request.body)
            given(articleRepository.findById(request.id)).willReturn(Mono.just(article))
            given(articleRepository.save(any(Article::class.java))).willReturn((Mono.just(updatedArticle)))
            // when
            val result = service.update(request)
            // then
            StepVerifier.create(result)
                .assertNext { then(it).isEqualTo(updatedArticle) }
                .verifyComplete()
        }

        @Test
        fun `존재하지 않는 article이면 DataNotFoundException 반환`() {
            // given
            given(articleRepository.findById(request.id)).willReturn(Mono.empty())
            // when
            val result = service.update(request)
            // then
            StepVerifier.create(result)
                .expectError(DataNotFoundException::class.java)
                .verify()
        }
    }
}
