# Stage 1: build the jar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -DskipTests && \
    JAR=$(ls target/*.jar | grep -v '^target/original-' | head -n 1) && \
    cp "$JAR" /tmp/app.jar

# Stage 2: runtime with virtual display + noVNC
FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y --no-install-recommends \
        xvfb x11vnc novnc websockify \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /tmp/app.jar /app/app.jar
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

ENV DISPLAY=:1
EXPOSE 6080
CMD ["/app/start.sh"]