plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://repo.maven.apache.org/maven2/")
    }
}

group = "ru.lember"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("javax.servlet:javax.servlet-api:${project.property("servletApiVersion")}")
    implementation ("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${project.property("springBootVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-web:${project.property("springBootVersion")}")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:${project.property("springMybatisVersion")}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.property("springOpenApiVersion")}")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${project.property("springBootVersion")}")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:${project.property("springMybatisVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter:${project.property("jUnitVersion")}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}