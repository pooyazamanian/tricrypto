pluginManagement {
    repositories {
        flatDir {
            dirs ("app/libs") // مسیر پوشه libs در ماژول اپلیکیشن
        }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
//        maven { url = uri("https://maven.myket.ir") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        flatDir {
            dirs ("app/libs") // مسیر پوشه libs در ماژول اپلیکیشن
        }
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            name = "TarsosDSP repository"
            url = uri("https://mvn.0110.be/releases")
        }
//        maven { url = uri("https://maven.myket.ir") }

    }
}

rootProject.name = "trade app"
include(":app")