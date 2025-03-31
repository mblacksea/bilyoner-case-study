# Betting Management System

Bu proje, çoklu bahis kuponu yönetimi için geliştirilmiş bir Spring Boot uygulamasıdır. Kullanıcılar, farklı spor müsabakaları için bahis oynayabilir ve birden fazla bahisi tek bir kuponda birleştirebilir.

## 🚀 Özellikler

- Canlı maç ve oran takibi
- Çoklu bahis kuponu oluşturma
- WebSocket ile gerçek zamanlı oran güncellemeleri
- Müşteri bazlı bahis limiti kontrolü
- H2 veritabanı ile in-memory veri saklama
- Swagger UI ile API dokümantasyonu

## 🛠 Teknolojiler

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- Spring WebSocket
- H2 Database
- Lombok
- SpringDoc OpenAPI (Swagger)

## 🏃‍♂️ Başlangıç

### Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/yourusername/bilyoner-case-study.git
cd bilyoner-case-study
```

2. Maven ile projeyi derleyin:
```bash
mvn clean install
```

3. Uygulamayı çalıştırın:
```bash
mvn spring-boot:run
```

## 📚 API Dokümantasyonu

API dokümantasyonuna aşağıdaki URL'lerden erişebilirsiniz:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### 🎯 API Endpoints

#### Event Management

- `GET /api/events` - Tüm canlı maçları listeler
- `POST /api/events` - Yeni canlı maç oluşturur

#### Betslip Management

- `POST /api/betslips` - Yeni bahis kuponu oluşturur

## ⚙️ Konfigürasyon

Temel konfigürasyon ayarları `application.properties` dosyasında bulunmaktadır:

```properties
# Server Configuration
server.port=8080

# Betting Configuration
app.betting.max-bets-per-event=500

# Transaction Configuration
spring.transaction.default-timeout=2s

# Scheduling Configuration
app.scheduling.odds-update-interval=1000
```

## 🌐 WebSocket Desteği

Oranların gerçek zamanlı güncellenmesi için WebSocket endpoint'i:

```
ws://localhost:8080/ws
```

Subscribe olunacak topic:
```
/topic/events
```

## 🗄️ Veritabanı

H2 Console'a erişim:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:bettingdb
- Username: sa
- Password: as