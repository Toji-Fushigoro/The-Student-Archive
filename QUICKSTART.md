# Quick Start Guide

Get The Student Archive up and running in 10 minutes!

## Prerequisites Checklist
- [ ] Java 17 installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] PostgreSQL installed and running
- [ ] Google OAuth credentials
- [ ] GitHub OAuth credentials

## Step 1: Database Setup (2 minutes)
```bash
# Login to PostgreSQL
sudo -u postgres psql

# Create database and user
CREATE DATABASE student_archive;
CREATE USER archive_user WITH ENCRYPTED PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE student_archive TO archive_user;
\q
```

## Step 2: Get OAuth Credentials (5 minutes)

### Google OAuth
1. Visit: https://console.cloud.google.com/
2. Create project → APIs & Services → Credentials
3. Create OAuth 2.0 Client ID
4. Add redirect URI: `http://localhost:8080/login/oauth2/code/google`
5. Copy Client ID and Secret

### GitHub OAuth
1. Visit: https://github.com/settings/developers
2. New OAuth App
3. Callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Copy Client ID and Secret

## Step 3: Configure Application (1 minute)
Edit `src/main/resources/application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/student_archive
spring.datasource.username=archive_user
spring.datasource.password=yourpassword

# Google
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET

# GitHub
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
```

## Step 4: Build & Run (2 minutes)
```bash
# Navigate to project directory
cd The-Student-Archive

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

## Step 5: Access & Test
1. Open browser: http://localhost:8080
2. Click "Login with Google" or "Login with GitHub"
3. Authorize the application
4. Go to Profile → Set your role (Student/Teacher/Admin)
5. Try searching or uploading a PDF!

## Troubleshooting

### Build Fails
- Check Java version: `java -version` (needs 17+)
- Check Maven version: `mvn -version`
- Clean and rebuild: `mvn clean install -U`

### Database Connection Error
- Check PostgreSQL is running: `sudo service postgresql status`
- Verify credentials in application.properties
- Test connection: `psql -U archive_user -d student_archive`

### OAuth Login Fails
- Verify redirect URIs match exactly
- Check client ID and secret are correct
- Ensure OAuth app is active (not in restricted mode)

### Port 8080 Already in Use
Change port in application.properties:
```properties
server.port=8081
```

## What's Next?

**As a Student:**
- Search for study materials
- Download PDFs
- View your search history

**As a Teacher:**
- Upload new PDFs
- Add metadata for better searchability
- Help build the archive

**As an Admin:**
- Manage all documents
- Delete inappropriate content
- Monitor usage

## Tips for Best Results

1. **Uploading PDFs:**
   - Use descriptive titles
   - Fill in all metadata fields
   - Keep files under 10MB

2. **Searching:**
   - Use specific keywords
   - Try different terms if no results
   - Check spelling

3. **First Time Setup:**
   - Set your role immediately after login
   - Test upload functionality
   - Try a few searches to see results

## Need Help?
- Check DEPLOYMENT.md for detailed instructions
- Read API.md for technical documentation
- Open an issue on GitHub

---

🎉 **Congratulations!** Your Student Archive is ready to use!
