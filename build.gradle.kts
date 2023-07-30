buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application).version("8.0.1").apply(false)
    alias(libs.plugins.android.library).version("8.0.1").apply(false)
    alias(libs.plugins.jetbrains.kotlin).version("1.8.10").apply(false)
    alias(libs.plugins.hilt).version("2.47").apply(false)
    alias(libs.plugins.kotlin.kapt).version("1.8.10").apply(false)
}