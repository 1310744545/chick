FROM java:8

COPY *.jar /chickTools.jar

CMD ["--server-port=10086"]

EXPOSE 10086

ENTRYPOINT ["java", "-jar", "/chickTools.jar"]