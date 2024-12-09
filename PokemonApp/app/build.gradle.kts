plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.pokemonapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pokemonapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
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
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.ui.desktop)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation("com.google.code.gson:gson:2.8.9")
    // Include AsyncHttpClient dependency
    implementation("com.codepath.libraries:asynchttpclient:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    // Add Glide dependency
    implementation("com.github.bumptech.glide:glide:4.15.1") // Glide library
    kapt("com.github.bumptech.glide:compiler:4.15.1") // Glide compiler for annotation processing

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("com.github.bumptech.glide:glide:4.15.1") // Glide library
    kapt("com.github.bumptech.glide:compiler:4.15.1") // Glide compiler for annotation processing
    // Room
    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.fragment:fragment-ktx:1.5.0")
    implementation("com.google.android.material:material:1.5.0")
}