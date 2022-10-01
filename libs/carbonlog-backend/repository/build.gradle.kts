import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion: String by project

plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
}

group = "ch.burguiere.carbonlog"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {

    implementation("org.mongodb:mongodb-driver-reactivestreams")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.projectreactor:reactor-core")
    implementation("org.springframework:spring-context")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")


    api(project(":libs:carbonlog-base"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = javaVersion
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
