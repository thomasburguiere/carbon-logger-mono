repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}


kotlin {
    jvm()

    js {
        outputModuleName = "carbonlog-model"
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.library()
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1-0.6.x-compat")
            // put your Multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertj.core)
        }
        jsTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
