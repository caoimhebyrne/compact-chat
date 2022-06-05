plugins {
    id("com.replaymod.preprocess") version "0ab22d2"
    id("fabric-loom") version "0.12-SNAPSHOT" apply false
}

configurations.register("compileClasspath")

preprocess {
    "1.19"(11900, "yarn") {
        "1.18.2"(11802, "yarn")
    }
}
