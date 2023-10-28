plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.openapi.generator") version "7.0.1"
}

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://repo.maven.apache.org/maven2/")
    }
//	dependencies {
//		classpath("org.openapitools:openapi-generator-gradle-plugin:5.2.1")
//	}
}

//apply(plugin = "org.openapitools.openapigenerator")

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

    implementation("org.apache.tomcat.embed:tomcat-embed-core:9.0.54")
//    implementation("org.openapitools.jackson.nullable:jackson-nullable:0.2.6")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${project.property("springBootVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-web:${project.property("springBootVersion")}")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:${project.property("springMybatisVersion")}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.property("springOpenApiVersion")}")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test:${project.property("springBootVersion")}")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:${project.property("springMybatisVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter:${project.property("jUnitVersion")}")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/appliance-api.yml".toString())
    outputDir.set("$projectDir/generated".toString())
    apiPackage.set("ru.lember.generated.api")
    modelPackage.set("ru.lember.generated.model")
    configOptions.set(mapOf(
        "interfaceOnly" to "true",
        "delegatePattern" to "true",
        "openApiNullable" to "false"
    ))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    compileJava {
        dependsOn(openApiGenerate)
    }
}

sourceSets {
    main {
        java {
            srcDir("generated")
        }
    }
}