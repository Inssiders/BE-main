# initialization

```sh
git clone https://github.com/Inssiders/BE-main.git
cd BE-main
cp .env.example .env

# https://sdkman.io/install/
sdk install java 21-tem
java --version

# https://github.com/diffplug/spotless/tree/main/plugin-gradle
gradlew spotlessApply
gradlew spotlessCheck

docker build --check .
```

# development

```sh
docker compose down --volumes --remove-orphans
docker compose up -d

# profile=dev 자동 초기화 및 멱등성 보장
gradlew clean build -x test
gradlew test -Dspring.profiles.active=dev -t
gradlew bootRun -Dspring.profiles.active=dev
# + IDE extension으로도 실행 가능

docker exec -it be-main-postgres-1 psql -U user -d dev
SELECT * FROM accounts;
SELECT * FROM user_profiles;
```

# stage

```sh
# export ddl and append on `schema.sql`
gradlew bootRun -Dspring.profiles.active=dev,stage

docker compose --profile stage up --wait
```

# production

```sh
# cold-start 상황에서 데이터베이스 초기화
# docker compose --profile prod down --volumes --remove-orphans
# SPRING_PROFILES_ACTIVE=stage docker compose --profile prod up --wait

# 이미지 최신화
docker compose --profile prod pull

docker compose --profile prod up -d

# certbot을 활용하여 로컬 nginx 구동
# /etc/nginx 경로 설정 수정하면 certbot 재설치
# sudo certbot --nginx
```
