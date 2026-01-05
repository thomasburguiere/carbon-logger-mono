plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(":libs:carbonlog-model"))
    implementation("tools.jackson.module:jackson-module-kotlin")
}
