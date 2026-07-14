# Fabric Alert Engine

A full-stack price-tracking app for fabric sourcing. Users sign up, watch fabrics on a live
price board, and set a target price per fabric — when a fabric's price drops to their target,
the app automatically sends them an email alert.

Prices are simulated: a scheduled background job applies a small random change (±10%) to every
fabric's price every 5 minutes, imitating a live market so the whole alert pipeline can be
exercised end to end without a paid price-feed API.

## Features

- Sign up and log in from the browser
- Live price board, prices refresh automatically every 30 seconds
- Set a target price on any fabric; setting a new target on the same fabric updates it (no duplicate alerts)
- Automatic email notification when a price falls to your target — sent once per price drop, re-armed when the price recovers
- "My alerts" page to view and remove your alerts
- Per-user privacy: users can only ever see and manage their own alerts
- Add and remove fabrics from the board (login required; viewing is public)
- Request validation with clean HTTP status codes (400/401/404/409)

## Tech stack

- **Backend:** Java, Spring Boot, Spring Security (BCrypt + HTTP Basic), Spring Data JPA, Spring Mail
- **Database:** PostgreSQL
- **Frontend:** vanilla HTML/CSS/JavaScript (no framework), served as Spring static resources
- **Tests:** JUnit 5

## Screenshots

<!-- TODO: add docs/board.png and docs/alerts.png -->

## Getting started

### Prerequisites

- JDK (project is built with Java 26)
- PostgreSQL running locally
- A Gmail account with an [app password](https://support.google.com/accounts/answer/185833) (for sending alert emails)

### Setup

1. Create the database:

   ```sql
   CREATE DATABASE fabric_alerts;
   ```

2. Create `src/main/resources/application.properties` (gitignored — it holds credentials):

   ```properties
   spring.application.name=fabric-alert-engine
   spring.datasource.url=jdbc:postgresql://localhost:5432/fabric_alerts
   spring.datasource.username=${DB_USERNAME:postgres}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=${MAIL_USERNAME}
   spring.mail.password=${MAIL_PASSWORD}
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

3. Set the environment variables:

   | Variable | Value |
   |---|---|
   | `DB_PASSWORD` | your PostgreSQL password |
   | `MAIL_USERNAME` | the Gmail address that sends alerts |
   | `MAIL_PASSWORD` | the Gmail **app password** (not your real password) |

4. Run the app:

   ```
   ./gradlew bootRun
   ```

   Then open <http://localhost:8080>, create an account, and log in. The database schema is
   created automatically on first run (`ddl-auto=update`); add a few fabrics from the board
   to get started.

## How it works

Two scheduled jobs drive everything:

- **Price simulation** (every 5 minutes) nudges each fabric's price by a random percentage
  between 10% and +10%, with a $5.00 floor.
- **Alert check** (every 60 seconds) compares each user's target price against the current
  price of the fabric they're watching. When the price is at or below the target, it sends an
  email and marks the alert as notified so the user isn't spammed every minute; the flag resets
  once the price climbs back above the target, so the next drop alerts again.

Ownership is enforced in the REST layer: alert endpoints resolve the logged-in user from the
request and filter everything by owner, returning 404 for other users' resources so their IDs
can't be probed. Passwords are stored as BCrypt hashes and are write-only in the JSON API.

## Things to come in the next version

- Better frontend ( less vanilla)
- Price history and charts
- Integration tests (MockMvc)
- Token-based auth (JWT) instead of HTTP Basic
- Docker / cloud deployment
