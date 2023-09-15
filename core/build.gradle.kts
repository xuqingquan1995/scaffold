@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

android {
    namespace = "top.xuqingquan"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourcePrefix = "scaffold_"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        release {
            isJniDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets {
        named("main") {
            jniLibs {
                srcDir("libs")//说明so的路径为该libs路径，关联所有so文件
            }
        }
    }
    packaging {
        resources {
            excludes += "DebugProbesKt.bin"
        }
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    api(libs.utils)

    api(libs.bundles.kotlin)
    api(libs.bundles.arch)
    api(libs.bundles.ui)

    compileOnly(libs.paging)
    ksp(libs.glide.ksp)

    //net
    api(libs.bundles.net)
    api(libs.bundles.convert)
    //eventbus
    api(libs.eventbus)
    //debug
    debugApi(libs.bundles.debug)
    releaseCompileOnly(libs.bundles.debug)
    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidTest)
}

afterEvaluate {
    publishing {
        publications {
            register("release",MavenPublication::class){
                from(components["release"])
                groupId = "top.xuqingquan"
                artifactId = "scaffold"
                version = libs.versions.versionName.get()
            }
        }
    }
}