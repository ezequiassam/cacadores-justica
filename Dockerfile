FROM openjdk:8u111-jre-alpine

## Install timezone data
RUN apk add --no-cache tzdata

## Default to TimeZone
ENV TZ America/Bahia

## Default to UTF-8 file.encoding
ENV LANG C.UTF-8

ADD ./build/libs/*.jar app.jar

EXPOSE 8080

# Regarding settings of java.security.egd, see http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
ENTRYPOINT ["java", "-Dspring.profiles.active=docker,production", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
