FROM adoptopenjdk:17-jdk-hotspot

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/weyoui-app-payment-0.0.1-SNAPSHOT.jar /weyoui-server/weyoui-app-payment.jar

WORKDIR /weyoui-server

ENTRYPOINT ["java", "-jar", "weyoui-app-payment.jar"]