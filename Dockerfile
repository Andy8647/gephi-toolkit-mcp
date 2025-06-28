# Multi-stage build for Gephi Toolkit MCP Server
FROM openjdk:11-jdk-slim as java-builder

# Install Maven
RUN apt-update && apt-get install -y maven

# Copy Java source and build
WORKDIR /app/java
COPY java/ .
RUN mvn clean compile

# Node.js stage
FROM node:18-slim

# Install Java runtime for Gephi Toolkit
RUN apt-get update && apt-get install -y openjdk-11-jre-headless && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install Node.js dependencies
RUN npm ci --only=production

# Copy source code
COPY src/ ./src/

# Copy compiled Java classes from builder stage
COPY --from=java-builder /app/java/target/ ./java/target/

# Make sure the entrypoint is executable
RUN chmod +x src/index.js

# Set environment variables
ENV NODE_ENV=production
ENV PATH="/usr/lib/jvm/java-11-openjdk-amd64/bin:$PATH"

# Expose stdio for MCP
ENTRYPOINT ["node", "src/index.js"]