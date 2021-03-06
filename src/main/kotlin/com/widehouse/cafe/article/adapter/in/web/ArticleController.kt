package com.widehouse.cafe.article.adapter.`in`.web

import com.widehouse.cafe.article.Article
import com.widehouse.cafe.article.adapter.`in`.web.dto.ArticleDto
import com.widehouse.cafe.article.application.ArticleService
import com.widehouse.cafe.common.exception.DataNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
class ArticleController(private val articleService: ArticleService) {
    @GetMapping("article/{articleId}")
    fun get(@PathVariable articleId: String) = articleService.getArticle(articleId)
        .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)) }

    @GetMapping("article", params = ["boardId"])
    fun listByBoard(@RequestParam boardId: String): Flux<Article> = articleService.listByBoard(boardId)

    @GetMapping("article", params = ["cafeId"])
    fun listByCafe(@RequestParam cafeId: String): Flux<Article> = articleService.listByCafe(cafeId)

    @PostMapping("article")
    fun create(@RequestBody articleDto: ArticleDto) = articleService.create(articleDto)
        .map { mapOf("id" to it.id) }

    @PutMapping("article")
    fun update(@RequestBody articleDto: ArticleDto) = articleService.update(articleDto)
        .map { mapOf("id" to it.id) }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler
    fun handle(ex: DataNotFoundException) { }
}
