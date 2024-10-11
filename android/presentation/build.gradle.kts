import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.semonemo.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "CHAIN_ID", properties["CHAIN_ID"] as String)
        buildConfigField("String", "CHAIN_NAME", properties["CHAIN_NAME"] as String)
        buildConfigField("String", "RPC_URLS", properties["RPC_URLS"] as String)
        buildConfigField("String", "SEVER_URL", properties["SEVER_URL"] as String)
        buildConfigField(
            "String",
            "METAMASK_PACKAGE_NAME",
            properties["METAMASK_PACKAGE_NAME"] as String,
        )
        buildConfigField(
            "String",
            "METAMASK_PLAY_STORE_PATH",
            properties["METAMASK_PLAY_STORE_PATH"] as String,
        )
        buildConfigField("String", "CONTRACT_ADDRESS", properties["CONTRACT_ADDRESS"] as String)
        buildConfigField(
            "String",
            "NFT_CONTRACT_ADDRESS",
            properties["NFT_CONTRACT_ADDRESS"] as String,
        )
        buildConfigField(
            "String",
            "COIN_CONTRACT_ADDRESS",
            properties["COIN_CONTRACT_ADDRESS"] as String,
        )
        buildConfigField(
            "String",
            "SYSTEM_CONTRACT_ADDRESS",
            properties["SYSTEM_CONTRACT_ADDRESS"] as String,
        )
        buildConfigField("String", "IPFS_READ_URL", properties["IPFS_READ_URL"] as String)
        buildConfigField("String", "BOOTPAY_API_KEY", properties["BOOTPAY_API_KEY"] as String)
        buildConfigField("String", "USER_NAME", properties["USER_NAME"] as String)
        buildConfigField("String", "USER_EMAIL", properties["USER_EMAIL"] as String)
        buildConfigField("String", "USER_PHONE", properties["USER_PHONE"] as String)

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

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // coroutines
    implementation(libs.bundles.coroutines)

    // lottie
    implementation(libs.lottie)

    // MetaMask
    implementation(libs.metamask.android.sdk)

    // wallet connect
    implementation(libs.wallet.android.core)
    implementation(libs.web3wallet)

    // glide with landscapist
    implementation(libs.landscapist.glide)

    // immutable dependency
    implementation(libs.jetbrains.kotlinx.collections.immutable)

    // bottom navigation
    implementation(libs.androidx.navigation.compose)
    // MultiFloatActionButton
    implementation(libs.multifab)

    // web3j
    implementation(libs.web3j.core)

    // websocket
    implementation(libs.krossbow.stomp.core)
    implementation(libs.krossbow.websocket.okhttp)
    implementation(libs.krossbow.stomp.moshi)

    // moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // pager
    implementation(libs.androidx.foundation)

    // camera
    implementation(libs.bundles.camera)

    // PermissionState
    implementation(libs.accompanist.permissions)

    // graph
    implementation(libs.bundles.graph)

    // gson
    implementation(libs.gson)

    // capture
    implementation(libs.capturable)

    //
    implementation(libs.accompanist.systemuicontroller)

    // bootpay
    implementation(libs.bootpay)
}
