plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

val clientOutput by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val h2Database by configurations.creating {
    isCanBeConsumed = true
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
    h2Database("com.h2database:h2:2.3.232")

    implementation("org.audux.bgg:bggclient:1.2.1") {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.jsonwebtoken:jjwt:0.13.0")
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

tasks.register<JavaExec>("h2_server_mode") {
    group = "Execution"
    description = "Run the h2-database with servermode"
    classpath(h2Database)
    mainClass = "org.h2.tools.Server"
    args("-tcp", "-tcpPort", "9092");
}
