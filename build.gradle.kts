plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.3.1"
}

project.group = (findProperty("pluginGroup") as String? ?: "com.example")
project.version = (findProperty("pluginVersion") as String? ?: "1.0.0")
description = findProperty("pluginDescription") as String? ?: "A Hytale plugin template"

repositories {
    mavenLocal()
    mavenCentral()
}

// --- Hytale install detection (Linux) ---
val patchlineProp = (findProperty("patchline") as String?) ?: "release"
val hytaleHomeProp = findProperty("hytale_home") as String?

val hytaleHome = hytaleHomeProp ?: run {
    val flatpak = file(System.getProperty("user.home") + "/.var/app/com.hypixel.HytaleLauncher/data/Hytale")
    if (flatpak.exists()) flatpak.absolutePath else {
        val local = file(System.getProperty("user.home") + "/.local/share/Hytale")
        if (local.exists()) local.absolutePath else ""
    }
}

val hasHytaleInstall = hytaleHome.isNotBlank()


val hytaleServerJar = file("$hytaleHome/install/$patchlineProp/package/game/latest/Server/HytaleServer.jar")
val hasHytaleServerJar = hasHytaleInstall && hytaleServerJar.exists()

dependencies {
    // Hytale Server API (provided by server at runtime)
    if (hasHytaleServerJar) {
        implementation(files(hytaleServerJar))
    } else {
        logger.lifecycle("Hytale install not detected on this machine/environment; skipping Server API jar dependency. Set hytale_home in gradle.properties for local dev.")
    }


    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains:annotations:24.1.0")
    
    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Configure server testing

tasks {
    // Configure Java compilation
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    
    // Configure resource processing
    // Build-time token expansion for src/main/resources/manifest.json (e.g., "Version": "${version}")

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        
        // Replace placeholders in manifest.json
        val props = mapOf(
            "group" to project.group,
            "version" to project.version,
            "description" to project.description
        )
        inputs.properties(props)
        
        filesMatching("manifest.json") {
            expand(props)
        }
    }
    
    // Configure ShadowJar (bundle dependencies)
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        
        // Relocate dependencies to avoid conflicts
        relocate("com.google.gson", "com.yourplugin.libs.gson")
        
        // Minimize JAR size (removes unused classes)
        minimize()
    }
    
    // Configure tests
    test {
        useJUnitPlatform()
    }
    
    // Make build depend on shadowJar
    build {
        dependsOn(shadowJar)
    }
}

// Configure Java toolchain
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}




