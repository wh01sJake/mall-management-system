# Mall Management System

A comprehensive e-commerce management system built with Spring Boot and Vue.js, featuring customer management, product catalog, order processing, and administrative tools.

## 🚀 Project Migration Notice

**⚠️ This repository is now archived and has been migrated to a microservices architecture.**

**New Repository:** [https://github.com/wh01sJake/mall-cloud](https://github.com/wh01sJake/mall-cloud)

The new microservices version includes:
- Improved scalability and maintainability
- Service-oriented architecture
- Enhanced performance and reliability
- Modern cloud-native features

## 📋 Legacy System Overview

This monolithic version includes:

### Backend Services
- **mall-admin**: Administrative backend service (Port 8080)
- **mall-portal**: Customer-facing portal service (Port 8081)
- **mall-common**: Shared utilities and components
- **mall-pojo**: Data transfer objects and entities

### Frontend Applications
- **mall-admin-ui**: Vue.js admin dashboard
- **mall-portal-ui**: Customer web interface

### Key Features
- 🛍️ Product catalog management
- 👥 Customer management system
- 📦 Order processing and tracking
- 🔐 Admin authentication and authorization
- 📊 Sales analytics and reporting
- 🖼️ AWS S3 image upload integration
- 📨 RabbitMQ message queuing
- 🗄️ Redis caching layer

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 2.x
- **Database**: MySQL 8.0
- **Cache**: Redis
- **Message Queue**: RabbitMQ
- **Cloud Storage**: AWS S3
- **ORM**: MyBatis-Plus

### Frontend
- **Framework**: Vue.js 3
- **Build Tool**: Vite
- **UI Library**: Element Plus
- **State Management**: Vuex

## 📁 Project Structure

```
mall/
├── mall-admin/          # Admin backend service
├── mall-portal/         # Customer portal service
├── mall-common/         # Shared utilities
├── mall-pojo/           # Data entities
├── mall-ui/
│   ├── mall-admin-ui/   # Admin dashboard
│   └── mall-portal-ui/  # Customer interface
├── database_sample_data.sql
└── README.md
```

## ⚠️ Security Notice

This repository has been cleaned of sensitive credentials. For local development:

1. Copy `.env.example` to `.env`
2. Add your actual database and AWS credentials
3. Never commit the `.env` file

## 🚀 Quick Start (Legacy)

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0
- Redis
- RabbitMQ

### Backend Setup
```bash
# Start admin service
cd mall-admin
mvn spring-boot:run

# Start portal service  
cd mall-portal
mvn spring-boot:run
```

### Frontend Setup
```bash
# Admin UI
cd mall-ui/mall-admin-ui
npm install
npm run dev

# Portal UI
cd mall-ui/mall-portal-ui
npm install
npm run dev
```

## 📈 Migration Path

For new development, please use the microservices version:
[https://github.com/wh01sJake/mall-cloud](https://github.com/wh01sJake/mall-cloud)

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Jake** - [GitHub Profile](https://github.com/wh01sJake)

---

**Note**: This is a legacy monolithic version. Active development has moved to the microservices architecture at [mall-cloud](https://github.com/wh01sJake/mall-cloud).
