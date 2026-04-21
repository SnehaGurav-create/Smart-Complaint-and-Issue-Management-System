# 🛠️ Smart Complaint & Issue Management System

A full-stack web application built with **Spring Boot** that streamlines the process of submitting, tracking, and resolving complaints or issues — with role-based access for Users and Admins, real-time notifications, and a powerful admin dashboard.

---

## 📌 Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

---

## 📋 About the Project

The **Smart Complaint & Issue Management System** is designed to bridge the gap between citizens/users and administrators by providing a structured platform to raise, manage, and resolve complaints efficiently. It supports multi-role access, complaint categorization, assignment workflows, and real-time status updates.

---

## ✨ Features

- 🔐 **Secure Authentication** — Registration & login with Spring Security
- 👥 **Role-Based Access Control** — Separate dashboards for `USER` and `ADMIN`
- 📝 **Complaint Submission** — Submit complaints with category selection
- 📂 **Complaint Tracking** — View complaint status (Pending → In Progress → Resolved)
- 👨‍💼 **Admin Dashboard** — View all complaints, assign to agents, update statuses
- 🔔 **Real-Time Notifications** — WebSocket (STOMP) based live notifications
- 📊 **Reports & Analytics** — Admin reports with complaint statistics
- 🗂️ **Category Management** — Classify complaints by category
- ⚠️ **Custom Error Handling** — Friendly error pages

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.4 |
| Security | Spring Security 6 |
| ORM | Spring Data JPA (Hibernate) |
| Templating | Thymeleaf + Layout Dialect |
| Real-time | WebSocket (STOMP) |
| Database | MySQL 8 |
| Build Tool | Maven |
| Utilities | Lombok, Spring DevTools |

---

## 📁 Project Structure

```
src/
└── main/
    ├── java/com/smartcomplaint/app/
    │   ├── config/         # Security, WebSocket, Async, DB config
    │   ├── controller/     # Auth, Admin, Complaint, Notification controllers
    │   ├── dto/            # Data Transfer Objects
    │   ├── enums/          # Role, ComplaintStatus enums
    │   ├── model/          # JPA Entities (User, Complaint, Assignment, etc.)
    │   ├── repository/     # Spring Data JPA Repositories
    │   └── service/        # Business logic layer
    └── resources/
        ├── templates/      # Thymeleaf HTML templates
        │   ├── admin/      # Admin views
        │   ├── user/       # User views
        │   ├── auth/       # Login & Register
        │   └── layout/     # Base layout
        ├── static/         # CSS & JS assets
        ├── schema.sql      # DB schema
        ├── data.sql        # Seed data
        └── application.properties
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [MySQL 8+](https://www.mysql.com/)
- [Git](https://git-scm.com/)

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/SnehaGurav-create/Smart-Complaint-and-Issue-Management-System.git
cd Smart-Complaint-and-Issue-Management-System
```

**2. Set up the database**

Create a MySQL database (the app can auto-create it):
```sql
CREATE DATABASE smart_complaint_db;
```

**3. Configure the application**

Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

**4. Run the application**
```bash
./mvnw spring-boot:run
```

**5. Open in browser**
```
http://localhost:8082
```

---

## ⚙️ Configuration

Key properties in `application.properties`:

```properties
# Server
server.port=8082

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/smart_complaint_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root

# JPA
spring.jpa.hibernate.ddl-auto=update
```

> ⚠️ **Note:** Change the default `username` and `password` before deploying to production.

---

## 🧭 Usage

### User Flow
1. Register a new account at `/register`
2. Login at `/login`
3. Submit a complaint from the User Dashboard
4. Track complaint status in real-time

### Admin Flow
1. Login with admin credentials
2. View all complaints in the Admin Dashboard
3. Assign complaints to agents
4. Update complaint statuses
5. View reports and analytics

---

## 📸 Screenshots

> _Add screenshots of your application here_

| User Dashboard | Admin Dashboard |
|---|---|
| ![User Dashboard](screenshots/user-dashboard.png) | ![Admin Dashboard](screenshots/admin-dashboard.png) |

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add YourFeature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👩‍💻 Author

**Sneha Gurav**
- GitHub: [@SnehaGurav-create](https://github.com/SnehaGurav-create)

---

> ⭐ If you found this project helpful, please give it a star!
