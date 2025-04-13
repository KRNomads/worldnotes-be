plugins {
    id("org.springframework.boot") version "3.4.2" 
    id("io.spring.dependency-management") version "1.1.4" 
    id("application")
    id("java")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "application")
    apply(plugin = "java") 

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web") // 웹 애플리케이션
        implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA
        implementation("org.springframework.boot:spring-boot-starter-security") // 시큐리티
        implementation("org.springframework.boot:spring-boot-starter-oauth2-client") // oauth2

        // Lombok
        implementation("org.projectlombok:lombok") 
        annotationProcessor("org.projectlombok:lombok")
        
        // JSON 처리
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("org.json:json:20240303")

        // PostgreSQL
        implementation("org.postgresql:postgresql")

        // JSONB
        implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.9")

        // SpringDoc OpenAPI
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

        // QueryDSL 설정
        implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
        implementation("com.querydsl:querydsl-core:5.0.0")
        annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")

        annotationProcessor("jakarta.persistence:jakarta.persistence-api:3.1.0")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api:2.1.1")

        // 테스트
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

application {
    mainClass.set("org.example.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}