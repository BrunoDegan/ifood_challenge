plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.kotlin.serialization.gradlePlugin)
    implementation(libs.kotlin.parcelize.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.koin.compiler.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
    implementation(libs.stability.analyzer.gradlePlugin)
}
