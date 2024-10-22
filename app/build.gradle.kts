plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
//    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.mymusicservise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mymusicservise"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    /*
   Navigate
   */
    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:ksp:4.16.0")

    /**
     *   viewBinding
     */
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.9")
    // room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ///
    implementation("de.hdodenhof:circleimageview:3.1.0")

    /*  Core
    */
    implementation("androidx.core:core-ktx:1.7.0")

    /*  Material
    */
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    /*  Test
    */
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    /* Better Logging In Android Using Timber
    /
    implementation "com.jakewharton.timber:timber:5.0.1"

    /*   Android Runtime Permission Library
    */
    implementation("com.nabinbhandari.android:permissions:3.8")

     */


    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.7.0")

    // Better Logging In Android Using Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")

    //palette
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("androidx.media:media:1.7.0")


    implementation ("com.mpatric:mp3agic:0.9.0")
//    implementation("com.github.Adonai:jaudiotagger:2.3.14")


    /**
     * lottie
     */
    implementation("com.airbnb.android:lottie:4.2.0")
}