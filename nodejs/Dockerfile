FROM node:18
ENV NODE_ENV=production
WORKDIR /usr/src/app
COPY ./nodejs/package.json  ./
RUN ls -la
RUN npm install
COPY ./nodejs/. .
EXPOSE 3000
CMD ["node", "app.js"]
