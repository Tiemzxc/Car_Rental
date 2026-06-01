# Car Rental System — Your Ride, Your Way

A full-stack web-based Car Rental System built with **Java Spring Boot**, **Thymeleaf**, **MySQL (XAMPP)**, and **Maven**. Users can register, browse available vehicles, and book rentals. Admins manage vehicles, bookings, brands, testimonials, and more through a dedicated dashboard.

---

## Project Structure

```
car_rental/
├── pom.xml
└── src/
    └── main/
        ├── java/com/carrental/
        │   ├── CarRentalApplication.java
        │   ├── controller/
        │   │   ├── MainController.java       # User-facing routes
        │   │   └── AdminController.java      # Admin-only routes
        │   ├── model/
        │   │   ├── User.java
        │   │   ├── Admin.java
        │   │   ├── Customer.java
        │   │   ├── Vehicle.java
        │   │   ├── Brand.java
        │   │   ├── Booking.java
        │   │   ├── Payment.java
        │   │   ├── Testimonial.java
        │   │   ├── Subscriber.java
        │   │   ├── ContactInfo.java
        │   │   ├── ContactQuery.java
        │   │   ├── PageContent.java
        │   │   └── OtpVerification.java
        │   ├── repository/
        │   │   ├── (interfaces)
        │   │   └── impl/
        │   │       └── (JDBC implementations)
        │   └── service/
        │       ├── (interfaces)
        │       └── impl/
        │           └── (service implementations)
        └── resources/
            ├── application.properties
            ├── schema.sql
            ├── templates/
            │   ├── index.html                # Login / Register / Landing
            │   ├── profile.html              # Customer dashboard
            │   └── admin/
            │       ├── dashboard.html
            │       ├── vehicles.html
            │       ├── brands.html
            │       ├── bookings.html
            │       ├── testimonials.html
            │       ├── users.html
            │       ├── subscribers.html
            │       ├── contact-queries.html
            │       ├── contact-info.html
            │       └── page-content.html
            └── static/
                ├── css/style.css
                └── js/app.js
```

---

## Features

### Customer
- Register with first name, last name, email, contact number, and driving license
- OTP email verification on registration
- Login / Logout with session management
- Browse all available vehicles on the homepage
- Book a vehicle by selecting start and end dates (conflict detection included)
- View booking history on the profile page
- Submit testimonials with a star rating

### Admin
- Secure admin panel — session-role-checked on every route
- **Dashboard** — overview of brands, vehicles, bookings, testimonials, contacts, subscribers, and users
- **Brands** — create, update, delete vehicle brands
- **Vehicles** — add vehicles (model, type, brand, image URL, price/day, availability), delete vehicles
- **Bookings** — view all bookings and update their status
- **Testimonials** — approve or reject customer-submitted reviews
- **Contact Queries** — view and delete messages from the contact form
- **Users** — view all registered customers
- **Subscribers** — manage newsletter subscribers, delete entries
- **Page Content** — edit dynamic content sections of the homepage
- **Contact Info** — update the site's public phone, email, and address

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.1.5 |
| Templating | Thymeleaf |
| Build Tool | Apache Maven |
| Database | MySQL via XAMPP |
| JDBC | Spring JDBC (JdbcTemplate) |
| Email | Spring Mail (Gmail SMTP) |
| UI Components | Bootstrap 5.3.2 (WebJar) |
| Charts | Chart.js 3.6.0 (WebJar) |
| Validation | Spring Boot Validation |

---

## Database Schema

```sql
-- Users (customers and admins)
CREATE TABLE users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    middle_name     VARCHAR(100),
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    contact_number  VARCHAR(20) NOT NULL,
    role            VARCHAR(20) NOT NULL,       -- 'ADMIN' or 'CUSTOMER'
    driving_license VARCHAR(100)
);

-- Car brands
CREATE TABLE brands (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Vehicles
CREATE TABLE vehicles (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    model        VARCHAR(150) NOT NULL,
    type         VARCHAR(100) NOT NULL,
    brand_id     BIGINT,
    image_url    VARCHAR(255),
    price_per_day DECIMAL(10,2) NOT NULL,
    available    BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (brand_id) REFERENCES brands(id)
);

-- Bookings
CREATE TABLE bookings (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    vehicle_id  BIGINT,
    start_date  DATE,
    end_date    DATE,
    returned    BOOLEAN DEFAULT FALSE,
    fine        DECIMAL(10,2) DEFAULT 0,
    status      VARCHAR(30),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicles(id)
);

-- Payments
CREATE TABLE payments (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id     BIGINT,
    amount         DECIMAL(10,2),
    status         VARCHAR(30),
    timestamp      DATETIME,
    payment_method VARCHAR(50),
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- Testimonials
CREATE TABLE testimonials (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    message     TEXT,
    rating      INT,
    status      VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES users(id)
);

-- OTP Verification
CREATE TABLE otp_verification (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(150),
    otp_code   VARCHAR(10),
    expires_at DATETIME,
    verified   BOOLEAN DEFAULT FALSE
);

-- Other tables: contact_queries, subscribers, page_content, contact_info
```

---

## Running Locally

### Prerequisites
- Java 17+
- Apache Maven
- XAMPP (MySQL + Apache)

### Steps

**1. Start XAMPP**

Open the XAMPP Control Panel and start **Apache** and **MySQL**.

**2. Set up the database**

Open `http://localhost/phpmyadmin` and run the `schema.sql` file found in `src/main/resources/schema.sql`.  
This creates the `car_rental_db` database and all tables automatically.  
A default admin account is also seeded:

| Field | Value |
|---|---|
| Email | `admin@carrental.local` |
| Password | `Admin123!` |
| Role | `ADMIN` |

**3. Configure the application**

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/car_rental_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=         # leave blank for default XAMPP

# Gmail SMTP for OTP emails
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
```

> For Gmail SMTP, generate an **App Password** from your Google account (2FA must be enabled).

**4. Build and run**

```bash
mvn clean package
mvn spring-boot:run
```

**5. Open in browser**

```
http://localhost:8080
```

---

## Routes

### Public / Customer

| Method | Route | Description |
|---|---|---|
| GET | `/` | Homepage — vehicle listing, login, register |
| POST | `/register` | Register a new customer |
| POST | `/verify-otp` | Verify OTP sent to email |
| POST | `/login` | Login |
| GET | `/logout` | Logout and invalidate session |
| GET | `/profile` | Customer profile and booking history |
| POST | `/book` | Submit a new booking |
| POST | `/testimonial` | Submit a testimonial |

### Admin (requires ADMIN role)

| Method | Route | Description |
|---|---|---|
| GET | `/admin/dashboard` | Overview statistics |
| GET/POST | `/admin/brands` | List, create, update, delete brands |
| GET/POST | `/admin/vehicles` | List, add, delete vehicles |
| GET/POST | `/admin/bookings` | View and update booking status |
| GET/POST | `/admin/testimonials` | Approve or reject testimonials |
| GET/POST | `/admin/contact-queries` | View and delete contact messages |
| GET | `/admin/users` | View all users |
| GET/POST | `/admin/subscribers` | Manage newsletter subscribers |
| GET/POST | `/admin/page-content` | Edit dynamic page content |
| GET/POST | `/admin/contact-info` | Update site contact details |

---

## Environment Variables / Configuration

All configuration lives in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/car_rental_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL_ADDRESS@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## Admin Setup

The default admin is seeded automatically by `schema.sql`. To promote any registered user to admin, run this in phpMyAdmin:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

---

## License

This project is for educational purposes.
