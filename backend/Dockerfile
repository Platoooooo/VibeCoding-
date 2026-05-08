FROM openjdk:17-slim

# 设置工作目录
WORKDIR /app

# 复制 jar 包
COPY target/wxzz-prod-1.0.0.jar app.jar

# 创建上传目录
RUN mkdir -p uploads

# 暴露端口
EXPOSE 8080

# 设置环境变量
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SERVER_PORT=8080

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=$SERVER_PORT"]
