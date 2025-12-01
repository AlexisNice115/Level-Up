@echo off
echo =========================================
echo Game Recommender - TensorFlow Edition
echo =========================================
echo.

echo Step 1: Cleaning and compiling...
call mvn clean compile -q

if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Step 2: Running with dependencies...
echo.
call mvn exec:java -Dexec.mainClass="com.gamerecommender.Main"

echo.
echo =========================================
echo Done!
echo =========================================
pause
