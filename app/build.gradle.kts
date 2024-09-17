plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "ir.masouddabbaghi.eduregister"
    compileSdk = 35

    defaultConfig {
        applicationId = "ir.masouddabbaghi.eduregister"
        buildToolsVersion = "35.0.0"
        minSdk = 28
        targetSdk = 35
        versionCode = 15
        versionName = "1.0.15"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.adapter.rxjava)
    implementation(libs.android.justified)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.carbon)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.dotsindicator)
    implementation(libs.glide)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.library)
    implementation(libs.logging.interceptor)
    implementation(libs.lottie)
    implementation(libs.material)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.icu4j)
    implementation(libs.persian.date.picker.dialog)

    ksp(libs.ksp)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
