# Banking System 💳

A simple **Banking System project** built using **Java, JDBC, and Maven**, with database integration.  
It supports basic banking operations such as creating accounts, deposits, withdrawals, and transaction history.

---

## 🚀 Features
- Create new bank accounts
- Deposit and withdraw money
- View account balance
- View transaction history
- JDBC integration with database

---

## 🛠️ Tech Stack
- **Java 8+**
- **Maven**
- **JDBC**
- **Database:** MySQL / SQLite

---

## ⚙️ Setup Instructions

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/banking-system.git
   cd banking-system
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Configure the database:
   - Run `schema.sql` in your MySQL/SQLite to create tables
   - Update `DBConnection.java` with your DB credentials

4. Run the application:
   ```bash
   mvn exec:java
   ```

---

## 📂 Database Schema

### Accounts Table
| Column   | Type    | Description              |
|----------|---------|--------------------------|
| acc_no   | INT PK  | Account Number           |
| name     | VARCHAR | Account Holder Name      |
| balance  | DOUBLE  | Account Balance          |
| pin_hash | VARCHAR | Secure PIN (hashed)      |

### Transactions Table
| Column    | Type      | Description             |
|-----------|-----------|-------------------------|
| trans_id  | INT AI PK | Transaction ID          |
| acc_no    | INT       | Account Number          |
| type      | VARCHAR   | Transaction Type        |
| amount    | DOUBLE    | Transaction Amount      |
| timestamp | DATETIME  | Transaction Date & Time |

---

## 📜 License
This project is licensed under the **MIT License**.

---
