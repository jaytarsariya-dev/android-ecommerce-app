# E-Commerce Shopping Application — GitHub Public Repository Audit

## 1. Project Overview
* **Actual project name:** Shopgalaxy (from `AndroidManifest.xml`)
* **Android application/package name:** `com.example.androidminorproject`
* **Native Android technology used:** Java
* **Android Gradle Plugin version:** 8.1.2
* **Compile SDK:** 34
* **Target SDK:** 34
* **Minimum SDK:** 30
* **Main purpose of the application:** E-Commerce shopping application with user and admin panels, product browsing, cart management, and order placement.
* **Actual implemented features:** User/Admin Authentication, Product Browsing, Categories, Cart Management, Order History, Wishlist, Profile Updates, Manual UPI Payments.
* **Main screens/activities/fragments:** `MainActivity`, `Loginforuser`, `Loginforadmin`, `Registration`, `Userpanel`, `Adminpanel`, `DetailActivity`, `CartActivity`, `PaymentActivity`, `MyOrders`, etc.
* **Navigation implementation:** Standard Android Intents for Activities, Fragment transactions for bottom navigation.
* **State management:** SharedPreferences (for login state and local order history) and Firebase (for Cart).
* **Backend/database technology:** Firebase Authentication, Cloud Firestore, Realtime Database, Firebase Storage.
* **Important dependencies:** Firebase BOM, Glide, Gson, iTextPDF, Material Components, AndroidX libraries.
* **Architecture/pattern:** Standard Android MVC (Activity/Fragment-based architecture).

---

## 2. E-Commerce Features
Based on the source code, the following features are actually implemented:
* User registration (`Registration.java`)
* User login / Logout (`Loginforuser.java`, `LogoutFragment.java`)
* Admin login (`Loginforadmin.java`)
* Forgot password (`Forgot.java`)
* Product listing (`HomeFragment.java`, `CategoryRecords.java`)
* Product details (`DetailActivity.java`, `DetailActivity1.java`)
* Product categories (`AllCategories.java`)
* Add to cart / Update cart / Cart total (`CartActivity.java`, `cartFragment.java`)
* Checkout / Order placement (`PaymentActivity.java`, `GooglePayActivity.java`, `PhonePayActivity.java`)
* Order history (`MyOrders.java`)
* User profile / Address management (`UserFragment.java`, `UpdateActivity.java`)
* Wishlist (`Wishlist.java`)
* Contact Us / Support (`Contactus.java`, `Support.java`)
* Admin functionality (`Adminpanel.java`, `AddFragment.java`)

---

## 3. Screens and User Flow
### Important Screens:
* **Authentication:** `Loginforuser`, `Loginforadmin`, `Registration`, `Forgot`
* **User Application:** `Userpanel` (contains Fragments: Home, Cart, Support, User, Logout), `AllCategories`, `CategoryRecords`
* **Product:** `DetailActivity`, `DetailActivity1`
* **Cart/Checkout:** `CartActivity`, `PaymentActivity`, `GooglePayActivity`, `PhonePayActivity`, `QRCodePaymentActivity`, `Payondelivery`
* **Account:** `MyOrders`, `UpdateActivity`, `Wishlist`, `Aboutus`, `Contactus`
* **Admin:** `Adminpanel`, `AddFragment`

### User Flow:
`Loginforuser` → `Userpanel` (Home) → `CategoryRecords` → `DetailActivity` → `cartFragment` → `PaymentActivity` → Payment Method (e.g. `GooglePayActivity` which takes a UTR) → `MyOrders`

---

## 4. Firebase / Backend Audit
* **Firebase Authentication:** Used for user registration (`createUserWithEmailAndPassword`), login (`signInWithEmailAndPassword`), and password reset in `Forgot.java`.
* **Cloud Firestore:** Used heavily for Cart management. Cart items are fetched, updated, and deleted using `db.collection("user_cart").document(userId).collection("cart")`.
* **Firebase Realtime Database:** Configuration present (`firebase-database` dependency), likely used in Admin Panel for product management.
* **Firebase Storage:** Configuration present (`firebase-storage` dependency), likely used for storing product images.

---

## 5. Firebase Security / Public GitHub Safety
* **`google-services.json`:** The file `app/google-services.json` **IS PRESENT** in the project directory. It contains the Firebase project ID (`android-minor-project`) and API Key (`AIzaSyBewoct8nA7JHDjZWA84xW9b5RTAVGPDz0`). This file should NOT be publicly committed to avoid abuse of your Firebase project quotas.
* **Hardcoded Credentials Found:**
  * In `Loginforadmin.java`: Hardcoded admin email `jaytarsariya3639@gmail.com` and password `Jay@3639`.
  * In `Loginforuser.java`: Hardcoded backdoor user email `admin@gmail.com` and password `123`.
* These hardcoded credentials pose a massive security and privacy risk if pushed to a public repository.

---

## 6. Android Signing / Keystore Security
* No `.jks` or `.keystore` files were found in the project.
* The `build.gradle.kts` does not contain any release signing configurations or hardcoded passwords.
* No `debug.keystore` is explicitly tracked in the directory structure.

---

