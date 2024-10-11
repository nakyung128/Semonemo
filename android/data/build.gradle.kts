import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())
android {
    namespace = "com.semonemo.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "CHAIN_ID", properties["CHAIN_ID"] as String)
        buildConfigField("String", "CHAIN_NAME", properties["CHAIN_NAME"] as String)
        buildConfigField("String", "RPC_URLS", properties["RPC_URLS"] as String)
        buildConfigField("String", "SEVER_URL", properties["SEVER_URL"] as String)
        buildConfigField("String", "NODE_PORT_NUMBER", properties["NODE_PORT_NUMBER"] as String)
        buildConfigField("String", "IPFS_UPLOAD_URL", properties["IPFS_UPLOAD_URL"] as String)
        buildConfigField("String", "IPFS_READ_URL", properties["IPFS_READ_URL"] as String)
        buildConfigField("String", "AI_SERVER_URL", properties["AI_SERVER_URL"] as String)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.bundles.network)
    implementation(libs.datastore.preferences)
    implementation(libs.junit)
    implementation(project(":domain"))
    // metamask
    implementation(libs.metamask.android.sdk)
}
