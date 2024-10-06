import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.flixsterpart1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.flixsterpart1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val apikeyPropertiesFile = rootProject.file("apikey.properties")
        println("API FILE: ${apikeyPropertiesFile}")
        val apikeyProperties = Properties().apply {
            load(FileInputStream(apikeyPropertiesFile))
        }
        println("PROPERTIES: ${apikeyProperties["api_key"]}")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Inject the API key into BuildConfig
        buildConfigField("String", "api_key", "\"${apikeyProperties["api_key"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.code.gson:gson:2.8.9")
    // Include AsyncHttpClient dependency
    implementation("com.codepath.libraries:asynchttpclient:2.2.0")
    // Add Glide dependency
    implementation("com.github.bumptech.glide:glide:4.15.1") // Glide library
    kapt("com.github.bumptech.glide:compiler:4.15.1") // Glide compiler for annotation processing
}