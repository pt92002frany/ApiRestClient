plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.sv.ues.dam235.apirestclient"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "edu.sv.ues.dam235.apirestclient"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ===== Dependencias originales del template =====
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    // ===== AGREGADAS PARA LA PRÁCTICA =====

    // Retrofit (cliente HTTP para consumir la API REST)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Converter Gson (convierte JSON <-> objetos Java automáticamente)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson (parseo manual de JSON si se necesita)
    implementation("com.google.code.gson:gson:2.10.1")

    // OkHttp Logging Interceptor (muestra en Logcat las peticiones y respuestas)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // RecyclerView (para mostrar la lista de productos)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}