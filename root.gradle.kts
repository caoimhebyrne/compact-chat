// https://github.com/EssentialGG/essential-gradle-toolkit
plugins {
    id("gg.essential.loom") version "1.9.32" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val fabric12101 = createNode("1.21.1-fabric", 12101, "yarn")
    val neoforge12101 = createNode("1.21.1-neoforge", 12101, "official")

    neoforge12101.link(fabric12101)    // Fabric 1.21.1    ->  NeoForge 1.21.1
}
