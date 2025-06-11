#
# docker build --no-cache -t brijeshdhaker/sb-server-module:1.0.0 -f ./bd-spring-module/sb-server-module/Dockerfile ./bd-spring-module/sb-server-module
# docker push brijeshdhaker/sb-server-module:1.0.0
#

FROM openjdk:25-ea-17-jdk-bullseye

LABEL MAINTAINER="Brijesh K Dhaker"
LABEL AUTHOR_EMAIL="brijeshdhaker@gmail.com"
LABEL PROJECT_NAME=bd-spring-module
LABEL PROJECT=${project.artifactId}

ARG FOO=${project.artifactId}

CMD mkdir -p /apps/{libs,configs}
 
ADD ${project.build.directory}/${project.build.finalName}.jar /apps/libs/${project.build.finalName}.jar
 
EXPOSE 8080

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -Dspring.config.location=/mnt/configs/application.yaml -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=/apps/configs/application.yaml -jar /apps/libs/${project.build.finalName}.jar