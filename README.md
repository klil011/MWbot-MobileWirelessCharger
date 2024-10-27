# MWbot - Sistema di Gestione Parcheggio con Ricaricatori Wireless Mobili

## Descrizione del Progetto
**MWbot** è un'applicazione **Spring Boot** progettata per gestire un sistema di parcheggio smart dotato di ricaricatori wireless mobili per veicoli elettrici. 
Il sistema permette agli utenti di parcheggiare i veicoli e richiedere la ricarica tramite MWbot, gestendo prenotazioni e pagamenti. 
Supporta tre tipi di utenti: utenti base, utenti premium e amministratori e include un'interfaccia backend per l’interazione tramite strumenti di testing come Postman e MQTTX.

## Tecnologie Utilizzate
- **Linguaggio**: Java
- **Framework**: Spring Boot
- **Notifiche**: MQTT per la cordinazione tra i sensori dei parcheggi, MWbot e Backend
- **Persistenza**: Database PostgreSQL
- **Testing**: Postman, MQTTX
