#
# docker build --no-cache --build-arg project_build_directory=target,project_build_finalName=sb-discovery-service,project_expose_port=8761 -t brijeshdhaker/sb-discovery-service:1.0.0 -f ./sb-discovery-service/Dockerfile ./sb-discovery-service
# docker push brijeshdhaker/sb-discovery-service:1.0.0
#
ARG project_artifactId="sb-discovery-service"
ARG project_build_directory="target"
ARG project_build_finalName="sb-discovery-service"
ARG project_expose_port=8100

FROM openjdk:17-jdk-slim

LABEL MAINTAINER="Brijesh K Dhaker"
LABEL AUTHOR_EMAIL="brijeshdhaker@gmail.com"
LABEL PROJECT_NAME="sb-discovery-service"
LABEL PROJECT="sb-discovery-service"

ARG PARAM=${project_artifactId}

CMD mkdir -p /apps/{libs,configs}
 
# ADD ${project.build.directory}/${project.build.finalName}.jar /apps/libs/${project.build.finalName}.jar
ADD target/sb-discovery-service.jar /apps/libs/sb-discovery-service.jar

EXPOSE 8761

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -Dspring.config.location=/mnt/configs/application.yaml -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -jar /apps/libs/sb-discovery-service.jar