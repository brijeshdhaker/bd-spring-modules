#
# docker build --no-cache --build-arg project_build_directory=target,project_build_finalName=sb-gateway-module,project_expose_port=8100 -t brijeshdhaker/sb-gateway-module:1.0.0 -f ./sb-gateway-module/Dockerfile ./sb-gateway-module
# docker push brijeshdhaker/sb-gateway-module:1.0.0
#

ARG project_build_directory="target"
ARG project_build_finalName="sb-gateway-module"
ARG project_expose_port=8100

FROM openjdk:17-jdk-slim

LABEL MAINTAINER="Brijesh K Dhaker"
LABEL AUTHOR_EMAIL="brijeshdhaker@gmail.com"
LABEL PROJECT=${project_build_finalName}

ENV project_build_directory=$project_build_directory
ENV project_build_finalName=$project_build_finalName
ENV project_expose_port=$project_expose_port

CMD mkdir -p /apps/{libs,configs}
 
ADD target/sb-gateway-module.jar /apps/libs/sb-gateway-module.jar
 
EXPOSE 8100

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -Dspring.config.location=/mnt/configs/application.yaml -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -jar /apps/libs/sb-gateway-module.jar