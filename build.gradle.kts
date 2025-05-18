plugins {
    `maven-publish`

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

group = "ru.killwolfvlad"
version = providers.gradleProperty("version").getOrElse("1.0-SNAPSHOT")

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.bundles.kotest)
}

tasks.check {
    dependsOn("installKotlinterPrePushHook")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/KillWolfVlad/expressions.kt")

            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                password = providers.gradleProperty("gpr.key").orNull
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.from(files("$rootDir/detekt.yaml"))
}
