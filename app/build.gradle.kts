plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt") // KAPT 플러그인 추가
    id("com.google.devtools.ksp")
}

android {
    namespace = "kr.nicepayment.paypro"
    compileSdk = 34

    defaultConfig {
        applicationId = "kr.nicepayment.paypro"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            //buildConfigField("String", "ENVIRONMENT", "\"DEVELOP\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            //buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
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
        dataBinding = true
        //buildConfig = true
    }

}

dependencies {

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.preference:preference:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    val roomVersion = "2.6.1"
    implementation ("androidx.room:room-runtime:$roomVersion")
    kapt ("androidx.room:room-compiler:$roomVersion")

    // Room KTX
    implementation ("androidx.room:room-ktx:$roomVersion")

    //Gson
    implementation("com.google.code.gson:gson:2.9.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //zxing
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")

    //Lottie
    implementation ("com.airbnb.android:lottie:3.4.0")

    //MPAndroidChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}