# Build stage
FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /workspace/app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:10.1-jdk17-temurin
WORKDIR $CATALINA_HOME
COPY --from=builder /workspace/app/target/todolist-*.war webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]