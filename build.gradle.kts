import kr.entree.spigradle.kotlin.spigot

val mcVersion = "1.16.3"

plugins {
    kotlin("jvm") version "1.4.10"
    id("kr.entree.spigradle") version "2.2.3"
}

group = "com.steve28.plugin" // 본인의 홈페이지 주소 또는 이메일 주소를 거꾸로 적음
version = "1.0.0"

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/") // 마인크래프트 관련 artifact들을 모아둔 repo
}

dependencies {
    implementation(kotlin("stdlib-jdk8")) // kotlin stdlib

    compileOnly(spigot(mcVersion)) // spigot api
}

spigot { // plugin.yml 설정
    authors = listOf("Steve28")
    commands {
        create("NoJump") {
            description = "If You Jump, U will die!"
            usage = "/NoJump || /NoJump [Boolean|PlayerName] || /NoJump [PlayerName] [Boolean]"
        }
    }
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        from(
                shade.map {
                    if (it.isDirectory)
                        it
                    else
                        zipTree(it)
                }
        )
    }
}