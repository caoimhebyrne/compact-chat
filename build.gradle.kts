object Constants {
    const val MINECRAFT_VERSION = "1.20.4"
    const val YARN_VERSION = "1.20.4+build.3"
    const val LOADER_VERSION = "0.15.6"

    const val MOD_MENU_VERSION = "9.0.0"
    const val CLOTH_CONFIG_VERSION = "13.0.121"
}

plugins {
    id("fabric-loom") version "1.5-SNAPSHOT"
}

group = "dev.caoimhe.compactchat"
version = "2.1.0"
base.archivesName = "compact-chat"

loom {
    runs {
        // Compact Chat is a client-sided only mod, it won't do anything on the server.
        remove(getByName("server"))
    }
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    minecraft("com.mojang:minecraft:${Constants.MINECRAFT_VERSION}")
    mappings("net.fabricmc:yarn:${Constants.YARN_VERSION}:v2")
    modImplementation("net.fabricmc:fabric-loader:${Constants.LOADER_VERSION}")

    modApi("com.terraformersmc:modmenu:${Constants.MOD_MENU_VERSION}")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${Constants.CLOTH_CONFIG_VERSION}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName}" }
        }
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }
}
