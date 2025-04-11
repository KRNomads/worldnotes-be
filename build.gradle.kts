plugins {
    id("org.springframework.boot") version "3.2.0" 
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

        // Lombok
        implementation("org.projectlombok:lombok") 
        annotationProcessor("org.projectlombok:lombok")
        
        // PostgreSQL
        implementation ("org.postgresql:postgresql")

        // QueryDSL 설정
        implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
        implementation("com.querydsl:querydsl-core:5.0.0")
        annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")

        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
        implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

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