#!/bin/bash
# 루트 디렉토리에서 실행

echo "🧼 운영 컨테이너 정리 중..."
docker compose --env-file .env.prod -f docker-compose.prod.yaml down --remove-orphans

echo "🚀 운영 컨테이너 실행 중..."
docker compose --env-file .env.prod -f docker-compose.prod.yaml up --build -d

echo "✅ 운영 환경 실행 완료!"