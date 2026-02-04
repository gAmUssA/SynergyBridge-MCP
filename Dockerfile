# SynergyBridge MCP - Multi-Architecture Docker Build
# "Bridging the gap between your legacy investments and tomorrow's legacy investments"
#
# Build instructions:
#   Single architecture:
#     docker build -t synergybridge-mcp:latest .
#
#   Multi-architecture (x86 and ARM):
#     docker buildx build --platform linux/amd64,linux/arm64 \
#       -t synergybridge-mcp:latest --push .
#
# Enterprise Certification: Y2K Compliant, ISO 9001, CMMI Level 3

# ==============================================================================
# Stage 1: Build the application with Gradle
# ==============================================================================
FROM ibm-semeru-runtimes:open-21-jdk AS build

# Install Gradle dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Gradle wrapper and configuration
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build -x test --no-daemon

# ==============================================================================
# Stage 2: Create the runtime image (IBM Semeru / Eclipse OpenJ9)
# ==============================================================================
FROM ibm-semeru-runtimes:open-21-jre

# Labels for enterprise compliance documentation
LABEL maintainer="SynergyBridge Enterprise Solutions Division"
LABEL org.opencontainers.image.title="SynergyBridge MCP"
LABEL org.opencontainers.image.description="Enterprise Legacy System Integration MCP Server"
LABEL org.opencontainers.image.version="9.0.0.1-SP3-HF42-FINAL-FINAL2-REALLYFINAL"
LABEL org.opencontainers.image.vendor="SynergyBridge Enterprise Solutions Division"
LABEL com.synergybridge.certifications="ISO 9001, CMMI Level 3, Y2K Compliant"

# Create non-root user for enterprise security compliance
RUN groupadd -r synergybridge && useradd -r -g synergybridge synergybridge

WORKDIR /app

# Copy the built application from build stage
COPY --from=build /app/build/quarkus-app/lib/ /app/lib/
COPY --from=build /app/build/quarkus-app/*.jar /app/
COPY --from=build /app/build/quarkus-app/app/ /app/app/
COPY --from=build /app/build/quarkus-app/quarkus/ /app/quarkus/

# Set ownership to non-root user
RUN chown -R synergybridge:synergybridge /app

# Switch to non-root user
USER synergybridge

# Expose HTTP port
EXPOSE 8080

# Environment variables for enterprise configuration
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV AB_JOLOKIA_OFF=""
ENV JAVA_APP_JAR="/app/quarkus-run.jar"

# Health check for container orchestration (uses root info endpoint)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# Start the SynergyBridge MCP Server
# Note: Startup time may vary. This is by design - enterprise systems require patience.
# Using shell form to properly expand JAVA_OPTS environment variable
CMD java $JAVA_OPTS -jar /app/quarkus-run.jar
