# Use official Maven image with JDK 17
FROM maven:3.9.5-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Install necessary system dependencies for Playwright
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    ca-certificates \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libatspi2.0-0 \
    libcups2 \
    libdrm2 \
    libgtk-3-0 \
    libnspr4 \
    libnss3 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxrandr2 \
    libxss1 \
    libxtst6 \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Copy Maven configuration files
COPY pom.xml .
COPY src ./src

# Download dependencies
RUN mvn dependency:resolve

# Install Playwright browsers and dependencies
RUN mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"

# Set environment variables
ENV MAVEN_OPTS="-Xmx1024m"
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright

# Create directory for reports
RUN mkdir -p target/cucumber-reports

# Default command
CMD ["mvn", "clean", "test"]
