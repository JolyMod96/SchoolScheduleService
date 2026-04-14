# School Schedule Service

[![Java Version](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6-blue.svg)](https://spring.io/projects/spring-security)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive educational management system designed to streamline school scheduling, user management, and administrative workflows. This project was developed as a graduation work at **IT STEP Academy**. 

> **Note:** I successfully defended this project on **September 15, 2023**, at the age of **16**.

## 🚀 Key Features

* **Role-Based Access Control (RBAC):** Secure authentication and authorization for Admins, Teachers, and Students using **Spring Security 6**.
* **Dynamic Scheduling:** Automated and manual management of school timetables, subjects, and groups.
* **User Management:** Specialized dashboards for administrators to manage student and teacher profiles.
* **Persistence Layer:** Robust data handling with **Spring Data JPA** and support for both **MariaDB** (production) and **H2** (development/testing).
* **Modern UI:** Responsive frontend built with **Thymeleaf** templates and integrated security tags for conditional rendering.

## 🛠 Tech Stack

| Technology | Usage |
| :--- | :--- |
| **Java 17** | Core programming language |
| **Spring Boot 3.1.1** | Application framework |
| **Spring Security 6** | Authentication and web-layer security |
| **Spring Data JPA** | ORM and Database communication |
| **Thymeleaf** | Server-side templating engine |
| **MariaDB / H2** | Relational databases |
| **Lombok** | Boilerplate code reduction |
| **SonarCloud** | Static code analysis and quality gate |

## 🏗 Project Structure

The project follows a classic **Layered Architecture (MVC)**:
* `controller/` — Web endpoints and request handling.
* `service/` — Business logic and orchestration.
* `repository/` — Data access layer (Spring Data JPA).
* `entity/` — Database models and domain objects.
* `security/` — Security configurations and custom `UserDetails`.
* `resources/templates/` — UI views and fragments (Thymeleaf).

## 🚦 Getting Started

### Prerequisites
* **JDK 17**
* **Maven 3.6+**
* **MariaDB** (optional, H2 in-memory database is available for a quick start)

### Installation & Run
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/JolyMod96/SchoolScheduleService.git](https://github.com/JolyMod96/SchoolScheduleService.git)
    ```
2.  **Build the project:**
    ```bash
    mvn clean install
    ```
3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

## 🤝 Credits & Acknowledgments

* **Developer:** Kiril Zahorskyi
* **Mentor:** Special thanks to **Maksym Shaptala** for his guidance and technical support during the development and defense of this project.
