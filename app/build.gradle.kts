plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 26
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
    android {
        buildFeatures {
            viewBinding = true
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.4.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.squareup.picasso:picasso:2.8")

    implementation ("com.github.AnupKumarPanwar:ScratchView:1.9.1")
    implementation ("com.google.android.gms:play-services-ads:23.1.0")

    implementation ("com.airbnb.android:lottie:6.4.1")
}
