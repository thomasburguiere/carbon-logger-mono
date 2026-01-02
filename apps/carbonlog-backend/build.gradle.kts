plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(project(":libs:carbonlog-base"))
    implementation("tools.jackson.module:jackson-module-kotlin")
}
