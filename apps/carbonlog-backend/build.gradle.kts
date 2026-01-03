plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":libs:carbonlog-model"))
    implementation("tools.jackson.module:jackson-module-kotlin")
}
