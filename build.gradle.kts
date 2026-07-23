import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `maven-publish`
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.21"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"
val reactiveMavenPublicUrl = "https://maven.leafmc.one/snapshots/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
        maven(reactiveMavenPublicUrl)
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
        options.isFork = true
        options.compilerArgs.addAll(listOf("-Xlint:-deprecation", "-Xlint:-removal"))
    }
    tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources>().configureEach {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test>().configureEach {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven(reactiveMavenPublicUrl) {
                name = "reactive"

                credentials.username = System.getenv("REPO_USER")
                credentials.password = System.getenv("REPO_PASSWORD")
            }
        }
    }
}

paperweight {
    upstreams.paper {
        ref = providers.gradleProperty("paperCommit")

        patchFile {
            path = "paper-server/build.gradle.kts"
            outputFile = file("reactive-server/build.gradle.kts")
            patchFile = file("reactive-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "paper-api/build.gradle.kts"
            outputFile = file("reactive-api/build.gradle.kts")
            patchFile = file("reactive-api/build.gradle.kts.patch")
        }
        patchDir("paperApi") {
            upstreamPath = "paper-api"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("reactive-api/paper-patches")
            outputDir = file("paper-api")
        }
    }
}

/**
 * Copies the built Paperclip jar from reactive-server/build/libs/ to Jar/
 * with a SHA-256 checksum sidecar so users can verify downloads from GitHub.
 *
 * Usage:
 *   ./gradlew copyServerJar
 *
 * Output:
 *   Jar/Reactive-paperclip.jar
 *   Jar/Reactive-paperclip.jar.sha256
 *
 * The output jar and its checksum are force-tracked via .gitignore so they
 * can be downloaded directly from the GitHub repository (web UI / raw URL).
 */
val copyServerJar: Copy = tasks.register<Copy>("copyServerJar") {
    group = "build"
    description = "Copies the Reactive Paperclip jar to Jar/ for GitHub distribution"
    dependsOn(":reactive-server:createPaperclipJar")

    val outDir = layout.projectDirectory.dir("Jar")

    from(file("reactive-server/build/libs")) {
        include("paperclip*.jar")
    }
    into(outDir)
    rename { fileName ->
        // paperclip.jar / paperclip-1.21.4.jar -> Reactive-paperclip.jar
        Regex("^paperclip.*\\.jar$").replace(fileName, "Reactive-paperclip.jar")
    }

    doLast {
        val jar = outDir.file("Reactive-paperclip.jar").asFile
        if (!jar.exists()) {
            val src = file("reactive-server/build/libs")
            val found = (src.listFiles() ?: emptyArray()).map { it.name }.joinToString()
            throw GradleException(
                "Reactive-paperclip.jar was not produced by copyServerJar.\n" +
                "Expected at: ${jar.absolutePath}\n" +
                "Source dir contents (${src.absolutePath}): $found\n" +
                "Check that :reactive-server:createPaperclipJar ran successfully."
            )
        }
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val jarBytes = jar.readBytes()
        val hash = digest.digest(jarBytes).joinToString("") { "%02x".format(it) }

        // Atomic write via temp + Files.move(ATOMIC_MOVE) so a crash mid-write
        // never leaves a truncated checksum that would fail sha256sum -c downstream.
        val shaFile = outDir.file("Reactive-paperclip.jar.sha256").asFile
        val tmpSha = File(shaFile.parentFile, shaFile.name + ".tmp")
        try {
            tmpSha.writeText("$hash  Reactive-paperclip.jar\n", Charsets.UTF_8)
            java.nio.file.Files.move(
                tmpSha.toPath(),
                shaFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE
            )
        } catch (e: Exception) {
            tmpSha.delete()
            throw e
        }

        logger.lifecycle("✓ Copied ${jar.absolutePath} (${jarBytes.size / 1024 / 1024} MB)")
        logger.lifecycle("✓ SHA-256: $hash")
        logger.lifecycle("  See Jar/README.md for download/verification instructions")
    }
}

/**
 * Companion task to cleanServerJar — removes the prebuilt jar from Jar/.
 * Symmetric with `./gradlew clean` (which removes `build/`).
 */
val cleanJar: Delete = tasks.register<Delete>("cleanJar") {
    group = "build"
    description = "Removes Jar/Reactive-paperclip.jar and its .sha256 sidecar (plus any stale .tmp)"
    delete(
        file("Jar/Reactive-paperclip.jar"),
        file("Jar/Reactive-paperclip.jar.sha256"),
        file("Jar/Reactive-paperclip.jar.sha256.tmp")
    )
}
