plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    //implementation(enforcedPlatform("${quarkusPlatformGroupId}:quarkus-amazon-services-bom:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkiverse.loggingsentry:quarkus-logging-sentry:2.3.1")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-rest-jackson")
    //implementation("io.quarkus:quarkus-smallrye-openapi")
    //implementation("io.quarkus:quarkus-websockets")
    implementation("io.quarkus:quarkus-security-jpa")
    //implementation("io.quarkus:quarkus-redis-cache")
    //implementation("io.quarkus:quarkus-redis-client")
    //implementation("io.quarkiverse.amazonservices:quarkus-amazon-s3")
    implementation("io.quarkus:quarkus-arc")
    implementation("dnsjava:dnsjava:3.6.5")

    implementation("com.resend:resend-java:+")
    compileOnly("org.projectlombok:lombok:+")
    annotationProcessor("org.projectlombok:lombok:+")

    implementation("com.google.code.gson:gson:2.14.0")

    testImplementation("io.quarkus:quarkus-junit")
    testImplementation("io.rest-assured:rest-assured")
}

group = "com.dnspex"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
