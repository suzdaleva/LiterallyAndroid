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

rootProject.name = "Literally"
include(":app")
include(":uikit")
include(":feature")
include(":core:network")
include(":core:common")
include(":storage")
include(":storage:api")
include(":storage:impl")
include(":data")
include(":data:authorization")
include(":feature:login")
include(":data:profile")
include(":feature:profile")
include(":feature:books")
include(":data:books")
