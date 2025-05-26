# Build stage
FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /workspace/app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:10.1-jdk17-temurin

# Install curl, download prebuilt sysdig binary (no kernel module)
RUN apt-get update && apt-get install -y curl && \
    curl -L -o /usr/local/bin/sysdig https://s3.amazonaws.com/download.draios.com/stable/sysdig && \
    chmod +x /usr/local/bin/sysdig && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR $CATALINA_HOME
COPY --from=builder /workspace/app/target/todolist-*.war webapps/ROOT.war
COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 8080
CMD ["/start.sh"]
