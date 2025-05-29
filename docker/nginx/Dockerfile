# docker/nginx/Dockerfile

FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/nginx.conf

RUN echo "✅ nginx.conf 덮어쓰기" && ls -l /etc/nginx/conf.d/