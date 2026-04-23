// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // 基础插件声明，每个只留一行，且必须加上 apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    
    // 【新增】Compose 编译器插件声明
    // 因为你用了 Kotlin 2.2，我们这里也对齐版本
    kotlin("plugin.compose") version "2.2.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
