# 🛒 Shopgalaxy — Android E-Commerce Application

Shopgalaxy is a native Android e-commerce shopping application built with **Java** and **Firebase**.

The application provides separate user and admin panels with features such as product browsing, categories, product details, cart management, wishlist, order history, profile management, and simulated UPI payment verification.

---

## 📱 Overview

Shopgalaxy is designed as a complete Android shopping application where users can:

* Create an account and log in
* Browse products and categories
* View product details
* Add and update products in the cart
* Manage wishlist items
* Place orders
* Choose different checkout/payment methods
* View order history
* Update profile and address information
* Contact support

The application also includes an **Admin Panel** for product management.

> **Note:** Payment functionality in this project is a simulated/manual UPI verification flow. It does not use a real payment gateway such as Razorpay or Stripe.

---

## ✨ Features

### 👤 User Features

* User Registration
* User Login & Logout
* Forgot Password
* Product Listing
* Product Categories
* Product Details
* Add to Cart
* Update Cart
* Cart Total
* Wishlist
* Checkout
* Order Placement
* Order History
* Profile Management
* Address Management
* Contact Us
* Support

### 🛠️ Admin Features

* Admin Authentication
* Admin Panel
* Product Management
* Add Product
* Product Image Management

### 💳 Payment

The application includes multiple payment-related options:

* Google Pay
* PhonePe
* QR Code Payment
* Cash on Delivery

The UPI payment flow is **simulated**. Users can enter a UTR number for manual verification instead of processing a real online payment.

---

## 🔄 Application Flow

### User Flow

```text
Login / Registration
        ↓
    User Panel
        ↓
      Home
        ↓
    Categories
        ↓
 Product Details
        ↓
      Cart
        ↓
    Checkout
        ↓
  Payment Method
        ↓
    My Orders
```

### Admin Flow

```text
Admin Login
     ↓
Admin Panel
     ↓
Product Management
     ↓
Add / Manage Products
```

---

## 🧰 Tech Stack

| Technology                 | Usage                                  |
| -------------------------- | -------------------------------------- |
| Java                       | Native Android application development |
| Android SDK                | Android development                    |
| Firebase Authentication    | User authentication                    |
| Cloud Firestore            | Cart data management                   |
| Firebase Realtime Database | Application/admin data                 |
| Firebase Storage           | Product image storage                  |
| Glide                      | Image loading                          |
| Gson                       | JSON serialization                     |
| iTextPDF                   | PDF generation                         |
| Material Components        | UI components                          |
| AndroidX                   | Android libraries                      |
| SharedPreferences          | Local login/order state                |

---

## 🏗️ Architecture

The application follows a standard **Android MVC / Activity-Fragment based architecture**.

### Navigation

* Android `Intent` is used for Activity navigation.
* Fragment transactions are used for bottom navigation.

### State & Data Management

* **SharedPreferences** is used for local login state and order history.
* **Cloud Firestore** is used for cart management.
* Firebase services are used for authentication, database, and storage operations.

---

## 🔥 Firebase

The application uses the following Firebase services.

### Firebase Authentication

Used for:

* User registration
* User login
* Password reset

### Cloud Firestore

Used for cart management and storing user cart items.

### Firebase Realtime Database

Used for application/admin data management.

### Firebase Storage

Used for storing product images.

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

* Android Studio
* JDK 8
* Android SDK
* Android device or emulator

The project uses **Android Gradle Plugin 8.1.2**.

---

## 📥 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/jaytarsariya-dev/android-ecommerce-app.git
```

### 2. Open the Project

Open the cloned project in **Android Studio**.

### 3. Configure Firebase

This repository does not include the original Firebase configuration file.

Create your own Firebase project and enable:

* Firebase Authentication
* Cloud Firestore
* Firebase Realtime Database
* Firebase Storage

Then download your Firebase Android configuration file:

```text
google-services.json
```

Place it inside:

```text
app/google-services.json
```

### 4. Build and Run

Sync the Gradle files and run the application on an Android device or emulator.

---

## 📱 Android Compatibility

| Configuration         | Version         |
| --------------------- | --------------- |
| Minimum SDK           | 30 (Android 11) |
| Target SDK            | 34 (Android 14) |
| Compile SDK           | 34              |
| Language              | Java            |
| Android Gradle Plugin | 8.1.2           |

---

## 📂 Project Structure

```text
app/
├── src/
│   └── main/
│       ├── java/
│       │   └── com.example.androidminorproject/
│       └── res/
│
├── build.gradle.kts
├── google-services.json
└── AndroidManifest.xml
```

> `google-services.json` is intentionally excluded from the public repository.

---

## 🔐 Security

Before publishing this project, the repository was reviewed for sensitive information.

The following security improvements were completed:

* Removed hardcoded admin credentials
* Removed hardcoded backdoor user credentials
* Protected `google-services.json`
* Added Firebase configuration to `.gitignore`
* Added keystore exclusion rules
* Removed exposed personal credentials where applicable
* Checked for payment credentials
* Checked for signing credentials
* Completed an additional secret scan

Final security verification:

```text
Secrets found: NO
Sensitive files found: NO
Firebase configuration protected: YES
Payment credentials found: NO
Signing credentials found: NO
.gitignore sufficient: YES
Public repository safe: YES
```

---

## 💳 Payment Disclaimer

This project **does not implement a real payment gateway**.

The payment screens demonstrate a simulated/manual UPI payment workflow where a user can provide a UTR number.

No real Razorpay, Stripe, or other payment gateway API is integrated.

This implementation is intended for **learning and demonstration purposes**.

---

## ⚠️ Firebase Configuration

When running the application locally, you must configure your own Firebase project.

Do not commit your personal:

```text
google-services.json
```

to a public repository.

The repository's `.gitignore` is configured to protect the Firebase configuration file.

---

## 📚 What I Learned

Through this project, I worked with:

* Native Android development using Java
* Android Activities and Fragments
* Firebase Authentication
* Cloud Firestore
* Firebase Realtime Database
* Firebase Storage
* CRUD operations
* Shopping cart management
* User authentication flows
* Local data persistence
* Image loading with Glide
* JSON serialization with Gson
* Android UI development
* Payment flow simulation
* Git and GitHub repository management
* Android project security and secret protection

---

## 👨‍💻 Author

**Jay Tarsariya**

React Native Developer | Mobile Application Developer

**Skills:**

* JavaScript
* TypeScript
* React Native
* Native Android
* Firebase
* REST APIs

---

## 📄 License

This project is created for **learning, portfolio, and demonstration purposes**.
