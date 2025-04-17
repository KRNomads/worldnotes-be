plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation (project(":auth-service"))
    implementation (project(":user-service"))
    implementation (project(":note-service"))
    implementation (project(":common"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass.set("org.example.App")
}