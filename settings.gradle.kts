rootProject.name = "microservice-example"

files("common", "consul1").forEach { dir ->
    dir.listFiles()?.forEach {
        if (it.isDirectory) {
            include(it.name)
            project(":${it.name}").projectDir = it
        }
    }
}

//include("gateway")
//include("security")