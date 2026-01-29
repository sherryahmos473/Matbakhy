# 🍽️ Matbakhy App

Matbakhy is an Android application that helps users discover meals, manage favorites, plan meals on a calendar, and securely back up their data using Firebase.  
The app is built with a clean architecture approach and makes heavy use of **RxJava**, **Room**, and **Firebase**.

---

## 📱 Features

- 🔍 Browse meals by:
  - Search
  - Category
  - Country
  - Ingredients
- ❤️ Add / remove meals from favorites
- 📅 Plan meals on a calendar
- 💾 Offline-first support using Room database
- ☁️ Backup & restore meals using Firebase Realtime Database
- 🔐 Firebase Authentication
- ⚡ Reactive programming with RxJava2
- 🧱 Clean architecture (Data / Domain / Presentation)
- 🧩 MVP + Repository pattern

---

## 🛠 Tech Stack

- **Language:** Java  
- **Architecture:** MVP + Repository  
- **Reactive:** RxJava2, RxAndroid  
- **Local Storage:** Room Database  
- **Remote Storage:** Firebase Realtime Database  
- **Authentication:** Firebase Auth  
- **Networking:** Retrofit  
- **UI:** Material Components, RecyclerView, ChipGroup  
- **Threading:** Schedulers (IO / Main)

---

## 🗂 Project Structure

│
├── data
│ ├── datasources
│ │ ├── local # Room DB, DAO, LocalDataSource
│ │ └── remote # Firebase & API services
│ ├── model # Meal, FirebaseMeal, DTOs
│ └── repository # Data repositories
│
├── presentation
│ ├── view # Activities & Fragments
│ ├── viewmodel # ViewModels
│ └── adapter # RecyclerView Adapters
│
└── utils # Helpers & constants
