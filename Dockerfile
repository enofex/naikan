FROM eclipse-temurin:20-alpine AS builder
ARG JAR_FILE=naikan-web/target/naikan-web.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:20-alpine
RUN set -eux; \
	apk update; \
	apk add --no-cache fontconfig; \
	apk add --no-cache ttf-dejavu; \
    rm -rf /var/cache/apk/*; \
    apk add tzdata; \
    cp /usr/share/zoneinfo/UTC /etc/localtime; \
    echo "UTC" > /etc/timezone;
VOLUME /tmp
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS $EXTRA_JAVA_OPTIONS -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher