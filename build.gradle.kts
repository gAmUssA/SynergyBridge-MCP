plugins {
    java
    id("io.quarkus")
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val quarkusMcpServerVersion: String by project

group = "com.synergybridge"
version = "9.0.0.1-SP3-HF42-FINAL-FINAL2-REALLYFINAL"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))

    // Quarkus core dependencies
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-vertx-http")

    // Quarkus MCP Server transport
    // HTTP transport - provides both Streamable HTTP (/mcp) and HTTP/SSE (/mcp/sse) endpoints
    // Note: Use quarkus-mcp-server-stdio instead if you need STDIO transport for CLI integration
    implementation("io.quarkiverse.mcp:quarkus-mcp-server-http:${quarkusMcpServerVersion}")

    // Testing dependencies
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.rest-assured:rest-assured")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:2.0.3")
    testImplementation("org.testcontainers:junit-jupiter:2.0.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("container")
    }
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.register<Test>("containerTest") {
    useJUnitPlatform {
        includeTags("container")
    }
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
