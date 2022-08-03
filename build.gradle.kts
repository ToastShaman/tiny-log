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

    testImplementation(Testing.junit.jupiter.api)
    testRuntimeOnly(Testing.junit.jupiter.engine)
    testImplementation("org.skyscreamer:jsonassert:_")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}