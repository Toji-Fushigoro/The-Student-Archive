# API Documentation

## Overview
The Student Archive provides a web-based interface for searching and managing college study materials.

## Authentication
The application uses OAuth 2.0 for authentication with Google and GitHub providers.

### Login Endpoints
- `GET /login` - Display login page
- `GET /oauth2/authorization/google` - Initiate Google OAuth flow
- `GET /oauth2/authorization/github` - Initiate GitHub OAuth flow
- `POST /logout` - Logout current user

## Web Routes

### Public Routes
- `GET /` - Landing page (public)
- `GET /login` - Login page

### Protected Routes (Requires Authentication)
- `GET /home` - Dashboard/home page
- `GET /search` - Search page with results
- `GET /profile` - User profile page
- `POST /profile/update-role` - Update user role

### Upload Routes (Teachers & Admins Only)
- `GET /documents/upload` - Upload form page
- `POST /documents/upload` - Handle file upload

### Download Routes (All Authenticated Users)
- `GET /documents/download/{id}` - Download PDF by ID

### Admin Routes (Admins Only)
- `POST /documents/delete/{id}` - Delete document

## Request/Response Examples

### Search
**Request:**
```
GET /search?query=data+structures&page=0&size=20
```

**Response:** HTML page with search results

### Upload Document
**Request:**
```
POST /documents/upload
Content-Type: multipart/form-data

file: [PDF file]
title: "Data Structures Mid-Term 2023"
description: "Previous year question paper"
category: "PREVIOUS_YEAR_QUESTIONS"
subject: "Data Structures"
year: 2023
semester: 3
```

**Response:** Redirect to home with success message

### Download Document
**Request:**
```
GET /documents/download/123
```

**Response:**
```
Content-Type: application/pdf
Content-Disposition: attachment; filename="data-structures.pdf"
[PDF binary data]
```

## Data Models

### User
```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "role": "STUDENT",
  "oauthProvider": "google",
  "createdAt": "2023-01-01T00:00:00"
}
```

### Document
```json
{
  "id": 1,
  "title": "Data Structures Notes",
  "description": "Complete course notes",
  "category": "NOTES",
  "subject": "Data Structures",
  "year": 2023,
  "semester": 3,
  "filename": "ds-notes.pdf",
  "fileSize": 1024000,
  "uploadedBy": { "name": "Prof. Smith" },
  "createdAt": "2023-01-01T00:00:00"
}
```

### Search Result
```json
{
  "id": 1,
  "title": "Data Structures Notes",
  "description": "Complete course notes",
  "category": "NOTES",
  "subject": "Data Structures",
  "year": 2023,
  "semester": 3,
  "filename": "ds-notes.pdf",
  "fileSize": 1024000,
  "uploaderName": "Prof. Smith",
  "relevanceScore": 0.85,
  "createdAt": "2023-01-01T00:00:00"
}
```

## Search Algorithm

The search engine uses NLP tokenization with the following steps:

1. **Query Tokenization:** User query is tokenized using OpenNLP
2. **Stop Word Removal:** Common words (a, the, is, etc.) are filtered
3. **Normalization:** Tokens are converted to lowercase, special characters removed
4. **Matching:** Tokens are matched against indexed PDF content
5. **Relevance Scoring:** Documents are scored based on:
   - Number of matching tokens / total query tokens
   - Title matches get 1.5x boost
   - Results sorted by relevance score (highest first)

## Document Categories
- `PREVIOUS_YEAR_QUESTIONS` - Past exam papers
- `NOTES` - Lecture notes and study notes
- `STUDY_MATERIAL` - Additional learning resources
- `OTHER` - Miscellaneous documents

## User Roles
- `STUDENT` - Can search and download documents
- `TEACHER` - Can search, download, and upload documents
- `ADMIN` - Full access (can also delete documents)

## Error Handling

The application provides user-friendly error pages for:
- File upload errors (size limit, invalid format)
- Authentication errors
- Document not found
- Unauthorized access
- General server errors

## Rate Limits
Currently no rate limits implemented. Consider adding for production:
- Search: 100 requests per minute per user
- Upload: 10 requests per hour per user
- Download: 50 requests per hour per user

## Security Features
- OAuth 2.0 authentication
- Role-based access control (RBAC)
- CSRF protection
- Secure file download
- File type validation
- File size limits (10MB max)
