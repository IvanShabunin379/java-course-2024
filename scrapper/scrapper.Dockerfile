FROM openjdk:21
WORKDIR /app
COPY /target/bot.jar /app/scrapper.jar
EXPOSE 8080 8081
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
