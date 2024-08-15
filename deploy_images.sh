#!/bin/bash

USERNAME=jakubolek

echo "Building backend Docker image..."
docker build -t $USERNAME/stocktracker-api:latest ./backend

echo "Building frontend Docker image..."
docker build -t $USERNAME/stocktracker-app:latest ./frontend

echo "Docker images built successfully."

docker push $USERNAME/stocktracker-api:latest
docker push $USERNAME/stocktracker-app:latest
echo "Docker images deploy successfully."
