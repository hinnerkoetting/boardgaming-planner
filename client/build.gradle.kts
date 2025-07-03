plugins {
  id("com.github.node-gradle.node") version("7.0.2")
}

node {    
    download = true
    version = "20.12.2"
    npmVersion = "10.8.3"
}

tasks.getByName("npm_run_build") {
    inputs.dir("src")
    inputs.files("package.json", "tsconfig.json", "vite.config.ts")
    outputs.dir("dist")
}