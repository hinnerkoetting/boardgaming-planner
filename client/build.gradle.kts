plugins {
    id("com.github.node-gradle.node") version ("7.1.0")
}

node {
    download = true
    version = "24.22.1"
    npmVersion = "11.19.1"
}

val buildTask = tasks.getByName("npm_run_build") {
    inputs.dir("src")
    inputs.files("package.json", "tsconfig.json", "vite.config.ts")
    outputs.dir("dist")
}

val clientOutput = configurations.create("clientOutput") {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(clientOutput.name, tasks.named("npm_run_build"))
}