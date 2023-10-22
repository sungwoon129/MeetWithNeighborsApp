FROM adoptopenjdk:17-jdk-hotspot

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/weyoui-app-core-0.0.1-SNAPSHOT.jar /weyoui-server/weyoui-app-core.jar

WORKDIR /weyoui-server

CMD ["java","-jar","weyoui-app-core-0.0.1-SNAPSHOT.jar"]

ENTRYPOINT ["java", "-jar", "app.jar"]