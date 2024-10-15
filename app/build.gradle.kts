plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.nathanhomework"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nathanhomework"
        minSdk = 31
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}
kapt {
    correctErrorTypes = true
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
    //hilt
    implementation(libs.dagger.hilt)
    kapt(libs.compoler.hilt)
    implementation(libs.jetbrains.kotlinx.serialization.json)
//    implementation(project(":feature"))
    implementation(project(":feature:like"))
    implementation(project(":feature:search"))
    implementation(project(":feature:detail"))
    implementation(project(":network"))
    implementation(project(":data"))
//    implementation(project(":controllers"))
    implementation(project(":controllers:network"))
    implementation(project(":controllers:device"))
    implementation(project(":device"))
    implementation(project(":domain"))
}