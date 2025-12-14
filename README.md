 Sports Assistant System

A desktop application for managing sports teams, players, coaches, matches, and statistics with PDF report generation.

 What It Does

Manage teams, players, coaches, matches, scores, and statistics all in one place. Generate professional PDF reports with player rankings and performance analysis.

 Requirements

- Java 8 or higher
- MySQL 5.7 or higher
- MySQL Connector and iText libraries

 Quick Setup

1. Clone this repository
2. Create MySQL database: `CREATE DATABASE sportsassistant_db;`
3. Run the schema script: `mysql -u root -p sportsassistant_db < database/schema.sql`
4. Update database credentials in `src/com/sportsassistant/config/DatabaseConfig.java`
5. Add required JAR files to `lib` folder
6. Compile and run: `java -cp ".:lib/*:src" com.sportsassistant.SportsAssistantApp`

 Default Login

Username: `admin` | Password: `1234`

Features

✅ Manage teams, players, coaches, matches, and scores  
✅ Track player statistics (goals, assists, matches played)  
✅ Generate 5 types of PDF reports  
✅ User-friendly tabbed interface
