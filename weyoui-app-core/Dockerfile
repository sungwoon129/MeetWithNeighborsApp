FROM openjdk:17-jdk-slim

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/weyoui-app-core-0.0.1-SNAPSHOT.jar /weyoui-server/weyoui-app-core.jar

WORKDIR /weyoui-server

CMD ["java", "-jar", "weyoui-app-core.jar", "--spring.profiles.active=dev"]