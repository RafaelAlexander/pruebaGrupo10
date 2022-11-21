FROM openjdk:8

COPY target/ejercicio-*.jar /ejercicio.jar

CMD ["java", "-jar", "/demo.jar"]