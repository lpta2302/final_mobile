plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("io.freefair.lombok") version "6.6"
}

android {
    namespace = "vn.edu.ueh.thanhdnh.firebase_example"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.edu.ueh.thanhdnh.firebase_example"
        minSdk = 29
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("org.projectlombok:lombok:1.18.24")  // Thêm Lombok vào đây
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("org.springframework.security:spring-security-crypto:5.7.3")
}
