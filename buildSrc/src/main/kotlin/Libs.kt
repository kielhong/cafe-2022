object Libs {
    object Plugins {
        const val spring = "org.springframework.boot"
        const val dependencyManagement = "io.spring.dependency-management"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val ktlintIdea = "org.jlleitschuh.gradle.ktlint-idea"
        const val jacocoBadge = "com.github.dawnwords.jacoco.badge"
    }

    object Versions {
        const val spring = "2.6.6"
        const val dependency = "1.0.11.RELEASE"
        const val kotlin = "1.6.10"
        const val kotest = "4.6.2"
        const val kotestExtension = "4.4.3"
        const val mockk = "1.12.0"
        const val ktlint = "10.2.0"
        const val jacoco = "0.8.7"
        const val jacocoBadge = "0.2.4"
        const val springMockk = "3.1.1"
    }

    object Test {
        const val springTest = "org.springframework.boot:spring-boot-starter-test"
        const val reactorTest = "io.projectreactor:reactor-test"
        const val kotest = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
        const val kotestAssertionsCore = "io.kotest:kotest-assertions-core:${Versions.kotest}"
        const val kotestProperty = "io.kotest:kotest-property:${Versions.kotest}"
        const val kotestExtensionsSpring = "io.kotest:kotest-extensions-spring:${Versions.kotestExtension}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val springMockk = "com.ninja-squad:springmockk:${Versions.springMockk}"
    }

    const val springBootDataMongoReactive = "org.springframework.boot:spring-boot-starter-data-mongodb-reactive"
    const val springBootValidation = "org.springframework.boot:spring-boot-starter-validation"
    const val springBootWebflux = "org.springframework.boot:spring-boot-starter-webflux"
    const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val reactorKotlin = "io.projectreactor.kotlin:reactor-kotlin-extensions"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val kotlinJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinCoroutineReactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor"
    const val embeddedMongo = "de.flapdoodle.embed:de.flapdoodle.embed.mongo"
}
