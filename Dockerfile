FROM java:openjdk-8-jdk

ENV MAVEN_VERSION 3.3.9

RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

VOLUME /root/.m2

# Compile from source
COPY src/ /root/middleware/src
ADD pom.xml /root/middleware/pom.xml

WORKDIR "/root/middleware"

EXPOSE 8081
EXPOSE 8082

RUN mvn package

CMD ["java", "-cp", "target/mongodb.driver-1.0-SNAPSHOT-jar-with-dependencies.jar", "com.mogtechnologies.mongodbdriver.App"]

# Run commands
#docker build -t middleware .
#docker run --name middleware_instance -i -p 8081:8081 -p 8082:8082 -t middleware