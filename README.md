# The Student Archive

A college archive system for storing and searching PDF files (previous year questions, notes, study materials) with NLP-powered search.

## Features
- **OAuth 2.0 Authentication**: Login via Google or GitHub
- **Role-Based Access**: Student, Teacher, and Admin roles
- **PDF Management**: Upload (Teachers/Admins), search, and download PDFs
- **NLP-Powered Search**: Apache OpenNLP tokenization for intelligent search
- **Web Interface**: Clean, responsive interface using Thymeleaf and Bootstrap

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- PostgreSQL
- Apache OpenNLP
- Apache PDFBox
- Thymeleaf
- Bootstrap

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 14+
- Google OAuth credentials (for Google login)
- GitHub OAuth credentials (for GitHub login)

## Setup Instructions

### 1. Database Setup
```sql
CREATE DATABASE student_archive;
```

### 2. OAuth Configuration

#### Google OAuth:
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
6. Copy Client ID and Client Secret

#### GitHub OAuth:
1. Go to GitHub Settings > Developer settings > OAuth Apps
2. Create a new OAuth App
3. Set Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Copy Client ID and Client Secret

### 3. Application Configuration
Edit `src/main/resources/application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/student_archive
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# Google OAuth
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET

# GitHub OAuth
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
```

### 4. Download OpenNLP Models
Download the tokenizer model and place it in `src/main/resources/nlp-models/`:
```bash
wget https://dlcdn.apache.org/opennlp/models/langdetect/1.8.3/langdetect-183.bin -P src/main/resources/nlp-models/
wget https://dlcdn.apache.org/opennlp/models/ud-models-1.0/opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin -P src/main/resources/nlp-models/
```

### 5. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080`

## User Roles
- **STUDENT**: Can search and download PDFs
- **TEACHER**: Can search, download, and upload PDFs
- **ADMIN**: Full access (search, download, upload, delete, manage users)

## Usage
1. Navigate to `http://localhost:8080`
2. Click "Login with Google" or "Login with GitHub"
3. Complete OAuth authorization
4. Set your role (first-time login)
5. Start searching or uploading PDFs

## Project Structure
```
src/
├── main/
│   ├── java/com/college/archive/
│   │   ├── config/          # Security and app configuration
│   │   ├── controller/      # MVC controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic
│   │   ├── dto/             # Data transfer objects
│   │   ├── exception/       # Custom exceptions
│   │   └── util/            # Utility classes
│   └── resources/
│       ├── templates/       # Thymeleaf HTML templates
│       ├── static/          # CSS, JS, images
│       └── nlp-models/      # OpenNLP models
└── test/                    # Unit and integration tests
```

## License
MIT License

## Contributing
This is a college project. Contributions are welcome!