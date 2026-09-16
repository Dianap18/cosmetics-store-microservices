# Cosmetics Store Chain

This is a software application developed for a cosmetics store chain. The system uses a microservices architecture and supports four distinct types of users: client, employee, manager, and administrator.

## Technologies Used
* **Language & Framework:** Java 17 and Spring Boot.
* **Database:** MS SQL Server.
* **Architecture:** Microservices with synchronous REST API communication.
* **Tools:** IntelliJ IDEA, Postman for endpoint testing, and PlantUML/DrawIO for system diagrams.

## Features by User Role
* **Client (No authentication):** Can view the list of cosmetic products sorted by name and price, and search for specific products to see which stores have them in stock.
* **Employee:** Can view and filter products in their specific store, process sales (which lowers the stock), add or update local inventory, and export product lists in CSV, JSON, XML, or DOC formats.
* **Manager:** Can perform all client operations, plus filter products across the entire chain, add, modify, or delete products, export data, and view graphical statistics.
* **Administrator:** Can manage all user accounts (add, modify, delete, and filter by role) and configure automated notifications (via email or SMS) for account data changes.

## Setup and Installation

### Prerequisites
* Java 17
* MS SQL Server
* Maven

### Running the Application locally
1. Clone this repository to your local machine.
2. Navigate to each microservice folder (for example, `NotificationService` or `InventoryService`).
3. Open the `src/main/resources/application.properties` file for each service. Update the database URL, username, and password to match your local MS SQL Server setup.
4. If testing the notification features, add your local email SMTP or SMS API keys to the properties file.
5. Run the application using your IDE or via the command line using Maven.
