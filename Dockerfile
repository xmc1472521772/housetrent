# 多阶段构建：第一阶段用于构建项目
FROM maven:3.8.6-openjdk-18-slim AS build

# 设置工作目录
WORKDIR /app

# 复制所有文件到容器中
COPY . .

# 调试：查看文件结构
RUN echo "=== 查看根目录文件 ===" && ls -la
RUN echo "=== 查看是否存在各个模块 ===" && \
    ls -la House-* || echo "未找到House-*模块" && \
    find . -name "pom.xml" -type f

# 下载依赖并构建项目（跳过测试以加快构建速度）
RUN mvn clean package -DskipTests

# 第二阶段：运行时镜像
FROM openjdk:18-jdk-slim

# 设置维护者信息
LABEL maintainer="your-email@example.com"

# 安装curl用于健康检查
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 创建非root用户（安全最佳实践）
RUN addgroup --system spring && adduser --system spring --ingroup spring

# 设置工作目录
WORKDIR /app

# 从构建阶段复制JAR文件
# 根据你的主启动模块调整路径，通常是House-Start模块
COPY --from=build /app/House-Start/target/*.jar app.jar

# 更改文件所有者
RUN chown spring:spring app.jar

# 切换到非root用户
USER spring:spring

# 暴露应用端口
EXPOSE 8080

# 设置JVM参数
ENV JAVA_OPTS="-Xmx1024m -Xms512m -Djava.security.egd=file:/dev/./urandom"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
