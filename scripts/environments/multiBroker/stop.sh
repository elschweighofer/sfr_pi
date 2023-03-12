#!/bin/bash
docker-compose down || true
docker stop $(docker ps -aq) || true
docker rm $(docker ps -aq) || true
