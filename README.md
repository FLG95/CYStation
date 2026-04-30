<div align="center">

<h1>CYStation</h1>

<p>
  <strong>Application web de gestion de stations — Projet ING1
</p>

<p>
  <img src="https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MariaDB-3.5.7-003545?style=flat-square&logo=mariadb&logoColor=white" />
  <img src="https://img.shields.io/badge/Thymeleaf-template%20engine-005F0F?style=flat-square&logo=thymeleaf&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Security-enabled-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/WebSocket-realtime-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/OpenAPI-3.0-85EA2D?style=flat-square&logo=swagger&logoColor=black" />
</p>

</div>

---

## 🛠 Stack technique

| Couche | Technologie |
|---|---|
| Backend | Java 25 · Spring Boot 4.0.5 |
| Frontend | HTML · CSS · JavaScript · Thymeleaf |
| Base de données | MariaDB (via Spring Data JPA) |
| Sécurité | Spring Security |
| Temps réel | Spring WebSocket |
| Documentation | SpringDoc OpenAPI 3.0 |
| Build | Gradle (Wrapper) |
| Mail | Spring Boot Mail |

---

## ✅ Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java 25+** — [Télécharger](https://jdk.java.net/25/)
- **MariaDB** — [Télécharger](https://mariadb.org/download/)
- **Git**

---

## 📦 Installation

```bash
# Cloner le dépôt
git clone https://github.com/FLG95/CYStation.git

# Se déplacer dans le répertoire
cd CYStation
```

---

## ⚙️ Configuration

Créez (ou modifiez) le fichier `src/main/resources/application.properties` avec vos paramètres :

```properties
# Base de données
spring.datasource.url=jdbc:mariadb://localhost:3306/cystation
spring.datasource.username=VOTRE_UTILISATEUR
spring.datasource.password=VOTRE_MOT_DE_PASSE
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Mail (optionnel)
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=VOTRE_EMAIL
spring.mail.password=VOTRE_MDP_EMAIL
```

---

## 🚀 Lancer l'application

```bash
# Avec le Gradle Wrapper (Linux / macOS)
./gradlew bootRun

# Avec le Gradle Wrapper (Windows)
gradlew.bat bootRun
```

L'application sera disponible sur **[http://localhost:8080](http://localhost:8080)**

---

