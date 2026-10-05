@file:Suppress("VulnerableLibrariesLocal")

import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    idea
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.run.paper)
}

group = project.properties["group"]!!

repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
        name = "sonatype"
    }
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://jitpack.io") {
        name = "JitPack"
    }
    maven("https://repo.xenondevs.xyz/releases") {
        name = "invui"
    }
    maven("https://repo.codemc.org/repository/maven-public/") {
        name = "codemc"
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        name = "placeholderapi"
    }
    maven("https://repo.minebench.de/") {
        name = "minebench"
    }
    maven("https://repo.alessiodp.com/releases/") {
        name = "alessiodp"
    }
}

val rebarVersion = libs.versions.rebar.get()
val pylonVersion = libs.versions.pylon.get()

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.rebar)
    compileOnly(libs.pylon)
    implementation(libs.libby.bukkit)
    compileOnly(libs.bundles.javet)
    compileOnly(libs.httpclient)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    compileOnly(libs.byte.buddy)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.shadowJar {
    mergeServiceFiles()

    exclude("kotlin/**")
    exclude("org/intellij/lang/annotations/**")
    exclude("org/jetbrains/annotations/**")

    relocate("net.byteflux.libby", "${project.group}.${project.name}.libraries.net.byteflux.libby")

    archiveBaseName = project.name
    archiveClassifier = null
}

bukkit {
    name = project.properties["name"] as String
    main = project.properties["main-class"] as String
    version = project.version.toString()
    apiVersion = "1.21"
    depend = listOf("Rebar")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf(
        "Pylon"
    )
}

tasks.runServer {
    downloadPlugins {
        github("pylonmc", "rebar", rebarVersion, "rebar-$rebarVersion.jar")
        github("pylonmc", "pylon", pylonVersion, "pylon-$pylonVersion.jar")
    }
    maxHeapSize = "4G"
    minecraftVersion("1.21.10")
}
