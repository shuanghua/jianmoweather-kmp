pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/amper/amper")
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}

plugins {
    id("org.jetbrains.amper.settings.plugin").version("0.5.0")
}

include(":android-app")
include(":ios-app")
include(":shared")
include(":data:domain")
include(":data:repo")
include(":data:network")
include(":data:database-room")
include(":data:database-sqldelight")
include(":data:datastore")
include(":data:location")
include(":data:base-lib")
include(":data:model")
