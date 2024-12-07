plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("io.freefair.lombok") version "6.6"
}

android {
    namespace = "com.dev.mail.lpta2302.final_mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.mail.lpta2302.final_mobile"
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

    packaging {
        // Exclude the conflicting files
        resources.excludes.add("META-INF/NOTICE.md")
        resources.excludes.add( "META-INF/NOTICE")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.tasks)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    compileOnly("org.projectlombok:lombok:1.18.36")  // Thêm Lombok vào đây
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    implementation("org.springframework.security:spring-security-crypto:5.7.3")
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")

}
