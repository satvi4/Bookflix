FROM openjdk
COPY ./target/bookflix-0.0.1-SNAPSHOT.jar ./
WORKDIR ./
EXPOSE 9090
CMD ["java", "-jar", "bookflix-0.0.1-SNAPSHOT.jar"]