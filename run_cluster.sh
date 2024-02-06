#!/bin/bash
./mvnw clean package
docker build -t itblueprints/system-agent-sample:latest .
docker-compose up --no-build
