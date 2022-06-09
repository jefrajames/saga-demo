#! /bin/zsh

clear

echo "Cleaning Eventuate Tram demo infrastructure"

docker container prune -f
docker volume prune -f
docker network prune -f
