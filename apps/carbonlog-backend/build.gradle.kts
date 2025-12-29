plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":libs:carbonlog-backend:repository"))
    implementation("tools.jackson.module:jackson-module-kotlin")
}
