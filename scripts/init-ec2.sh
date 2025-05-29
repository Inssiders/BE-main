#!/bin/bash

echo "🚀 EC2 초기 설정을 시작합니다..."

# 1. 시스템 패키지 업데이트
echo "🔄 시스템 패키지 업데이트 중..."
sudo apt update && sudo apt upgrade -y

# 2. Docker 설치
echo "🐳 Docker 설치 중..."
sudo apt install -y ca-certificates curl gnupg lsb-release
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
  sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 3. Docker Compose v2 확인
echo "✅ Docker Compose 버전:"
docker compose version

# 4. 현재 사용자도 Docker 그룹에 추가 (재접속 필요)
echo "👤 현재 사용자를 Docker 그룹에 추가 중..."
sudo usermod -aG docker $USER

# 5. Docker 서비스 시작 및 부팅 시 자동 실행 설정
sudo systemctl start docker
sudo systemctl enable docker

echo "🙌 EC2 초기화 완료! 셸을 다시 시작하거나 로그아웃 후 재접속하세요."