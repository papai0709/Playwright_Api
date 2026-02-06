#!/bin/bash

# Playwright API Testing - Local Test Runner
# This script helps run tests locally with different configurations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTION]"
    echo ""
    echo "Options:"
    echo "  sanity      Run sanity tests only"
    echo "  smoke       Run smoke tests only"
    echo "  regression  Run full regression suite"
    echo "  all         Run all tests"
    echo "  docker      Run tests in Docker container"
    echo "  security    Run security tests"
    echo "  clean       Clean target directory"
    echo "  install     Install Playwright dependencies"
    echo "  help        Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 sanity"
    echo "  $0 docker"
    echo "  $0 regression"
}

# Function to install Playwright
install_playwright() {
    print_info "Installing Playwright dependencies..."
    mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
    print_success "Playwright dependencies installed successfully"
}

# Function to clean target directory
clean_target() {
    print_info "Cleaning target directory..."
    mvn clean
    print_success "Target directory cleaned"
}

# Function to run tests with specific tags
run_tests() {
    local tags=$1
    local description=$2

    print_info "Running $description..."
    print_info "Tags: $tags"

    mvn test -Dtest=**/CucumberTestRunner -Dcucumber.filter.tags="$tags"

    if [ $? -eq 0 ]; then
        print_success "$description completed successfully"
        print_info "Reports available at: target/cucumber-reports/"
    else
        print_error "$description failed"
        exit 1
    fi
}

# Function to generate reports
generate_reports() {
    print_info "Generating comprehensive reports..."
    mvn verify
    print_success "Reports generated successfully"
}

# Function to run Docker tests
run_docker_tests() {
    print_info "Building Docker image..."
    docker build -t playwright-api-tests .

    print_info "Running Playwright tests in Docker container..."
    docker run --rm \
        -v "$(pwd)/target:/app/target" \
        playwright-api-tests \
        mvn test -Dtest=**/CucumberTestRunner -Dcucumber.filter.tags="@sanity"

    print_success "Docker Playwright tests completed"
}

# Function to run security tests
run_security_tests() {
    print_info "Running security analysis..."

    print_info "Running security-tagged Playwright tests..."
    mvn test -Dtest=**/CucumberTestRunner -Dcucumber.filter.tags="@security"

    print_success "Security tests completed"
}

# Main script logic
case "$1" in
    sanity)
        run_tests "@sanity" "Sanity Tests"
        generate_reports
        ;;
    smoke)
        run_tests "@smoke" "Smoke Tests"
        generate_reports
        ;;
    regression)
        run_tests "@regression or @sanity or @smoke" "Full Regression Suite"
        generate_reports
        ;;
    all)
        run_tests "" "All Tests"
        generate_reports
        ;;
    docker)
        run_docker_tests
        ;;
    security)
        run_security_tests
        ;;
    clean)
        clean_target
        ;;
    install)
        install_playwright
        ;;
    help|--help|-h)
        show_usage
        ;;
    "")
        print_error "No option provided"
        show_usage
        exit 1
        ;;
    *)
        print_error "Invalid option: $1"
        show_usage
        exit 1
        ;;
esac

print_success "Script execution completed!"
