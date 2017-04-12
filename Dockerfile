FROM maven:3-jdk-8

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app
#VOLUME /usr/src/app/target
CMD ["mvn", "-Ptest","-e", "install"]
#RUN ["mvn", "-Ptest","-e", "install"]
RUN ["mvn", "-Dmaven.test.skip=true", "package"]
#VOLUME dockerMavenOut:/usr/src/app/target
RUN ["cp", "-r", "/usr/src/app/target/", "/dockerMavenOut/"]
VOLUME /dockerMavenOut
