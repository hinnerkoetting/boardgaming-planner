plugins {
    id("com.github.node-gradle.node") version ("7.1.0")
}

node {
    download = true
    version = "22.17.1"
    npmVersion = "10.9.2"
}

val buildTask = tasks.getByName("npm_run_build") {
    inputs.dir("src")
    inputs.files("package.json", "tsconfig.json", "vite.config.ts")
    outputs.dir("dist")
}

val clientOutput by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(clientOutput.name, tasks.named("npm_run_build"))
}