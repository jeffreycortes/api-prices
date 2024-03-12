FROM openjdk:11.0-jdk-slim-stretch
#RUN apk --update add tzdata
RUN echo "America/Bogota" > /etc/timezone
RUN ln -snf /usr/share/zoneinfo/America/Bogota /etc/localtime && echo America/Bogota > /etc/timezone
ARG DEPENDENCY=target
COPY ${DEPENDENCY}/api-prices.jar /home/api-prices.jar


ENTRYPOINT ["java","-jar","-Dspring.profiles.active=develop","/home/api-prices.jar"]
