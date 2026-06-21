# IronPlate — Fitness & Nutrition Platform

A microservices-based fitness and nutrition management platform built with Spring Boot and Spring Cloud.

---

## Table of Contents

- [Project Description](#project-description)
- [Architecture](#architecture)
- [ER Diagram](#er-diagram)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Monitoring](#monitoring)

---

## Project Description

IronPlate is a distributed fitness and nutrition platform that allows users to manage training programs, exercises, meal plans, and food items. The platform is built as a microservices system with centralized configuration, service discovery, API gateway, Redis caching, circuit breakers, distributed tracing, and monitoring.

**Public URL:** `https://<your-ngrok-url>.ngrok-free.app`

---

## Architecture

```
                        ┌─────────────────┐
                        │   API Gateway   │  :8080
                        │ (Rate Limiting, │
                        │  JWT Auth)      │
                        └────────┬────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
     ┌────────▼──────┐  ┌────────▼──────┐  ┌───────▼───────┐
     │  User Service │  │Fitness Service│  │Nutrition Svc  │
     │    :8081      │  │    :8082      │  │    :8083      │
     │  (Redis Cache)│  │ (CB + Retry)  │  │  (CB + Cache) │
     └────────┬──────┘  └────────┬──────┘  └───────┬───────┘
              │                  │                  │
              └──────────────────┼──────────────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
     ┌────────▼──────┐  ┌────────▼──────┐  ┌───────▼───────┐
     │  Eureka       │  │ Config Server │  │     MySQL     │
     │  Server :8761 │  │    :8888      │  │    :3306      │
     └───────────────┘  └───────────────┘  └───────────────┘

     ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
     │     Redis     │  │  Prometheus   │  │    Grafana    │
     │    :6379      │  │    :9090      │  │    :3000      │
     └───────────────┘  └───────────────┘  └───────────────┘

     ┌───────────────┐
     │    Zipkin     │
     │    :9411      │
     └───────────────┘
```

**Key patterns & features:**
- **API Gateway** — centralized routing, JWT authentication, Redis-backed rate limiting (10 req/s per IP)
- **Service Discovery** — Eureka server, all services auto-register
- **Load Balancing** — Spring Cloud LoadBalancer (round-robin across scaled instances)
- **Config Server** — centralized configuration for all microservices
- **Circuit Breaker** — Resilience4j in fitness-service and nutrition-service (fallback to null on user-service failure)
- **Retry** — 3 attempts with 1s wait in fitness-service
- **Redis Caching** — user lookups cached in user-service, food item lookups in nutrition-service
- **Distributed Tracing** — Zipkin via Micrometer Brave
- **Monitoring** — Prometheus + Grafana dashboard (CPU, memory, HTTP request rate)
- **CI/CD** — GitHub Actions on push to `master` and `dev`

---

## ER Diagram

### ironplate_users (User Service)

```
┌─────────────────────────┐
│          users          │
├─────────────────────────┤
│ id          BIGINT (PK) │
│ username    VARCHAR     │
│ password    VARCHAR     │
│ email       VARCHAR     │
│ role        ENUM        │
│             ATHLETE     │
│             COACH       │
│             ADMIN       │
└─────────────────────────┘
```

### ironplate_fitness (Fitness Service)

```
┌──────────────────────────────┐     ┌──────────────────────────────┐
│           exercises          │     │       training_programs      │
├──────────────────────────────┤     ├──────────────────────────────┤
│ id             BIGINT (PK)   │     │ id              BIGINT (PK)  │
│ name           VARCHAR       │     │ name            VARCHAR      │
│ description    VARCHAR       │     │ description     VARCHAR      │
│ muscle_group   ENUM          │     │ duration_weeks  INT          │
│   CHEST, BACK, LEGS,         │     │ difficulty_level ENUM        │
│   SHOULDERS, ARMS,           │     │   BEGINNER                  │
│   CORE, FULL_BODY            │     │   INTERMEDIATE              │
│ exercise_type  ENUM          │     │   ADVANCED                  │
│   STRENGTH, CARDIO,          │     │ user_id         BIGINT (FK→ │
│   MOBILITY, PLYOMETRIC       │     │                  users.id)  │
└──────────────────────────────┘     └──────────────────────────────┘
```

### ironplate_nutrition (Nutrition Service)

```
┌──────────────────────────────┐     ┌──────────────────────────────┐
│          food_items          │     │          meal_plans          │
├──────────────────────────────┤     ├──────────────────────────────┤
│ id               BIGINT (PK) │     │ id               BIGINT (PK) │
│ name             VARCHAR     │     │ name             VARCHAR     │
│ category         ENUM        │     │ start_date       DATE        │
│   PROTEIN, CARBOHYDRATE,     │     │ end_date         DATE        │
│   FAT, VEGETABLE, FRUIT,     │     │ daily_calorie_   INT         │
│   DAIRY, OTHER               │     │   target                    │
│ calories_per100g DOUBLE      │     │ user_id          BIGINT (FK→ │
│ protein_per100g  DOUBLE      │     │                  users.id)  │
│ carbs_per100g    DOUBLE      │     └──────────────────────────────┘
│ fat_per100g      DOUBLE      │
└──────────────────────────────┘
```

> `user_id` in `training_programs` and `meal_plans` is a logical foreign key — enforced at the application level via Feign Client calls to user-service, not at the database level (separate schemas across services).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4.5 |
| Service Discovery | Spring Cloud Netflix Eureka |
| Config | Spring Cloud Config Server |
| API Gateway | Spring Cloud Gateway |
| Communication | Spring Cloud OpenFeign |
| Fault Tolerance | Resilience4j (Circuit Breaker + Retry) |
| Caching | Redis + Spring Cache |
| Database | MySQL 8.0 |
| Monitoring | Prometheus + Grafana |
| Tracing | Zipkin + Micrometer Brave |
| CI/CD | GitHub Actions |
| Containerization | Docker + Docker Compose |

---

## Setup Instructions

### Prerequisites

- Docker & Docker Compose
- Java 21
- Gradle

### Environment Variables

All configuration is managed by Config Server. The following variables are set via Docker Compose:

| Variable | Value | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `docker` | Activates Docker profile (uses container hostnames) |
| `MYSQL_ROOT_PASSWORD` | `ironplate` | MySQL root password |
| `GF_SECURITY_ADMIN_PASSWORD` | `ironplate` | Grafana admin password |

### Run Locally

```bash
# 1. Clone the repository
git clone https://github.com/MarianDanaila/AWBD_project.git
cd AWBD_project/IronPlate

# 2. Build all microservices
for service in config-server eureka-server user-service fitness-service nutrition-service api-gateway; do
  cd $service && GRADLE_USER_HOME=/tmp/gradle-clean ./gradlew build -x test && cd ..
done

# 3. Start everything
docker compose up --build

# 4. Verify all services are up
docker compose ps
```

### Service URLs

| Service | URL |
|---|---|
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| Config Server | http://localhost:8888 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (admin / ironplate) |
| Zipkin | http://localhost:9411 |

### Scale a Service (Load Balancing Demo)

```bash
docker compose up --scale user-service=2
```

---

## API Documentation

All requests go through the API Gateway at `http://localhost:8080`.  
Protected endpoints require `Authorization: Bearer <token>`.

### Authentication

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/auth/register` | No | Register a new user |
| POST | `/auth/login` | No | Login and receive JWT token |

**Register:**
```json
POST /auth/register
{
  "username": "athlete1",
  "password": "secret123",
  "email": "athlete1@example.com"
}
```

**Login:**
```json
POST /auth/login
{
  "username": "athlete1",
  "password": "secret123"
}
// Response: { "token": "eyJ..." }
```

---

### User Service

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/users` | Yes | List all users |
| GET | `/users/{id}` | Yes | Get user by ID (cached in Redis) |
| PUT | `/users/{id}/role` | Yes | Update user role |
| DELETE | `/users/{id}` | Yes | Delete user |
| GET | `/users/instance` | Yes | Returns hostname (load balancing demo) |

---

### Fitness Service

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/exercises` | Yes | List exercises (paginated) |
| GET | `/exercises/{id}` | Yes | Get exercise by ID |
| POST | `/exercises` | Yes | Create exercise |
| PUT | `/exercises/{id}` | Yes | Update exercise |
| DELETE | `/exercises/{id}` | Yes | Delete exercise |
| GET | `/training-programs` | Yes | List programs (paginated, optional `?userId=`) |
| GET | `/training-programs/{id}` | Yes | Get program by ID |
| POST | `/training-programs` | Yes | Create program (validates user via Feign) |
| PUT | `/training-programs/{id}` | Yes | Update program |
| DELETE | `/training-programs/{id}` | Yes | Delete program |

**Create Training Program:**
```json
POST /training-programs
{
  "name": "12-Week Strength",
  "description": "Progressive overload program",
  "durationWeeks": 12,
  "difficultyLevel": "INTERMEDIATE",
  "userId": 1
}
```

---

### Nutrition Service

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/food-items` | Yes | List food items (paginated) |
| GET | `/food-items/{id}` | Yes | Get food item by ID (cached in Redis) |
| POST | `/food-items` | Yes | Create food item |
| PUT | `/food-items/{id}` | Yes | Update food item |
| DELETE | `/food-items/{id}` | Yes | Delete food item |
| GET | `/meal-plans` | Yes | List meal plans (optional `?userId=`) |
| GET | `/meal-plans/{id}` | Yes | Get meal plan by ID |
| POST | `/meal-plans` | Yes | Create meal plan (validates user via Feign) |
| PUT | `/meal-plans/{id}` | Yes | Update meal plan |
| DELETE | `/meal-plans/{id}` | Yes | Delete meal plan |

**Create Food Item:**
```json
POST /food-items
{
  "name": "Chicken Breast",
  "category": "PROTEIN",
  "caloriesPer100g": 165,
  "proteinPer100g": 31,
  "carbsPer100g": 0,
  "fatPer100g": 3.6
}
```

**Create Meal Plan:**
```json
POST /meal-plans
{
  "name": "Cutting Plan",
  "startDate": "2026-07-01",
  "endDate": "2026-09-01",
  "dailyCalorieTarget": 2000,
  "userId": 1
}
```

---

## Monitoring

### Prometheus Targets

Open `http://localhost:9090/targets` — user-service, fitness-service, and nutrition-service should all show as UP.

### Grafana Dashboard

Open `http://localhost:3000`, login with `admin` / `ironplate`.  
The **IronPlate Metrics** dashboard is pre-provisioned with:
- HTTP request rate per service
- JVM heap memory usage
- CPU usage
- Service health status

### Distributed Tracing

Open `http://localhost:9411` to view traces. Every request is traced with 100% sampling probability.  
Cross-service calls (e.g. fitness-service → user-service via Feign) appear as multi-span traces.

### Rate Limiting

The API Gateway enforces **10 requests/second** per IP with a burst capacity of 20.  
Exceeding the limit returns `429 Too Many Requests`.
