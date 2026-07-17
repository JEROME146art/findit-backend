# 🔍 FindIt & UniVote AI — Smart Campus Civic & Lost Item Platform

A smart full-stack Java and responsive web application that helps students recover lost items on campus and participate in secure, verifiable college elections.

## 🌟 Live Features & Portals
- **FindIt Lost & Found**: Reconnect with lost items using weighted Jaccard similarity scoring.
- **UniVote AI Campus Voting (`/voting` or `univote/index.html`)**: Modern college election & civic platform featuring Ranked-Choice Voting (IRV), zero-knowledge cryptographic ballot verification (#UV-2026 tokens), townhall Q&A, and live turnout analytics.

## 🗳️ UniVote AI — Modern College Voting System Highlights

- ⚡ **Ranked-Choice Voting (RCV / Instant Runoff)** — Eliminate split votes and ensure 50% majority outcomes with an interactive round-by-round elimination simulator.
- 🔐 **Zero-Knowledge Cryptographic Receipts** — SHA-256 ballot tokens (`#UV-2026-...`) verified on an immutable public campus audit ledger while guaranteeing 100% secret ballot anonymity.
- 📊 **Platform Comparison Matrix** — Side-by-side comparison of candidate stances on Academic Reform, Green Budgets, and Dorm Life.
- 💬 **Townhall Q&A Forum** — Direct student-to-candidate Q&A with upvoting and official response tracking.
- 📈 **Turnout Analytics Dashboard** — Real-time interactive Chart.js graphs displaying turnout by academic year, department share, and hourly velocity.
- 🛡️ **Role Switcher** — Seamlessly switch between **Student Voter**, **Candidate Campaign Portal**, and **Election Commission (Admin)** to create new ballots on the fly.

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
