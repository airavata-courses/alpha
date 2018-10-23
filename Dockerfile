FROM node:8

COPY . /home/ubuntu/ms-stocks

WORKDIR /home/ubuntu/ms-stocks/stock-ms

COPY package*.json ./

EXPOSE 8000

CMD node server.js

