FROM openjdk
COPY ./target/bookflix-0.0.1-SNAPSHOT.jar ./
COPY ./src/main/resources/secure-connect.zip ./
WORKDIR ./
EXPOSE 9090
CMD ["java", "-jar", "bookflix-0.0.1-SNAPSHOT.jar"]