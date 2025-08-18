plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.astratechpoststask"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.astratechpoststask"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField ("boolean", "DEBUG", "true")
            buildConfigField("String", "BASE_URL", "\"http://taskapi.astra-tech.net/api/\"")
            buildConfigField("String","ALL_BLOGS_ENDPOINT", "\"blogs\"")
            buildConfigField("String","SHOE_BLOG_ENDPOINT", "\"blogs/shows/\"")
            buildConfigField("String","STORE_BLOG_ENDPOINT", "\"blogs/store\"")
            buildConfigField("String","UPDATE_BLOG_ENDPOINT", "\"blogs/update\"")
            buildConfigField("String","DELETE_BLOG_ENDPOINT", "\"blogs/delete\"")
        }
        release {
            buildConfigField ("boolean", "RELEASE", "true")
            buildConfigField("String", "BASE_URL", "\"http://taskapi.astra-tech.net/api/\"")
            buildConfigField("String","ALL_BLOGS_ENDPOINT", "\"blogs\"")
            buildConfigField("String","SHOE_BLOG_ENDPOINT", "\"blogs/shows/\"")
            buildConfigField("String","STORE_BLOG_ENDPOINT", "\"blogs/store\"")
            buildConfigField("String","UPDATE_BLOG_ENDPOINT", "\"blogs/update\"")
            buildConfigField("String","DELETE_BLOG_ENDPOINT", "\"blogs/delete\"")
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
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // --- Navigation Compose ---
    implementation(libs.androidx.navigation.compose)

    // --- Hilt ---
    implementation(libs.hilt.android)
    kapt (libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // --- Retrofit + OkHttp ---
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.relase)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}