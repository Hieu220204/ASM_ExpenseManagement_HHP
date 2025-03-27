plugins {
    alias(libs.plugins.android.application)
}

android {
<<<<<<< HEAD
    namespace = "com.example.asm_expensemanageapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.asm_expensemanageapp"
=======
    namespace = "com.example.androi_asm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androi_asm"
>>>>>>> 9429b28cf12214c8cbfd9a2fcfeb8c38cbeade7c
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
<<<<<<< HEAD
=======
    buildFeatures {
        viewBinding = true
    }
>>>>>>> 9429b28cf12214c8cbfd9a2fcfeb8c38cbeade7c
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
<<<<<<< HEAD
=======

    implementation("androidx.core:core:1.12.0")


>>>>>>> 9429b28cf12214c8cbfd9a2fcfeb8c38cbeade7c
}