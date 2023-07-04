plugins {
    alias(libs.plugins.android.application).version("8.0.1")
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.hilt).version("2.43")
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.geekydroid.materialclock"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.geekydroid.materialclock"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            //proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/gradle/*")
        }
    }
}

dependencies {

    androidTestImplementation(libs.androidx.navigation.testing)
    implementation(libs.material.three)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.prefs)
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.joda.time)

    testImplementation(libs.android.test.exts)
    testImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.google.truth)
    testImplementation(libs.google.truth.java.exts)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.google.truth.java.exts)

    testImplementation(libs.mock.test)
    testImplementation(libs.mock.agent)

    androidTestImplementation(libs.mock.test)
    androidTestImplementation(libs.mock.agent)
}