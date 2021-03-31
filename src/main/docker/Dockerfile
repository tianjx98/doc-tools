FROM openjdk:11-jre

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

ADD *.jar app.jar

CMD echo "The application will start " && \
    java ${JAVA_OPTS}  -jar /app.jar

EXPOSE 80
