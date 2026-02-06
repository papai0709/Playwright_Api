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

# Copy Maven configuration files first for better Docker layer caching
COPY pom.xml .

# Download dependencies first (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:resolve dependency:resolve-sources

# Install Playwright browsers and dependencies
RUN mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"

# Copy source code
COPY src ./src

# Set environment variables for Playwright
ENV MAVEN_OPTS="-Xmx1024m"
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
ENV PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1

# Create directory for reports
RUN mkdir -p target/cucumber-reports

# Run Playwright tests by default
CMD ["mvn", "test", "-Dtest=**/CucumberTestRunner"]
