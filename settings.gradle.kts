rootProject.name = "appliance-controller"

pluginManagement {
    plugins {
        val springBootVersion: String by settings
        val springDependencyManagementVersion: String by settings
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }
}