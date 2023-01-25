# RemindAndInform

## Leverantör

Sundsvalls kommun

## Beskrivning
RemindAndInform är en tjänst där man kan registrera påminnelser som skickas ut vid angiven tidpunkt.


## Tekniska detaljer

### Integrationer
Tjänsten integrerar mot:

* Messaging

### Starta tjänsten

|Miljövariabel|Beskrivning|
|---|---|
|**Databasinställningar**||
|`SPRING_DATASOURCE_URL`|JDBC-URL för anslutning till databas|
|`SPRING_DATASOURCE_USERNAME`|Användarnamn för anslutning till databas|
|`SPRING_DATASOURCE_PASSWORD`|Lösenord för anslutning till databas|
*Inställningar för tjänsten Messaging**|
|`INTEGRATION_MESSAGING_URL`| API-URL till tjänsten Messaging|
|`SPRING_SECURITY_OATH2_CLIENT_REGISTRATION_MESSAGING_AUTHORIZATION-GRANT-TYPE`|Typ av authorisering för att hämta token. Ska vara "client_credentials".|
|`SPRING_SECURITY_OATH2_CLIENT_REGISTRATION_MESSAGING.CLIENT-ID`| OAuth2-klient-nyckel för Messaging |
|`SPRING_SECURITY_OATH2_CLIENT_REGISTRATION_MESSAGING.CLIENT-SECRET`| OAuth2-klient-id för Messaging |
|`SPRING_SECURITY_OATH2_CLIENT_PROVIDER_MESSAGING_TOKEN-URI`| URI för att hämta OAuth2-token för Messaging |


### Paketera och starta tjänsten
Applikationen kan paketeras genom:

```
./mvnw package
```
Kommandot skapar filen `api-remindandinform-<version>.jar` i katalogen `target`. Tjänsten kan nu köras genom kommandot `java -jar target/api-remindandinform-<version>.jar`.

### Bygga och starta med Docker
För att bygga en Docker-image:

```
docker build -f src/main/docker/Dockerfile -t api.sundsvall.se/ms-remindandinform:latest .
```

För att starta samma Docker-image i en container:

```
docker run -i --rm -p 8080:8080 api.sundsvall.se/ms-remindandinform
```

För att bygga och starta en container i sandbox mode:

```
docker-compose -f src/main/docker/docker-compose-sandbox.yaml build && docker-compose -f src/main/docker/docker-compose-sandbox.yaml up
```


## 
Copyright (c) 2021 Sundsvalls kommun