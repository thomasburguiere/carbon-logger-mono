plugins {
    id("buildlogic.kotlin-library-conventions")
}


dependencies {
    implementation("org.mongodb:mongodb-driver-reactivestreams")

    api(project(":libs:carbonlog-base"))
}