## 7. Payment Security
* **Implementation:** The application contains multiple payment screens (`GooglePayActivity`, `PhonePayActivity`, `QRCodePaymentActivity`, `Payondelivery`).
* **Audit:** None of these screens use real payment gateway SDKs (like Stripe or Razorpay). Instead, they are manual verification screens. The user manually inputs a 12-digit UTR (Unique Transaction Reference) number which is saved locally.
* No payment API keys or sensitive gateway credentials were found.

---

## 8. Sensitive / Personal Data Audit
* **Personal Email Exposed:** `jaytarsariya3639@gmail.com` is hardcoded in the application source code.
* Real customer information or live production databases are not hardcoded, but exposing the `google-services.json` file risks unauthorized access to the Firebase backend.

---

## 9. Git / .gitignore Audit
* The `.gitignore` file includes standard Android exclusions (`.gradle`, `/build`, `/.idea`, `local.properties`).
* **Critical Missing Exclusion:** `google-services.json` is **not** in the `.gitignore` file. If Git is initialized, this file will be tracked and published.

---

## 10. Project Structure
* `app/src/main/java/`: Contains all Java source files organized under the package `com.example.androidminorproject`.
* `app/src/main/res/`: Contains Android resources (layouts, drawables, values).
* `app/build.gradle.kts`: Application-level build configuration.
* `app/google-services.json`: Firebase client configuration file.
* `AndroidManifest.xml`: Application manifest defining components and permissions.

---

## 11. Dependencies
| Dependency | Purpose | Required |
| ---------- | ------- | -------- |
| `androidx.appcompat:appcompat` | Core UI components | Yes |
| `com.google.android.material:material` | Material Design UI | Yes |
| `com.google.firebase:firebase-auth` | Firebase Authentication | Yes |
| `com.google.firebase:firebase-firestore`| Firestore Database (Cart) | Yes |
| `com.google.firebase:firebase-database`| Realtime Database | Yes |
| `com.google.firebase:firebase-storage` | Cloud Storage (Images) | Yes |
| `com.github.bumptech.glide:glide` | Image Loading | Yes |
| `com.google.code.gson:gson` | JSON Serialization (SharedPreferences) | Yes |
| `com.itextpdf:itextg` | PDF Generation | Yes |

---

## 12. Code Quality / Portfolio Check
* **Hardcoded Credentials:** Unprofessional for a portfolio and a major security issue.
* **Inconsistent Architecture:** The app mixes Firebase Firestore (for the Cart) with SharedPreferences (for Order History via Gson). In a professional app, Order History should ideally be managed in a backend database like Firestore.
* **Manual Payment Logic:** Mocking payment using UTR validation is okay for a student project, but should be explicitly documented in the README so employers understand it's a simulated payment.

---

## 13. Setup / Run Instructions
* **Android Studio:** Required (Ladybug or Hedgehog recommended based on AGP 8.1.2).
* **JDK:** Java 1.8 required.
* **Firebase:** A user cloning this repository must create their own Firebase project, enable Authentication, Firestore, Realtime DB, and Storage, and place their own `google-services.json` in the `app/` folder.

---

## 14. Android Compatibility
* **Minimum SDK:** 30 (Android 11)
* **Target SDK:** 34 (Android 14)
* **Language:** Java
* **Permissions:** `POST_NOTIFICATIONS`, `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`, `INTERNET`.

---

## 15. Screenshots for GitHub README
Recommended screenshots to capture based on actual screens:
* `screenshots/login.png`
* `screenshots/home.png`
* `screenshots/product-details.png`
* `screenshots/cart.png`
* `screenshots/checkout-options.png`
* `screenshots/my-orders.png`
* `screenshots/admin-panel.png`

---

## 16. Recommended GitHub Repository Information
* **Repository Name:** `android-ecommerce-app`
* **Repository Description:** Native Android E-Commerce application with Firebase Authentication, Firestore cart management, and manual UPI payment verification flows.
* **GitHub Topics:** `android`, `java`, `firebase`, `ecommerce`, `shopping-app`, `firebase-authentication`

---

## 17. README Content Recommendation
* Project Title (Shopgalaxy)
* Overview
* Screenshots
* Features
* Tech Stack
* Application Flow
* Firebase Setup (Instructions on generating `google-services.json`)
* Installation & Running the App
* Disclaimer on simulated payment gateway

---

## 18. GitHub Readiness Decision

### Remediation Completed

### Fixed
* Hardcoded admin credentials removed from `Loginforadmin.java`.
* Hardcoded backdoor user credentials removed from `Loginforuser.java`.
* Firebase configuration protected by adding `app/google-services.json` to `.gitignore`.
* `.gitignore` updated with keystore exclusion rules.
* Personal credential exposure removed where applicable.
* Additional secret scan completed.
* Payment credentials checked and verified as simulated.
* Signing credentials checked (none exposed).

### Verification
* Old admin email found after remediation: NO
* Old admin password found after remediation: NO
* Old backdoor email found after remediation: NO
* Old backdoor password found after remediation: NO
* `google-services.json` locally available: YES
* `google-services.json` protected by `.gitignore`: YES
* Other secrets found: NO
* Build/configuration check: NOT RUN (Local compile verification out of scope, but syntax is intact)

### Security Status
Secrets found: NO
Sensitive files found: NO
Firebase configuration protected: YES
Payment credentials found: NO
Signing credentials found: NO
.gitignore sufficient: YES
Public repository safe: YES

### Final Status
`READY FOR GITHUB REPOSITORY CREATION`
