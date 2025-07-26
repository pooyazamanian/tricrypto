import org.gradle.internal.declarativedsl.parsing.main

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.tradeapp"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.tradeapp"
        minSdk = 30
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

    ksp {
        arg("dagger.fullBindingGraphValidation", "ERROR")
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
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //hilt
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    //room
    ksp(libs.androidx.room.compiler.v261)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)

    //coroutines
    implementation (libs.kotlinx.coroutines.android)

    //supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.0-rc-1"))
    implementation(libs.postgrest.kt)
    implementation(libs.auth.kt)
    implementation(libs.realtime.kt)
    implementation ("io.github.jan-tennert.supabase:storage-kt")
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.android)

    implementation(libs.ktor.client.cio)           // Engine با پشتیبانی WebSocket
    implementation(libs.ktor.client.websockets)   // پلاگین وب‌‌سوکت Ktor :contentReference[oaicite:1]{index=1}
    implementation(libs.kotlinx.serialization.json) // برای JSON
    implementation(platform("io.ktor:ktor-bom:3.2.0"))
    implementation("io.ktor:ktor-client-android")
    implementation("io.ktor:ktor-client-logging")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    //online img
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation ("androidx.security:security-crypto:1.1.0-alpha03")
    //vico (chart in jetpack compose )
    implementation(libs.vico.compose)
    implementation("dev.spght:encryptedprefs-ktx:1.0.3")



}