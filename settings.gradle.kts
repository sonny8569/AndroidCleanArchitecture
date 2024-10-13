pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NathanHomeWork"
include(":app")
include(":feature")
include(":feature:search")
include(":feature:like")
include(":feature:detail")
include(":network")
include(":data")
include(":controllers")
include(":controllers:network")
include(":domain")
include(":device")
include(":controllers:device")
