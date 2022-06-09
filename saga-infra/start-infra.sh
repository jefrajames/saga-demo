#! /bin/zsh

clear

echo "Starting Eventuate Tram demo infrastructure"

docker container prune -f
docker volume prune -f
docker network prune -f

docker compose up
