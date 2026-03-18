# Deployment Guide for The Student Archive

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 14+ installed and running
- OAuth credentials from Google and GitHub

## Step 1: Database Setup

1. Install PostgreSQL if not already installed:
```bash
# Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib

# macOS
brew install postgresql
```

2. Start PostgreSQL service:
```bash
# Ubuntu/Debian
sudo service postgresql start

# macOS
brew services start postgresql
```

3. Create the database:
```bash
sudo -u postgres psql
CREATE DATABASE student_archive;
CREATE USER archive_user WITH ENCRYPTED PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE student_archive TO archive_user;
\q
```

## Step 2: OAuth Configuration

### Google OAuth Setup
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable Google+ API
4. Go to "Credentials" and create OAuth 2.0 Client ID
5. Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
6. Copy Client ID and Client Secret

### GitHub OAuth Setup
1. Go to GitHub Settings > Developer settings > OAuth Apps
2. Click "New OAuth App"
3. Fill in details:
   - Application name: The Student Archive
   - Homepage URL: http://localhost:8080
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Copy Client ID and Client Secret

## Step 3: Application Configuration

1. Copy the template configuration:
```bash
cp application.properties.template src/main/resources/application.properties
```

2. Edit `src/main/resources/application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/student_archive
spring.datasource.username=archive_user
spring.datasource.password=your_password

# Google OAuth
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET

# GitHub OAuth
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
```

## Step 4: Build the Application

```bash
mvn clean install
```

## Step 5: Run the Application

```bash
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080`

## Step 6: First Login

1. Navigate to `http://localhost:8080`
2. Click "Login with Google" or "Login with GitHub"
3. Complete OAuth authorization
4. Go to Profile and set your role (Student/Teacher/Admin)

## Production Deployment

### Using JAR
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/student-archive-1.0.0.jar
```

### Using Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/student-archive-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t student-archive .
docker run -p 8080:8080 student-archive
```

### Environment Variables (Production)
For production, use environment variables instead of properties file:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/student_archive
export SPRING_DATASOURCE_USERNAME=archive_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running: `sudo service postgresql status`
- Check database exists: `psql -U postgres -l`
- Verify connection string in application.properties

### OAuth Login Issues
- Verify redirect URIs match exactly (including protocol and port)
- Check OAuth credentials are correct
- Ensure OAuth apps are not in development mode restrictions

### File Upload Issues
- Check disk space available
- Verify file size is under 10MB
- Ensure PDF is not corrupted

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

## Monitoring and Logs

Logs are available in the console output. For production:
```properties
logging.file.name=logs/application.log
logging.level.com.college.archive=INFO
```

## Backup and Maintenance

### Database Backup
```bash
pg_dump -U archive_user student_archive > backup.sql
```

### Database Restore
```bash
psql -U archive_user student_archive < backup.sql
```

## Security Best Practices

1. Change default passwords
2. Use HTTPS in production
3. Keep OAuth secrets secure
4. Regular database backups
5. Monitor application logs
6. Keep dependencies updated
