package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.article.repository.ArticleRepository
import com.widehouse.cafe.repository.BoardRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.Collections

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val boardRepository: BoardRepository
) {
    fun getArticle(articleId: String) = articleRepository.findById(articleId)

    fun listArticleByBoard(boardId: String): Flux<Article> = articleRepository.findByBoardId(boardId)

    fun listArticleByCafe(cafeId: String): Flux<Article> {
        val boardIds = boardRepository.findByCafeId(cafeId)
            .map { it.id }
            .collectList()
            .blockOptional()
            .orElse(Collections.emptyList())
            .toList()

        return articleRepository.findByBoardIdIn(boardIds)
    }
}