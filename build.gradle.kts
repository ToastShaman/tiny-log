plugins {
    id("java")
}

group = "com.github.toastshaman"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:_")
    implementation("org.slf4j:slf4j-api:_")

    testImplementation(Testing.junit.jupiter.api)
    testRuntimeOnly(Testing.junit.jupiter.engine)
    testImplementation("org.skyscreamer:jsonassert:_")
    testImplementation("org.awaitility:awaitility:_")
    testImplementation("com.google.truth:truth:_")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}