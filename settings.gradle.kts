import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

if (!file(".git").exists()) {
    // Reactive start - project setup
    val errorText = """
        
        =====================[ ERROR ]=====================
         The Leaf project directory is not a properly cloned Git repository.
         
         In order to build Reactive from source you must clone
         the Reactive repository using Git, not download a code
         zip from GitHub.
         
         Built Reactive jars are available for download at
         https://github.com/rizer001/Reactive/releases
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper forks.
        ===================================================
    """.trimIndent()
    // Reactive end - project setup
    error(errorText)
}

rootProject.name = "reactive"

for (name in listOf("reactive-api", "reactive-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
}

gradle.lifecycle.beforeProject {
    val mcVersion = providers.gradleProperty("mcVersion").get().trim()
    val paperVersionChannel = providers.gradleProperty("channel").get().trim()
    val paperBuildNumber = providers.environmentVariable("BUILD_NUMBER").orNull?.trim()?.toInt()
    val versionString = if (paperBuildNumber == null) {
        "$mcVersion.local-SNAPSHOT"
    } else {
        "$mcVersion.build.$paperBuildNumber-${paperVersionChannel.lowercase()}"
    }
    version = versionString
}
