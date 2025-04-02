# Betting Management System

Bu proje, çoklu bahis kuponu yönetimi için geliştirilmiş bir Spring Boot uygulamasıdır. Kullanıcılar, farklı spor müsabakaları için bahis oynayabilir ve birden fazla bahisi tek bir kuponda birleştirebilir.

##  Özellikler

- Canlı maç ve oran takibi
- Çoklu bahis kuponu oluşturma
- WebSocket ile gerçek zamanlı oran güncellemeleri
- Müşteri bazlı bahis limiti kontrolü
- Oran değişikliği geçmişi takibi (Append-Only)
- H2 veritabanı ile in-memory veri saklama
- Swagger UI ile API dokümantasyonu

##  Teknolojiler

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- Spring WebSocket
- H2 Database
- Lombok
- SpringDoc OpenAPI (Swagger)

## Başlangıç

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
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Djasypt.encryptor.password=secretKey"    
```

## API Dokümantasyonu

API dokümantasyonuna aşağıdaki URL'lerden erişebilirsiniz:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### API Endpoints

#### Event Management

- `GET /api/events` - Tüm canlı maçları listeler
- `POST /api/events` - Yeni canlı maç oluşturur

#### Betslip Management

- `POST /api/betslips` - Yeni bahis kuponu oluşturur

## Konfigürasyon

Temel konfigürasyon ayarları `application.properties` dosyasında bulunmaktadır:

```properties
# Server Configuration
server.port=8080

# Betting Configuration
app.betting.max-bets-per-event=500

# Scheduling Configuration
app.scheduling.odds-update-interval=1000

# Execution Time Threshold
app.execution.time-threshold=1000
```

## WebSocket Desteği

Oranların gerçek zamanlı güncellenmesi için WebSocket endpoint'i:

```
ws://localhost:8080/ws
```

Subscribe olunacak topic:
```
/topic/events
```

## Veritabanı

H2 Console'a erişim:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:bettingdb
- Username: sa
- Password: sa

# Betting System - Akış Dokümanı

## 1. Sistem Bileşenleri

### 1.1 Backend Servisler
- EventService: Maç ve oran yönetimi
- BetslipService: Bahis kuponu işlemleri
- WebSocketNotificationService: Gerçek zamanlı veri akışı

### 1.2 Veritabanı Tabloları
- events: Maç bilgileri ve güncel oranlar
- event_odds_history: Oran değişikliği geçmişi
- betslips: Bahis kuponları
- bets: Kupon detayları

### 1.3 İletişim Kanalları
- REST API: Bahis işlemleri için
- WebSocket: Gerçek zamanlı oran güncellemeleri için

## 2. Oran Güncelleme Akışı

### 2.1 Scheduled Job
```java
@Scheduled(fixedRateString = "${app.scheduling.odds-update-interval}")
public void updateRandomOdds() {
    //işlemler
}

