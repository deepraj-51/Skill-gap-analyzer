# 🎯 SkillGap Analyzer

SkillGap Analyzer is an adaptive, full-stack web application designed to help students and professionals identify their skill gaps against industry-standard job roles and receive personalized, actionable learning recommendations.

Built as a **Minor Project for Jabalpur Engineering College**.

## ✨ Features

- **🔐 Secure Authentication**: JWT-based email/password authentication and **Google OAuth2** one-tap sign-in.
- **📊 Adaptive Skill Profiling**: Users can add skills and rate their proficiency (1-5).
- **💼 Role Library**: Pre-configured tech roles (Full Stack Developer, Data Scientist, DevOps, etc.) with strict skill requirements.
- **📈 Rule-Based Gap Analysis Engine**: Compares user skills with role requirements to categorize skills as Matched, Partial, or Missing.
- **🤖 AI Learning Path Generator**: Integrates with **Google Gemini AI** to generate a dynamic, week-by-week learning roadmap based on exact gap analysis results.
- **📉 Visual Dashboards**: Interactive charts using Chart.js to clearly display readiness scores and skill distributions.
- **🎨 Premium UI/UX**: Fully responsive, dark-themed modern interface with beautiful micro-animations and smooth transitions.

## 🛠️ Tech Stack

**Frontend**
- React 18 + Vite
- React Router DOM (v6)
- Axios & Chart.js
- `@react-oauth/google` for Google Sign-In
- Vanilla CSS with a responsive Design System

**Backend**
- Java 17 + Spring Boot 3
- Spring Security + JJWT (JSON Web Tokens)
- Spring WebFlux (for asynchronous AI API calls)
- Spring Data JPA + Hibernate
- Google API Client (for OAuth verification)

**Database & AI**
- PostgreSQL / MySQL (Relational configuration ready)
- Google Gemini AI (gemini-2.5-flash)

## 🚀 Local Setup Instructions

### Prerequisites
- Node.js (v16+)
- Java (v17+)
- Maven (v3.6+)
- Database: MySQL or PostgreSQL server running locally
- Google Cloud Console: OAuth Web Client ID
- Google AI Studio: Gemini API Key

### Backend Setup
1. Open the `backend` directory:
   ```bash
   cd backend
   ```
2. Configure your environment variables in `src/main/resources/application.properties`:
   - Database URL, username, and password (`spring.datasource.*`).
   - `jwt.secret` (Base64 encoded string) and `jwt.expiration`.
   - `gemini.api.key` (From Google AI Studio).
   - `google.client.id` (From Google Cloud Console).
3. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   *The backend will run on `http://localhost:8080`. Flyway or `ddl-auto` will automatically create the necessary database tables.*

### Frontend Setup
1. Open the `frontend` directory:
   ```bash
   cd frontend
   ```
2. Install the necessary dependencies:
   ```bash
   npm install
   ```
3. Update your Google Client ID in `src/main.jsx`:
   ```javascript
   const GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID_HERE";
   ```
4. Start the Vite development server:
   ```bash
   npm run dev
   ```
   *The frontend will run on `http://localhost:5173`.*

## 👨‍💻 Team
- Deepraj Thakur
- Alok Barekar
- Roshan Ahirwar
