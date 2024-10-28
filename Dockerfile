# Sử dụng image Java từ Docker Hub
FROM azul/zulu-openjdk:17-latest

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép tệp JAR vào Docker image
COPY target/zizi-app-0.0.1-SNAPSHOT.jar app.jar

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]