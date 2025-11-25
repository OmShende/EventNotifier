# Event Notification System – Java Spring Boot (Gradle)

This project implements a multithreaded, queue-based **Event Notification System** that processes events asynchronously (EMAIL, SMS, PUSH) and sends callbacks upon completion.

The system is designed using:
- Spring Boot (Java 17)
- Gradle
- ExecutorService (multithreading)
- LinkedBlockingQueue (back-pressure & FIFO)
- Spring WebClient (async callbacks)
- Docker + Docker Compose
- JUnit + Mockito (unit tests)

---

## 📦 Architecture Overview

### 🧩 Components
- **EventController** — Accepts events & callbacks.
- **EventServiceImpl** — Routes events to appropriate processor.
- **AbstractEventProcessor** — Core multithreaded queue worker.
- **Email/Sms/Push Processors** — Specialized worker instances.
- **CallbackClient** — Sends callbacks using WebClient.

### ⚙️ Processing Flow
1. Client POSTs `/api/events`
2. EventService stores & forwards event to a processor.
3. Processor enqueues event into a bounded queue.
4. Worker threads pick & process events.
5. After processing, CallbackClient POSTs to `callbackUrl`
6. `/api/events/ack` receives callback.

---

## 🐳 Docker Setup

### 🔧 Dockerfile (multi-stage)
This project uses:
- JDK image for building
- Lightweight JRE for running

### ▶️ Build and Run with Docker

```bash
docker build -t event-notifier .
docker run -p 8080:8080 event-notifier
