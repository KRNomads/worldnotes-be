dependencies {
    implementation (project(":user-service"))
    implementation (project(":common"))
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

application {
    mainClass.set("org.example.NoteServiceApplication")
}