// https://github.com/EssentialGG/essential-gradle-toolkit
plugins {
    id("gg.essential.loom") version "1.9.32" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val neoforge260100 = createNode("26.1-neoforge", 260100, "official")
    val fabric260100 = createNode("26.1-fabric", 260100, "official")
    val neoforge12101 = createNode("1.21.1-neoforge", 12101, "official")
    val fabric12101 = createNode("1.21.1-fabric", 12101, "yarn")

    neoforge260100.link(fabric260100)  // NeoForge 26.1    -> Fabric 26.1
    fabric260100.link(neoforge12101, file("versions/26.1-fabric-1.21.1-neoforge.txt")) // Fabric 26.1 -> NeoForge 1.21.1
    neoforge12101.link(fabric12101)    // NeoForge 1.21.1 -> Fabric 1.21.1
}
