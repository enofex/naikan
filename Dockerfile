# Build stage
FROM eclipse-temurin:23-alpine AS builder
ARG JAR_FILE=naikan-server/target/naikan-server.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Runtime stage
FROM eclipse-temurin:23-alpine
ARG USER=naikan

RUN set -eux; \
	apk update; \
	apk add --no-cache fontconfig ttf-dejavu tzdata; \
    rm -rf /var/cache/apk/*; \
    cp /usr/share/zoneinfo/UTC /etc/localtime; \
    echo "UTC" > /etc/timezone; \
    adduser -D -s /bin/sh $USER

WORKDIR /home/$USER

COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

RUN chown -R $USER:$USER /home/$USER

USER $USER

VOLUME /tmp

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS $JAVA_TOOL_OPTIONS -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.launch.JarLauncher