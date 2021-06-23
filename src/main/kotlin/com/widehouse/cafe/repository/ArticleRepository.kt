package com.widehouse.cafe.repository

import com.widehouse.cafe.model.Article
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface ArticleRepository : ReactiveMongoRepository<Article, String> {
    fun findByBoardId(boardId: String): Flux<Article>

    fun findByBoardIdIn(boardIds: List<String>): Flux<Article>
}
