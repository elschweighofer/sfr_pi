FROM node:lts-alpine
ENV NODE_ENV=production
WORKDIR /usr/src/app
COPY ./package.json  ./
RUN ls -la
RUN npm install --production
COPY . .
EXPOSE 3000
CMD ["node", "app.js"]
