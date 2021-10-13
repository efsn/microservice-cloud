import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    `java-library`
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    id("org.springframework.boot") version "2.5.5"
}

repositories { jcenter() }

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(from = rootProject.file("gradle/ktlint.gradle.kts"))

    group = "cn.elmi.microservice"
    version = "0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("reflect"))
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        implementation(platform(SpringBootPlugin.BOM_COORDINATES))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2020.0.4"))
        implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.5.2"))
        implementation(platform("io.jsonwebtoken:jjwt-root:0.11.2"))
//        implementation(platform("org.keycloak.bom:keycloak-adapter-bom:11.0.2"))

        implementation("org.slf4j:slf4j-api")
        implementation("ch.qos.logback:logback-core")
        implementation("ch.qos.logback:logback-classic")

        developmentOnly("org.springframework.boot:spring-boot-devtools")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }

        test {
            failFast = true
            useJUnitPlatform()
            testLogging { events("passed", "skipped", "failed") }
            dependsOn(project.tasks.named("ktlint"))
        }

//        bootJar {
//            launchScript()
//        }
    }
}