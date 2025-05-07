# 📘 Consultify

**Consultify** is a JavaFX desktop application designed to facilitate student-lecturer consultations with features like booking management, feedback, and request approvals. It uses local text files for storage, making it lightweight and easy to run without external databases.

## 🚀 Installation & Running

Make sure you have the following installed:

- Java 11 or higher
- Maven

Then, run the following commands:

```bash
mvn install
mvn javafx:run -DskipTests
```


## 👤 Testing Credentials

You can log in using the following sample accounts:

### 1. Student
- **Username**: `student`  
- **Password**: `student`

### 2. Lecturer
- **Username**: `lecturer`  
- **Password**: `lectuer` *(Note: Typo intended? Double-check spelling)*


## 🎯 Features

- ✅ Book available time slots
- 🔄 Manage upcoming bookings (cancel or reschedule)
- 🕓 View consultation history
- 📝 Submit feedback after consultations
- 📁 Uses local text file as a database (no external DB required)
- 👨‍🏫 Lecturer panel to approve or deny student consultation requests
- 📂 File-Based Storage - all data is stored in text files for ease of portability. No need for external database setup.
