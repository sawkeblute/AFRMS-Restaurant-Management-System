# AFRMS – Restaurant Management System

AFRMS (AIU Front-gate Restaurant Management System) is a desktop-based system designed to support a Thai front-gate restaurant environment, including order processing, Thai baht payment handling, inventory tracking, sales reporting, and role-based access control for a single-branch restaurant.

---

## System Modules

- **Authentication & RBAC Module**  
  Staff login is role-based. Owner, Manager, Cashier, Kitchen Staff, and Inventory Staff each see only the modules permitted by the AFRMS access matrix.

- **Order Management Module**  
  The POS creates paid orders in Thai baht, links every order to the active user ID, calculates VAT, and sends the order to the kitchen queue.

- **Payment Processing Module**  
  Payment validation blocks empty orders, zero or negative payments, and insufficient payment amounts with user-readable error messages.

- **Inventory Management Module**  
  Shop menu items such as fried noodles, pad Thai, fried rice, tom yum fried rice, suki, noodle soup, rad na, basil stir-fry, omelet rice, and som tam are mapped to recipes. Completed sales automatically deduct ingredient quantities, while supplier deliveries and manual adjustments require justification.

- **Reporting Dashboard Module**  
  The dashboard and report view summarize orders, revenue, estimated cost, estimated profit, average order value, and low-stock alerts.

- **Audit Logging Module**  
  Login, logout, menu snapshots, order creation, order status changes, and inventory updates are written to an immutable in-browser audit log.

---

## Team Members

- Saw Ke Blute – Lead Architect / Reviewer  
- Saw Eh Thalay Htoo – Lead Developer  
- Saw Joshua – QA Lead  
- Similosakhe Moyo – Backend Developer  
- Samantha Jawjong – Reporting / UI Developer  

---

## Development Workflow

This project follows a structured GitHub workflow:

1. Develop each major module independently.
2. Validate changes with the Java test runner.
3. Review feature behavior against the AFRMS reports before merging.

## Demo Login Accounts

| Role | Username | Password |
| --- | --- | --- |
| Owner | `owner` | `owner123` |
| Manager | `manager` | `manager123` |
| Cashier | `cashier` | `cashier123` |
| Kitchen Staff | `kitchen` | `kitchen123` |
| Inventory Staff | `inventory` | `inventory123` |

## Local Verification

```bash
javac *.java
java TestRunner
```
