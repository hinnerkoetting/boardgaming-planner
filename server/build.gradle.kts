plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

val clientOutput by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

group = "de.oetting"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    clientOutput(project(":client", "clientOutput"))

    implementation("com.github.marcioos:bgg-client:1.0")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.liquibase:liquibase-core")

    runtimeOnly("javax.xml.bind:jaxb-api:2.3.1") // JWT
    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName("bootJar") {
    dependsOn("copy_client_resources")
}

tasks.register<Copy>("copy_client_resources") {
    val sharedFiles: FileCollection = clientOutput
    from(sharedFiles)
    into("build/client/static")
}

tasks.processResources {
    dependsOn("copy_client_resources")
}

java.sourceSets["main"].resources {
    srcDir("build/client")
}
