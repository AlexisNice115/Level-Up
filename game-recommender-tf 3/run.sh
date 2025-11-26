#!/bin/bash

# Game Recommender TensorFlow - Build and Run Script

echo "========================================="
echo "Game Recommender - TensorFlow Edition"
echo "========================================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed."
    echo "Please install Maven first:"
    echo "  - macOS: brew install maven"
    echo "  - Ubuntu: sudo apt install maven"
    echo "  - Windows: Download from https://maven.apache.org/download.cgi"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed."
    echo "Please install JDK 17+:"
    echo "  - macOS: brew install openjdk@17"
    echo "  - Ubuntu: sudo apt install openjdk-17-jdk"
    exit 1
fi

echo ""
echo "Step 1: Cleaning and compiling..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed!"
    exit 1
fi

echo "Step 2: Running with dependencies..."
echo ""
mvn exec:java -Dexec.mainClass="com.gamerecommender.Main" -q

echo ""
echo "========================================="
echo "Done!"
echo "========================================="
