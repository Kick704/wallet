FROM maven:3.9.9 AS builder

WORKDIR /wallet
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /wallet
COPY --from=builder /wallet/target/wallet-0.0.1-SNAPSHOT.jar /wallet-app.jar

ENTRYPOINT ["java", "-jar", "/wallet-app.jar"]
EXPOSE 8080