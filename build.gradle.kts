plugins {
    application
}

group = "org.newosrs"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.119.Final")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

application {
    mainClass.set("org.newosrs.bootstrap.Bootstrap")
}

tasks.test {
    useJUnitPlatform()
}
