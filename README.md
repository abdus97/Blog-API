# Blog API Application

## Overview
The Blog API is a powerful Spring Boot application designed to empower users to create, manage, and publish their blog posts efficiently. This API goes beyond standard blogging functionalities by integrating seamlessly with GitHub, allowing users to associate their repositories and issues with blog posts. With robust user authentication, flexible post management, and comprehensive comment management, the Blog API provides an excellent platform for both writers and readers.

## Key Features

### User Authentication and Authorization
- **JWT Authentication**: Secure user login and access control using JSON Web Tokens.
- **Role-based Authorization**: Implement roles such as Author and Reader to manage user permissions.

### Blog Management
#### Post Management
- Create, read, update, and delete blog posts with ease.
- Save posts as drafts for later publishing.
- Publish posts to make them visible to readers.

#### Comment Management
- Add, update, delete, and retrieve comments on blog posts.
- Support for nested comments to facilitate threaded discussions.

### GitHub Integration
- Fetch user repositories using GitHub’s API for better content management.
- Associate blog posts with GitHub repositories to enhance context.
- Fetch and manage GitHub issues, allowing users to create blog posts directly from issue descriptions.

## Technologies Used

### Backend
- **Spring Boot**: The core framework for building the application.
- **Spring Security**: For implementing robust authentication and authorization.
- **Spring Data JPA**: For seamless database interactions.
- **Hibernate**: The ORM framework for managing database entities.
- **PostgreSQL**: The chosen database for storing application data.

### GitHub API
- **Feign Client**: For smooth interaction with GitHub’s REST API, enabling easy data retrieval.


## Setup and Installation

### Prerequisites
- **Java 17 or later**: Ensure you have the required Java version installed.
- **Docker (optional)**: For containerized PostgreSQL setup.
- **GitHub Developer Account**: You will need to register an OAuth application to enable GitHub authentication.

### Steps to Set Up

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/abdus97/Blog-API.git
    cd Blog-API
    ```

2. **Configure Database:**
    - Create a PostgreSQL database (e.g., `blogapi`).
    - Update the `application.yml` file with your database credentials.

3. **Set Up GitHub OAuth:**
    - Register an OAuth app on GitHub (under your account settings).
    - Update the `application.properties` with your `clientId` and `clientSecret` values.

4. **Build and Run the Application:**
    ```bash
    1. Maven clean
    2. Maven Build
    3. Modify Run Configuration and set the 
              jwt.secret=***** 
        environment variable.
    ```

5. **Access the Application:**
    - **Base URL**: `http://localhost:8080`
    - **API Documentation (Swagger)**: If enabled, access it at `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication
- **POST /auth/login**: Authenticate user and retrieve JWT.
- **POST /auth/register**: Register a new user.

### Blog Post Management
- **POST /posts**: Create a new post.
- **GET /posts/{id}**: Retrieve a single post.
- **PUT /posts/{id}**: Update an existing post.
- **DELETE /posts/{id}**: Delete a post.

### GitHub Integration
- **GET /github/repos**: Fetch the user’s GitHub repositories.
- **GET /github/repos/{repoId}/issues**: Fetch issues from a specific repository.

### Comments
- **POST /posts/{postId}/comments**: Add a comment to a post.
- **GET /posts/{postId}/comments**: Retrieve comments for a specific post.

## How to Contribute

1. Fork the repository.
2. Create a new feature branch:
    ```bash
    git checkout -b feature/your-feature
    ```
3. Commit your changes:
    ```bash
    git commit -m "Add your changes"
    ```
4. Push your changes to your fork:
    ```bash
    git push origin feature/your-feature
    ```
5. Create a pull request for review.
