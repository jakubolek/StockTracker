FROM node:20.8-bookworm-slim
WORKDIR /app
COPY package*.json ./
COPY tsconfig.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
