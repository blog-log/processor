FROM gradle:7.2-jdk16-openj9 as builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle build --stacktrace

FROM openjdk:16-jdk-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=builder /builder/build/libs/processor-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "processor-0.0.1-SNAPSHOT.jar"]