# syntax=docker/dockerfile:1

FROM node:16-alpine3.14

ENV NPM_CONFIG_LOGLEVEL=info \
    NODE_ENV=development \
    MONGODB_ADDON_URI=mongodb://localhost/admin \
    PORT=8080

COPY ["package.json", "package-lock.json*", "./"]

RUN npm install

COPY . .

EXPOSE 8080

ENTRYPOINT npm start