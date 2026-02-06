# GitHub Actions CI/CD Pipeline

This document describes the GitHub Actions CI/CD pipeline setup for the Playwright API Testing framework.

## 🚀 Workflows Overview

### 1. Main CI/CD Pipeline (`ci-cd.yml`)

**Triggers:**
- Push to `main`, `develop`, or `feature/**` branches
- Pull requests to `main` or `develop`
- Manual workflow dispatch

**Features:**
- **Matrix Testing**: Tests against Java 17 and 21
- **Smart Test Execution**:
  - Pull Requests: `@sanity` tests
  - Develop branch: `@smoke` tests  
  - Main branch: Full regression (`@regression`, `@sanity`, `@smoke`)
  - Manual trigger: Custom tags via input
- **Artifact Management**: Uploads test reports and coverage
- **GitHub Pages**: Deploys reports to GitHub Pages
- **Slack Notifications**: Success/failure notifications

### 2. Nightly Regression Tests (`nightly-regression.yml`)

**Triggers:**
- Scheduled at 2 AM UTC daily
- Manual trigger

**Features:**
- Full regression test suite
- Email notifications on failure
- 7-day report retention

### 3. Performance Testing (`performance-tests.yml`)

**Triggers:**
- Manual workflow dispatch with load level selection
- Scheduled weekly (Sundays at 3 AM)

**Features:**
- Configurable load levels (light, medium, heavy)
- Performance report generation
- 14-day report retention

### 4. Security Testing (`security-tests.yml`)

**Triggers:**
- Push/PR to main branch
- Scheduled daily at 1 AM UTC
- Manual trigger

**Features:**
- OWASP dependency check
- CodeQL security analysis
- Snyk vulnerability scanning
- API security tests

## 🏷️ Tag Strategy

| Tag | Purpose | When to Use |
|-----|---------|-------------|
| `@sanity` | Quick smoke tests for critical functionality | PR validation |
| `@smoke` | Basic functionality verification | Develop branch |
| `@regression` | Comprehensive test coverage | Main branch, nightly |
| `@test` | Legacy tag for existing scenarios | Backward compatibility |
| `@performance` | Performance and load testing | Performance workflow |
| `@security` | Security-focused test scenarios | Security workflow |

## 🔧 Configuration

### Environment Variables

Set these secrets in your GitHub repository:

```
SLACK_WEBHOOK           # Slack webhook URL for notifications
EMAIL_USERNAME          # SMTP username for email notifications
EMAIL_PASSWORD          # SMTP password for email notifications  
NOTIFICATION_EMAIL      # Email address for failure notifications
SNYK_TOKEN             # Snyk API token for security scanning
```

### Runner Configuration

The test runner supports dynamic tag filtering through system properties:

```java
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@sanity or @test")
```

### Maven Commands

Execute tests with specific tags:

```bash
# Run sanity tests
mvn clean test -Dcucumber.filter.tags="@sanity"

# Run multiple tag combinations  
mvn clean test -Dcucumber.filter.tags="@regression or @sanity"

# Run tests excluding certain tags
mvn clean test -Dcucumber.filter.tags="not @slow"
```

## 📊 Reports and Artifacts

### Cucumber Reports
- **HTML Report**: `target/cucumber-reports/report.html`
- **JSON Report**: `target/cucumber-reports/report.json`  
- **JUnit XML**: `target/cucumber-reports/report.xml`

### GitHub Pages
Reports are automatically deployed to GitHub Pages on main branch pushes:
- URL: `https://[username].github.io/[repository]/test-reports/`

### Artifact Retention
- Test reports: 30 days
- Nightly reports: 7 days
- Performance reports: 14 days
- Security reports: 30 days

## 🔄 Workflow Examples

### Pull Request Flow
1. Developer creates PR
2. `@sanity` tests run automatically
3. Reports uploaded as artifacts
4. Test results posted as PR comment
5. Slack notification sent

### Release Flow  
1. Code merged to main
2. Full regression suite runs
3. Reports deployed to GitHub Pages
4. Success notification sent
5. Security scans triggered

### Manual Testing
1. Go to Actions tab
2. Select "API Testing CI/CD Pipeline"  
3. Click "Run workflow"
4. Specify custom tags (e.g., `@user-management`)
5. Monitor execution and download reports

## 🛠️ Customization

### Adding New Tags
1. Add tag to feature files:
   ```gherkin
   @new-feature
   Scenario: Test new functionality
   ```

2. Update runner if needed:
   ```java
   @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@sanity or @new-feature")
   ```

3. Modify workflow tag filters as required

### Environment-Specific Testing
Update workflow inputs to support different environments:

```yaml
environment:
  description: 'Target environment'
  type: choice
  options:
    - dev
    - staging  
    - production
```

### Custom Notifications
Add additional notification channels (Teams, Discord, etc.) by extending the notification jobs.

## 🐛 Troubleshooting

### Common Issues

1. **Playwright Installation Fails**
   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
   ```

2. **Reports Not Generated**
   - Ensure `mvn verify` runs after tests
   - Check cucumber-reports directory exists

3. **Tag Filtering Not Working**
   - Verify tag syntax in feature files
   - Check system property configuration in POM

### Debug Mode
Enable verbose logging by adding to workflow:
```yaml
- name: Debug Test Execution
  run: |
    mvn clean test -Dcucumber.filter.tags="@debug" -X
```

## 📚 Additional Resources

- [Cucumber Documentation](https://cucumber.io/docs)
- [Playwright Java Documentation](https://playwright.dev/java/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
