
import com.flixclusive.model.provider.Language
import com.flixclusive.model.provider.ProviderType
import com.flixclusive.model.provider.Status
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.internal.logging.progress.ProgressLogger
import org.gradle.internal.logging.progress.ProgressLoggerFactory
import org.gradle.internal.service.ServiceRegistry
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileOutputStream
import java.net.URL

val csJarFolder =
    gradle.gradleUserHomeDir
        .resolve("caches")
        .resolve("cloudstream")
val csJar = csJarFolder.resolve("cloudstream.jar")

dependencies {
    implementation("androidx.core:core:1.15.0")

    // Comment if not implementing own SettingsScreen
    // No need to specify the compose version explicitly
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.runtime:runtime")
    // ================= END: COMPOSE UI =================

    fatImplementation(files(csJar))
    fatImplementation("com.github.Blatzar:NiceHttp:0.4.11") // http library
    fatImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    fatImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    fatImplementation("io.karn:khttp-android:0.1.2")
    fatImplementation("com.uwetrottmann.tmdb2:tmdb-java:2.11.0")
    fatImplementation("me.xdrop:fuzzywuzzy:1.4.0")
    fatImplementation("org.mozilla:rhino:1.7.15")

    testImplementation("com.github.Blatzar:NiceHttp:0.4.11") // http library
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    testImplementation("io.karn:khttp-android:0.1.2")
    testImplementation("com.faendir.rhino:rhino-android:1.6.0")
    testImplementation("com.uwetrottmann.tmdb2:tmdb-java:2.11.0")
    testImplementation(files(csJar))
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "TMDB_API", "\"${properties.getProperty("TMDB_API")}\"")
        buildConfigField("String", "GHOSTX_API", "\"${properties.getProperty("GHOSTX_API")}\"")
        buildConfigField("String", "CINEMATV_API", "\"${properties.getProperty("CINEMATV_API")}\"")
        buildConfigField("String", "SFMOVIES_API", "\"${properties.getProperty("SFMOVIES_API")}\"")
        buildConfigField("String", "ZSHOW_API", "\"${properties.getProperty("ZSHOW_API")}\"")
        buildConfigField("String", "DUMP_API", "\"${properties.getProperty("DUMP_API")}\"")
        buildConfigField("String", "DUMP_KEY", "\"${properties.getProperty("DUMP_KEY")}\"")
        buildConfigField("String", "CRUNCHYROLL_BASIC_TOKEN", "\"${properties.getProperty("CRUNCHYROLL_BASIC_TOKEN")}\"")
        buildConfigField("String", "CRUNCHYROLL_REFRESH_TOKEN", "\"${properties.getProperty("CRUNCHYROLL_REFRESH_TOKEN")}\"")
        buildConfigField("String", "MOVIE_API", "\"${properties.getProperty("MOVIE_API")}\"")
        buildConfigField("String", "MultiMovies_API", "\"${properties.getProperty("MultiMovies_API")}\"")
        buildConfigField("String", "MovieDrive_API", "\"${properties.getProperty("MovieDrive_API")}\"")
        buildConfigField("String", "AsianDrama_API", "\"${properties.getProperty("AsianDrama_API")}\"")
    }
}

flxProvider {
    description = """This is an experimental attempt for a CloudStream adapter."""

    changelog =
        """
        Experimental
        """.trimIndent()

    versionMajor = 0
    versionMinor = 0
    versionPatch = 1
    versionBuild = 0

    iconUrl = "https://i.imgur.com/g4FxRfl.png"

    language = Language.Multiple

    providerType = ProviderType.All

    status = Status.Beta
}

tasks.register("getCSJar") {
    doFirst {
        logger.lifecycle("Fetching Cloudstream JAR")

        val tag = "pre-release"
        val apkDownloadUrl = URL("https://github.com/recloudstream/cloudstream/releases/download/$tag/classes.jar")

        if (!csJarFolder.exists()) {
            csJarFolder.mkdirs()
        }

        if (!csJar.exists()) {
            apkDownloadUrl.download(
                file = csJar,
                progressLogger =
                    createProgressLogger(
                        project = project,
                        loggerCategory = "cloudstream",
                    ),
            )
        }
    }
}

fun createProgressLogger(
    project: Project,
    loggerCategory: String,
): ProgressLogger = createProgressLogger((project as ProjectInternal).services, loggerCategory)

fun createProgressLogger(
    services: ServiceRegistry,
    loggerCategory: String,
): ProgressLogger {
    val progressLoggerFactory = services.get(ProgressLoggerFactory::class.java)
    return progressLoggerFactory.newOperation(loggerCategory).also { it.description = loggerCategory }
}

fun URL.download(
    file: File,
    progressLogger: ProgressLogger,
) {
    progressLogger.started()
    try {
        val tempFile = File.createTempFile(file.name, ".part", file.parentFile)
        tempFile.deleteOnExit()

        val connection = this.openConnection()
        val size = connection.contentLengthLong
        val sizeText = toLengthText(size)

        connection.getInputStream().use { inputStream ->
            var finished = false
            var processedBytes: Long = 0
            try {
                FileOutputStream(tempFile).use { os ->
                    val buf = ByteArray(1024 * 10)
                    var read: Int
                    while (inputStream.read(buf).also { read = it } >= 0) {
                        os.write(buf, 0, read)
                        processedBytes += read
                        progressLogger.progress("Downloading ${toLengthText(processedBytes)}/$sizeText")
                    }
                    os.flush()
                    finished = true
                }
            } finally {
                if (finished) {
                    tempFile.renameTo(file)
                } else {
                    tempFile.delete()
                }
            }
        }
    } finally {
        progressLogger.completed()
    }
}

fun toLengthText(bytes: Long): String =
    if (bytes < 1024) {
        "$bytes B"
    } else if (bytes < 1024 * 1024) {
        (bytes / 1024).toString() + " KB"
    } else if (bytes < 1024 * 1024 * 1024) {
        String.format("%.2f MB", bytes / (1024.0 * 1024.0))
    } else {
        String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
    }