```

### 2.2 Güncelleme Süreci
1. Job her saniye tetiklenir
2. Aktif maçlar optimistic lock ile getirilir (`@Version` field kullanılarak)
3. Her maç için:
    - Yeni oranlar ±%5 aralığında rastgele hesaplanır
    - Mevcut oranlar `event_odds_history` tablosuna kaydedilir
    - Event entity'si yeni oranlarla güncellenir
    - Optimistic locking ile versiyon kontrolü yapılır
    - WebSocket üzerinden client'lara bildirim gönderilir

### 2.3 WebSocket Bildirimi
```json
{
  "eventId": 1,
  "homeWinOdds": 2.15,
  "drawOdds": 3.40,
  "awayWinOdds": 3.25,
  "updateDate": "2024-03-15T10:30:15"
}
```

### 2.5 Optimistic Locking
Event entity'sinde versiyon kontrolü için:
```java
@Version
private Long version;
```

Bu yaklaşımla:
- Her oran değişikliği history tablosuna kaydedilir (Append-Only)
- Versiyon kontrolü ile eşzamanlı güncellemeler yönetilir
- Oran geçmişi sayesinde bahis işlemlerinde tutarlılık sağlanır
- WebSocket ile anlık bildirimler gönderilir

## 3. Bahis Kuponu Oluşturma Akışı

### 3.1 Client İsteği
```json
{
  "amount": 100,
  "multipleCount": 2,
  "bets": [
    {
      "eventId": 1,
      "selectedBetType": "HOME_WIN",
      "expectedOdds": 2.15
    }
  ]
}
```

### 3.2 Validasyon Süreci
1. Kupon tutarı kontrolü
    - Maksimum tutar: 10000 TL
    - Formül: amount * multipleCount <= 10000

2. Maç başına bahis limiti kontrolü
    - Maksimum: 500 bahis/maç/kullanıcı
    - Mevcut bahisler + yeni bahisler <= 500

3. Oran tutarlılığı kontrolü
    - İşlem başlangıç zamanı (validationTime) kaydedilir
    - Her bahis için:
        * Güncel oranlar kontrol edilir
        * Eğer oranlar değişmişse, validationTime anındaki oranlar history'den kontrol edilir
        * Beklenen oran ile uyuşmazsa işlem reddedilir

## 4. Örnek Senaryo

### 4.1 Başarılı Bahis Senaryosu
1. Client WebSocket bağlantısı açar
2. Sürekli oran güncellemelerini dinler
3. Kullanıcı 2.15 oranından Arsenal'e bahis yapmak ister
4. CreateBetslip isteği gönderilir
5. Backend:
    - İşlem başlangıç zamanını kaydeder
    - Oranları kontrol eder
    - Limitleri kontrol eder
    - Kuponu kaydeder
6. Başarılı yanıt döner

### 4.2 Başarısız Bahis Senaryosu
1. Client WebSocket bağlantısı açar
2. Kullanıcı 2.15 oranından bahis seçer
3. Tam bu sırada scheduled job oranı 2.20'ye günceller
4. Client eski oran (2.15) ile istek gönderir
5. Backend:
    - İşlem başlangıç zamanını kaydeder
    - Güncel oranın 2.20 olduğunu görür
    - History'den işlem başlangıcındaki oranı kontrol eder
    - Oran değişikliği tespit edilir
    - İşlemi reddeder
6. OddsChangedException yanıtı döner

## 5. Hata Senaryoları ve Çözümleri

### 5.1 Oran Değişikliği
```java
if (!event.validateOddsNotChanged(betRequest.getSelectedBetType(), 
        betRequest.getExpectedOdds(), validationTime)) {
    throw new OddsChangedException(betRequest.getEventId(), 
            betRequest.getExpectedOdds(), currentOdds);
}
```

### 5.2 Limit Aşımı
```java
if (totalExistingBets + request.getMultipleCount() > maxBetsPerEvent) {
    throw new IllegalStateException(
        String.format("Maximum bet limit (%d) exceeded for event ID: %d",
        maxBetsPerEvent, eventId));
}
```

## 6. Monitoring ve Logging

### 6.1 Aspect-Based Logging
```java
@Around("execution(* com.bilyoner.controller.*.*(..))")
public Object logRequestResponse(ProceedingJoinPoint joinPoint)
```

### 6.2 Performance Monitoring
```java
@Around("@within(org.springframework.stereotype.Service)")
public Object monitorServicePerformance(ProceedingJoinPoint joinPoint)
```

### 6.3 Exception Handling
```java
@AfterThrowing(pointcut = "execution(* com.bilyoner..*.*(..))")
public void logException(JoinPoint joinPoint, Throwable exception)
```

## Güvenlik

### Jasypt Şifreleme
Uygulama, veritabanı şifresini encrypted olarak application.properties dosyasında saklamaktadır.

#### Kullanım
1. Şifre: sa
2. SecretKey: secretKey
4. Uygulamayı çalıştırırken secret key'i vm parametre olarak geçmeliyiz:
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Djasypt.encryptor.password=secretKey"

veya

Spring Boot Edit Configuration -> VM Options: -Djasypt.encryptor.password=secretKey
değerini eklemeliyiz.
```