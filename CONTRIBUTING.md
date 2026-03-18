# Contributing to The Student Archive

Thank you for considering contributing to The Student Archive!

## How to Contribute

### Reporting Bugs
1. Check if the bug has already been reported in Issues
2. Create a new issue with:
   - Clear title and description
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots if applicable
   - Your environment (OS, Java version, browser)

### Suggesting Features
1. Check if the feature has been suggested
2. Create an issue describing:
   - The problem it solves
   - Proposed solution
   - Alternative solutions considered

### Code Contributions

#### Setup Development Environment
1. Fork the repository
2. Clone your fork
3. Set up database and OAuth credentials (see README.md)
4. Create a feature branch: `git checkout -b feature/your-feature`

#### Coding Standards
- Follow Java naming conventions
- Use Lombok annotations where appropriate
- Write meaningful commit messages
- Add comments for complex logic
- Keep methods focused and small
- Write unit tests for new features

#### Testing
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=TokenizationServiceTest
```

#### Pull Request Process
1. Update README.md if needed
2. Ensure all tests pass
3. Update API.md for API changes
4. Create pull request with:
   - Clear description of changes
   - Reference to related issues
   - Screenshots for UI changes

#### Code Review
- Be responsive to feedback
- Make requested changes promptly
- Keep discussions professional

## Project Structure
```
src/main/java/com/college/archive/
├── config/          # Configuration classes
├── controller/      # MVC controllers
├── dto/             # Data transfer objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── repository/      # Data repositories
├── service/         # Business logic
└── util/            # Utility classes
```

## Development Guidelines

### Adding New Features
1. Discuss in an issue first
2. Create feature branch
3. Implement with tests
4. Update documentation
5. Submit pull request

### Database Changes
1. Update entity classes
2. Hibernate will handle schema updates
3. Document changes in migration notes

### Adding New OAuth Provider
1. Add dependency if needed
2. Configure in application.properties
3. Update CustomOAuth2UserService
4. Add login button in login.html

## Questions?
Open an issue with the "question" label.

## License
By contributing, you agree that your contributions will be licensed under the MIT License.
