# 🎬 FilmPatro
[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![TDD](https://img.shields.io/badge/Test_Driven_Development-✓-blueviolet)](https://en.wikipedia.org/wiki/Test-driven_development)

**FilmPatro** is an online platform for discovering, rating, and reviewing Nepali movies. Users can create accounts, log in via social media, share feedback, and explore community perspectives on Nepali cinema.

---

## ✨ Features
- **Movie Discovery**: Browse Nepali film database
- **Ratings & Reviews**: Submit and read movie evaluations
- **Social Login**: Register/sign-in via social platforms
- **Security**: Robust protection mechanisms
- **Email Notifications**: User alerts and updates
- **Version Auditing**: Track changes with Hibernate Envers
- **Test-Driven Development**: Comprehensive test coverage

---

## 🛠 Tech Stack
| Component             | Technology                           |
|-----------------------|--------------------------------------|
| **Backend**           | Java 21, Spring Boot 3.5.0           |
| **Database**          | MySQL                                |
| **Authentication**    | OAuth2 + JWT                         |
| **ORM**               | Hibernate with Envers                |
| **Security**          | Spring Security                      |
| **Email**             | Spring Boot Mail                     |
| **Testing**           | JUnit 5, Mockito, Spring Test        |
| **Build Tool**        | Maven                                |

---

## Prerequisites

Ensure the following tools are installed on your system:

- **Java Development Kit (JDK)**: Version 21 or later
- **MySQL Server**: Ensure a running MySQL instance
- **Maven**: To manage dependencies and build the project

---

## 🚀 Getting Started

### 1. Clone Repository
```bash
git clone https://github.com/DeepDhamala/filmpatro.git
cd filmpatro
```

### 2. Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE filmpatro;
```

### Sample `application.properties` configuration

```properties
spring.datasource.url=jdbc:mysql://<your-database-url>:3306/<your-database-name>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build & Run the Project

To build the project and start the server, run:
```shell
  mvn clean install
  mvn spring-boot:run
```

The application should now be accessible at `http://localhost:8080`.

---

## Dependencies

Key dependencies used in this project include:

- **Spring Boot Starters**:
    - Web
    - Data JPA
    - Security
    - Validation
    - OAuth2 Client
- **MySQL Driver**
- **JWT: JJWT (API, Impl, Jackson)**
- **Hibernate Envers**
- **Spring Mail**
- **Lombok**

---

## Development Notes

- **Testing**: Includes Spring Boot Test and Spring Security Test for unit and integration testing.
- **DevTools**: Integrated Spring Boot DevTools for simplified development.
- **Docker Compose**: Included dependencies support Docker deployment for easier scalability.

---

## Contribution

Contributions are welcome! Follow the steps below to contribute:

1. Fork the project.
2. Create your feature branch:
   ```bash
   git checkout -b feature/YourFeature
   ```
3. Commit your changes:
   ```bash
   git commit -m 'Add some feature'
   ```
4. Push your branch to the repository:
   ```bash
   git push origin feature/YourFeature
   ```
5. Open a pull request.

---

## License

This project does not currently define a specific license. Please contact the developer(s) for clarification or licensing queries.

---

## Contact

For any inquiries, support, or suggestions, feel free to open an issue on the repository or contact the developer.


