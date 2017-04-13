#RUn: ON build only    CMD: only last executed, on run only
#TODO RUn the install and cmd the prod
FROM maven:3-jdk-8

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app
#VOLUME /usr/src/app/target
#CMD ["mvn", "-Ptest","-e", "install"]
#RUN ["mvn", "-Ptest","-e", "install"]
#RUN ["mvn", "clean"]
#RUN ["mvn", "-Pproduction", "-Dmaven.test.skip=true", "package"]
#CMD ["mvn", "-Pproduction", "-Dmaven.test.skip=true", "package"]
#-Dmaven.clean.failOnError=false  normal clean fail because training-java is a volume, so its done manually
CMD mvn -Ptest -e install && rm -f -r /usr/src/app/target/training-java-0.1.0/* && mvn -Pproduction -e -Dmaven.test.skip=true package

#VOLUME dockerMavenOut:/usr/src/app/target
#RUN ["cp", "-r", "/usr/src/app/target/", "/dockerMavenOut/"]
#VOLUME /dockerMavenOut
