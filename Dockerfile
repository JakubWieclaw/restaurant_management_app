FROM eclipse-temurin:17
WORKDIR /app
COPY app/build/outputs/apk/debug/app-debug.apk app.apk
CMD ["java", "-jar", "app.apk"]
