# ============================================================
# Stage 1 – Build
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

# Copy Maven wrapper and POM first to leverage layer caching
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source and build the fat JAR (skip tests; run them in CI)
COPY src src
RUN ./mvnw package -B -DskipTests

# ============================================================
# Stage 2 – Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy only the fat JAR from the builder stage
COPY --from=builder /workspace/target/apartment-*.jar app.jar

# Fix ownership
RUN chown appuser:appgroup app.jar

USER appuser

# Spring Boot default port
EXPOSE 8080

# Pass JVM flags via JAVA_OPTS for easy tuning at runtime
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
