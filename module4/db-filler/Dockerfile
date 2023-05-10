FROM amazoncorretto:17-alpine
ADD /build/libs/db-filler-1.0.jar db-filler.jar
ENTRYPOINT ["java", "-jar", "db-filler.jar"]