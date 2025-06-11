#
# docker build --no-cache -t brijeshdhaker/sb-resource-server:1.0.0 -f ./bd-spring-module/sb-resource-server/Dockerfile ./bd-spring-module/sb-resource-server
# docker push brijeshdhaker/sb-resource-server:1.0.0
#

FROM openjdk:25-ea-17-jdk-bullseye
LABEL maintainer="Brijesh K Dhaker <brijeshdhaker@gmail.com>"
LABEL PROJECT_NAME=sb-resource-server
LABEL PROJECT=${project.artifactId}

ARG FOO=${project.artifactId}

CMD mkdir -p /apps/{libs,configs}
 
ADD ${project.build.directory}/${project.build.finalName}.jar /apps/libs/${project.build.finalName}.jar
 
EXPOSE 8085

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=/apps/configs/application.yaml -jar /apps/libs/${project.build.finalName}.jar