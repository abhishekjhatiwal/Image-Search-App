plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.imagesearchapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.imagesearchapp"
        minSdk = 24
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

// app/build.gradle.kts
val hiltVersion = "2.57.2" // Core Hilt version
val hiltXVersion = "1.2.0" // androidx.hilt version (for ViewModel, etc.)
val retrofitVersion = "2.9.0"
val okhttpVersion = "4.12.0"
val gsonVersion = "2.10.1"
// Use this to define the version across all Paging dependencies
val pagingVersion = "3.2.1" // Check for the latest stable version
val coilVersion = "2.6.0" // Always check for the latest stable version

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.paging.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Core Hilt Dependencies
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Optional: for Hilt Jetpack integrations (like @HiltViewModel)
    implementation("androidx.hilt:hilt-navigation-fragment:$hiltXVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltXVersion")

    // If using Jetpack Compose
     implementation("androidx.hilt:hilt-navigation-compose:$hiltXVersion")

    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")
// 2. Converters (Select only one, Gson is the most common)
// Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:${retrofitVersion}")
    implementation("com.google.code.gson:gson:${gsonVersion}")

    // Core Paging 3 library (required for Pager, PagingSource, etc.)
    implementation("androidx.paging:paging-runtime-ktx:${pagingVersion}")
// Dependency for collectAsLazyPagingItems() and Compose integration
    implementation("androidx.paging:paging-compose:${pagingVersion}")
// If you are also using the ViewModel integration
    implementation("androidx.paging:paging-common-ktx:${pagingVersion}")

    // Dependency for the AsyncImage composable
    implementation("io.coil-kt:coil-compose:${coilVersion}")
}