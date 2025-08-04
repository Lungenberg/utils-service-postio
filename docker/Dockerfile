FROM openjdk:21

WORKDIR /srv

COPY target/*.jar .
COPY docker/utils-service.sh .

RUN chmod +x utils-service.sh

EXPOSE 8185

ENTRYPOINT ["./utils-service.sh"]