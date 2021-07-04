package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.Article
import com.widehouse.cafe.article.controller.dto.ArticleDto
import com.widehouse.cafe.article.repository.ArticleRepository
import com.widehouse.cafe.article.repository.BoardRepository
import com.widehouse.cafe.common.exception.DataNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.Collections
import java.util.UUID

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val boardRepository: BoardRepository
) {
    fun getArticle(articleId: String): Mono<Article> = articleRepository.findById(articleId)

    fun listByBoard(boardId: String): Flux<Article> = articleRepository.findByBoardId(boardId)

    fun listByCafe(cafeId: String): Flux<Article> {
        val boardIds = boardRepository.findByCafeId(cafeId)
            .map { it.id }
            .collectList()
            .blockOptional()
            .orElse(Collections.emptyList())
            .toList()

        return articleRepository.findByBoardIdIn(boardIds)
    }

    fun create(articleDto: ArticleDto): Mono<Article> =
        articleRepository.save(Article(UUID.randomUUID().toString(), articleDto.boardId, articleDto.title, articleDto.body))

    fun update(articleDto: ArticleDto): Mono<Article> =
        articleRepository.findById(articleDto.id)
            .flatMap { article ->
                articleRepository.save(Article(article.id, articleDto.boardId, articleDto.title, articleDto.body))
            }
            .switchIfEmpty(Mono.error(DataNotFoundException("Article(id=${articleDto.id}) not found")))
}
