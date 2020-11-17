rootProject.name = "microservice-example"

files("netflix", "consul").forEach { dir ->
    dir.listFiles().forEach {
        if (it.isDirectory) {
            include(it.name)
            project(":${it.name}").projectDir = it
        }
    }
}