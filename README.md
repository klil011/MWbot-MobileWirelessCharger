# MWbot - Sistema di Gestione Parcheggi e Ricariche

**MWbot** è un'applicazione basata su **Spring Boot** progettata per la gestione dei parcheggi e delle ricariche per veicoli elettrici.  
Il sistema consente di gestire le richieste di sosta e ricarica all'interno dell'area di parcheggio.

Le ricariche vengono effettuate da robot a induzione (**MWbot**), che erogano energia ai veicoli che ne fanno richiesta.  
Oltre al servizio *on-demand*, il sistema offre agli utenti premium la possibilità di **prenotare** sessioni di ricarica o sosta, garantendo un'esperienza più flessibile, compatibilmente con la disponibilità del parcheggio.

---

## Prerequisiti

Prima di installare ed eseguire MWbot, è necessario disporre dei seguenti componenti:

- **Java 17+** – È richiesto l'SDK di Java 17 o superiore.
- **Maven** – Utilizzato per la gestione delle dipendenze e la compilazione del progetto.
- **PostgreSQL** – Utilizzato come database per la persistenza dei dati.
- **Mosquitto 2.0.18** – Per la comunicazione IoT tra i sensori di parcheggio, l'MWbot e il servizio Backend.

---

## Installazione

### 1. Clonare il repository

```bash
git clone https://github.com/tuo-nome/MWbot-MobileWirelessCharger.git

cd MWbot
```

---

### 2. Configurare il database

Modificare il file `application.properties` o `application.yml` per impostare i dettagli del database PostgreSQL (se si utilizza un DB diverso occorre modificare i driver di configurazione):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/MW_DB
spring.datasource.username=#####
spring.datasource.password=#####
spring.datasource.driver-class-name=org.postgresql.Driver

spring.data.jdbc.repositories.enabled=true
```

---

### 3. Compilare e eseguire l'applicazione

```bash
mvn spring-boot:run
```

---

### 4. Istruzioni aggiuntive

I sensori di parcheggio e il bot di ricarica sono simulati per facilitare i test e lo sviluppo.  
È possibile generare eventi di simulazione inviando messaggi MQTT tramite MQTTX o direttamente da linea di comando.  
Ad esempio, per segnalare lo stato di un sensore di parcheggio:

```bash
mosquitto_pub -h localhost -t "Parcheggio/Posto/1" -m "libero"
```

Questo comando pubblica un messaggio sul topic MQTT `Parcheggio/Posto/1`, indicando che il posto è libero e simulando l'uscita di un veicolo.

Per istruzioni dettagliate sull'utilizzo del sistema, consulta il file `istruzioniDettagliate.pdf`

---

## API Principali

L'applicazione espone diverse API REST per la gestione di:

- Parcheggi
- Ricariche
- Prenotazioni

Una volta avviato il server, puoi consultare la documentazione interattiva all’indirizzo:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Architettura

L'applicazione segue un pattern architetturale a strati, tipico delle applicazioni Spring Boot.

Lo stack tecnologico utilizzato:

- **Spring Boot** per il backend
- **JDBC** per la persistenza dei dati
- **MQTT** per la comunicazione IoT
