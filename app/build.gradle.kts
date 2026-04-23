plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // 这里不需要写版本号了，直接应用即可
    kotlin("plugin.compose")
}


android {
    namespace = "com.xyzone.blogs.app.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.xyzone.blogs.app.android"
        minSdk = 28
        targetSdk = 35 
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        // 开启 Compose 开关
        compose = true
        // 如果你不需要写 XML 布局了，可以关掉这个节省编译时间
        viewBinding = false
    }

    // 注意：在 Kotlin 2.0+ 模式下，这里不需要 composeOptions 块了，直接删掉

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget("17"))
        }
    }
}

dependencies {
    // 导入 Compose 物料清单 (BOM)
    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    
    // Compose 核心组件
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // 让你的 Activity 能够使用 setContent { ... }
    implementation("androidx.activity:activity-compose:1.9.0")

    // 保留你原有的核心库
    implementation(libs.androidx.core.ktx)
}
