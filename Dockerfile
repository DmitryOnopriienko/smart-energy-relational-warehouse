FROM amazoncorretto:21 AS builder
# The Gradle wrapper script needs xargs, which the minimal Amazon Linux base image lacks
RUN dnf install -y findutils && dnf clean all
WORKDIR /app

COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

FROM amazoncorretto:21
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
