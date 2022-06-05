import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("java")
    id("fabric-loom")
    id("com.replaymod.preprocess")
}

group = project.property("maven_group")!!
version = project.property("mod_version")!!

val mcVersion: Int by extra
val minecraftVersion: String by project
val baseMinecraftVersion: String by project

preprocess {
    vars.put("MC", mcVersion)
}

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    val yarnMappings: String by project
    val loaderVersion: String by project
    val fabricVersion: String by project
    val modmenuVersion: String by project
    val clothConfigVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    modImplementation("com.terraformersmc:modmenu:$modmenuVersion") { exclude("net.fabricmc.fabric-api") }
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") { exclude("net.fabricmc.fabric-api") }
}

tasks {
    "remapJar"(RemapJarTask::class) {
        archiveVersion.set("")
        archiveBaseName.set("CompactChat-${project.version}-$minecraftVersion")
    }

    @Suppress("UnstableApiUsage")
    "processResources"(ProcessResources::class) {
        inputs.property("version", project.version)
        inputs.property("baseMinecraftVersion", baseMinecraftVersion)
        filesMatching(listOf("fabric.mod.json")) {
            expand(mapOf("version" to project.version, "baseMinecraftVersion" to baseMinecraftVersion))
        }
    }

    "compileJava"(JavaCompile::class) {
        options.release.set(17)
    }


    val copyArchives by registering(Copy::class) {
        from(remapJar.get().archiveFile)
        into(rootProject.buildDir.resolve("distributions"))
    }

    assemble.get().dependsOn(copyArchives)
}
