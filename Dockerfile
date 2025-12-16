# --- 1. ビルドステージ (Builder) ---
FROM amazoncorretto:21-alpine-jdk AS builder
WORKDIR /app
COPY . .
# Gradle Wrapperに実行権限を付与してビルド (テストはスキップして高速化)
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

# --- 2. 実行ステージ (Runner) ---
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
# ビルドステージから成果物(JAR)だけをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]