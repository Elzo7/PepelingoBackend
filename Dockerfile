FROM  openjdk:17
ADD target/PepelingBackendV2.jar PepelingoBackendV2.jar
EXPOSE 6969
ENTRYPOINT ["java", "-jar","PepelingoBackendV2.jar"]