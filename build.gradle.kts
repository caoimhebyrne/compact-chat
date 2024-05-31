object Constants {
    const val MINECRAFT_VERSION = "1.20.6"
    const val YARN_VERSION = "1.20.6+build.3"
    const val LOADER_VERSION = "0.15.11"

    const val FABRIC_API_VERSION = "0.99.4+1.20.6"
    const val MOD_MENU_VERSION = "10.0.0-beta.1"
    const val CLOTH_CONFIG_VERSION = "14.0.126"
}

plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
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

    // This is not required by Compact Chat, but it is required by Mod Menu.
    // We don't want to force people to have it, but we need Mod Menu in the development environment,
    // so we'll just include it for that.
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${Constants.FABRIC_API_VERSION}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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
