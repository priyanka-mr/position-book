FROM node:18-alpine AS builder
WORKDIR /positionbook-ui
COPY package.json package-lock.json ./
RUN npm install --frozen-lockfile
COPY . .
RUN npm run build
FROM nginx:alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*

COPY --from=builder /positionbook-ui/build ./
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
