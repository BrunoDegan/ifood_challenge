import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.devtools.ksp")
    id("io.insert-koin.compiler.plugin")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.skydoves.compose.stability.analyzer")
}

extensions.configure<ApplicationExtension> {
    compileSdk = 37

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        minSdk = 30
        targetSdk = 37

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
}

// Compile time check
ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_19)
    }
}

composeStabilityAnalyzer {
    traceAll {
        enabled.set(true)
        threshold.set(2) // default: 2 — skips the initial-composition burst
    }
    stabilityValidation {
        // Log stability changes as warnings instead of failing the build
        failOnStabilityChange.set(true)
        includeTests.set(false)
        ignoreNonRegressiveChanges.set(false)
    }
}
