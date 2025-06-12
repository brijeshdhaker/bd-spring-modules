#
# docker build --no-cache --build-arg project_build_directory=${project.build.directory},project.build.finalName=${project.build.finalName} -t brijeshdhaker/${project.build.finalName}:1.0.0 -f ${project.basedir}/Dockerfile ${project.basedir}
# docker push brijeshdhaker/${project.build.finalName}:1.0.0
#
ARG project_artifactId=${project.build.finalName}
ARG project_build_directory=${project.build.directory}
ARG project_build_finalName=${project.build.finalName}
ARG project_expose_port=8100

FROM openjdk:25-ea-17-jdk-bullseye

LABEL MAINTAINER="Brijesh K Dhaker"
LABEL AUTHOR_EMAIL="brijeshdhaker@gmail.com"
LABEL PROJECT_NAME=${project.build.finalName}
LABEL PROJECT=${project.build.finalName}

ARG ARTIFACT_ID=${project.build.finalName}

CMD mkdir -p /apps/{libs,configs}
 
ADD ${project.build.directory}/${project.build.finalName}.${project.packaging} /apps/libs/${project.build.finalName}.${project.packaging}

EXPOSE 8100

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -Dspring.config.location=/apps/configs/application.yaml -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=/apps/configs/application.yaml -jar /apps/libs/${project.build.finalName}.${project.packaging}