FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="smart-campus"
LABEL description="Smart Campus Management System"

WORKDIR /app

RUN apk add --no-cache curl jq && \
    rm -rf /var/cache/apk/*

COPY target/smart-campus-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /app/app.jar"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1
