$USERNAME = "jakubolek"

Write-Host "Building backend Docker image..."
docker build -t "$USERNAME/stocktracker-api:latest" ./backend

Write-Host "Building frontend Docker image..."
docker build -t "$USERNAME/stocktracker-app:latest" ./frontend

Write-Host "Docker images built successfully."

docker push "$USERNAME/stocktracker-api:latest"
docker push "$USERNAME/stocktracker-app:latest"

Write-Host "Docker images deployed successfully."

