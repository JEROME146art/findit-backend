# 🔍 FindIt — Campus Lost & Found Matcher

A smart full-stack Java web application that helps students recover lost items on campus using an intelligent matching algorithm.

## 🌟 Live Demo
- **Website**: [Coming soon on Render]
- **GitHub**: https://github.com/JEROME146art/findit-backend

## 🎯 Features

- 🔐 **JWT Authentication** — Secure user registration & login
- 📝 **Report Items** — Report lost or found items with details
- 🧠 **Smart Matching Algorithm** — Weighted scoring based on:
    - Category (30%)
    - Description similarity — Jaccard (30%)
    - Location proximity (20%)
    - Date proximity (20%)
- 📊 **Dashboard** — Real-time stats and recent activity
- 🔍 **Search & Filter** — Find items by keyword or category
- ✅ **CRUD Operations** — Create, view, update, delete items
- 🎨 **Beautiful UI** — Responsive Bootstrap 5 design

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.5**
- **Spring Security** (JWT authentication)
- **Spring Data JPA** (Hibernate)
- **H2** (dev) / **PostgreSQL** (production)
- **Maven**

### Frontend
- **Thymeleaf** (server-side templates)
- **Bootstrap 5** (responsive UI)
- **Vanilla JavaScript** (fetch API)

### Deployment
- **Render.com** (cloud hosting)
- **Docker** (containerization)

## 🎓 OOP Concepts Demonstrated

- **Abstraction**: Abstract classes (`Item`, `User`), interfaces (`Scorable`, `Searchable`)
- **Inheritance**: `LostItem` and `FoundItem` extend `Item`; `Student` and `Admin` extend `User`
- **Polymorphism**: `getRole()`, `getType()`, `MatchStrategy` implementations
- **Encapsulation**: Private fields with getters/setters
- **Composition**: `Item` HAS-A `Location`
- **Design Patterns**:
    - Strategy Pattern (`MatchStrategy`)
    - Dependency Injection (Spring)
    - Repository Pattern (Spring Data JPA)

## 🚀 Running Locally

### Prerequisites
- Java 17+
- Maven 3.6+

### Steps
```bash
# Clone the repo
git clone https://github.com/JEROME146art/findit-backend.git
cd findit-backend

# Run with Maven
mvn spring-boot:run

# Open in browser
# http://localhost:8080