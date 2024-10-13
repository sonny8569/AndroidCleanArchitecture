import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
}

val properties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { inputStream ->
        properties.load(inputStream)
    }
}

android {
    namespace = "com.example.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        val kakaoBaseUrl: String = properties.getProperty("kakao_base_url", "")
        val kakaoVideoUrl: String = properties.getProperty("kakao_video_url", "")
        val kakaoImgUrl: String = properties.getProperty("kakao_img_url", "")
        val kakaoKey: String = properties.getProperty("kakao_key", "")
        val kakaoHeader: String = properties.getProperty("kakao_header", "")
        val projectKey: String = properties.getProperty("project_key", "")

        buildConfigField("String", "KAKAO_BASE_URL", kakaoBaseUrl)
        buildConfigField("String", "KAKAO_VIDEO_URL", kakaoVideoUrl)
        buildConfigField("String", "KAKAO_IMG_URL", kakaoImgUrl)
        buildConfigField("String", "KAKAO_KEY", kakaoKey)
        buildConfigField("String", "KAKAO_HEADER", kakaoHeader)
        buildConfigField("String", "PROJECT_KEY", projectKey)
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
        buildConfig = true
    }
}
kapt {
    correctErrorTypes = true
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //hilt
    implementation(libs.dagger.hilt)
    kapt(libs.compoler.hilt)
    //network
    implementation(libs.squareup.retrofit2.retrofit)
    implementation(libs.squareup.retrofit2.converter.kotlinx.serialization)
    implementation(libs.squareup.okhttp3.logging.interceptor)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}