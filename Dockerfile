FROM node:8

COPY . /app

WORKDIR /app/react-ui

RUN npm install && \
 npm install --save react-scripts

EXPOSE 3000

CMD npm start

