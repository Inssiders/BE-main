# syntax=docker/dockerfile:latest
# check=experimental=all

# base 공통 베이스 이미지 및 유틸리티 설치
FROM bellsoft/liberica-openjdk-alpine:21 AS base
LABEL maintainer="ooMia"
RUN apk add --no-cache dos2unix

# builder Gradle 빌드 및 의존성 캐시 활용
FROM base AS builder
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
COPY .java-version .java-version

ENV GRADLE_USER_HOME=/gradle-cache
RUN dos2unix gradlew

COPY . .
RUN ./gradlew bootJar -x test --no-daemon

# runner 실행용 경량 이미지 생성
FROM bellsoft/liberica-openjre-alpine:21 AS runner
WORKDIR /app

# wget 설치
RUN apk add --no-cache wget

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD ["sh", "-c", "wget -qO- http://localhost:8080/actuator/health || exit 1"]
ENTRYPOINT ["java", "-jar", "app.jar"]
