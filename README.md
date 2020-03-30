# Endava's RampUp 2020-1
Interns Rampup January 2020

## Mail2Clients 

[![Build Status](https://cloud.drone.io/api/badges/mnl359/rampup2020/status.svg)](https://cloud.drone.io/mnl359/rampup2020)

This web application will let you send emails by using **JAVA 1.8** REST services.     
You need to use MAVEN to create the .war actifact that runs over a *Tomcat* server.       
An additional logging service is included, the emails sent are storaged on a *Postgres* database.    

## Services required
- Tomcat 
- SMTP 
- Postgres 

## App configuration 

- **Application.properties**     
 https://github.com/zelfick/rampup2020/blob/master/mail2clients/src/main/resources/application.properties
```sh
    #Database connection information
    database.xyz 
    ...

    #This app uses Hibernate, here are the configuration parameters
    hibernate.xyz

    #SMTP connection information, please include yours email setup
    mail.xyz

    #Included as well Velocity library to send templated emails
    velocity.template 
 ```

- **Database initial script**     
This table is required prior the application's start, so please prepare the database and run it before any use. (Script included into the repository).
https://github.com/zelfick/rampup2020/blob/master/mail2clients/db_initial_script.sql
```sh
CREATE TABLE IF NOT EXISTS email_logger (
  log_id            SERIAL                      NOT NULL,
  log_serial        VARCHAR(100)                NOT NULL,
  log_subject       VARCHAR(100)                NOT NULL,
  log_delivered     BOOL                        NOT NULL DEFAULT 'false',
  log_content       VARCHAR(2500)               NOT NULL,
  log_timestamp     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp
) WITH (OIDS =FALSE);
ALTER TABLE email_logger OWNER TO admin_db;
DROP DATABASE postgres;
```

## Initial service test 

After Tomcat has properly deployed the app you can test the service status calling this endpoint.
```sh
http://localhost:8080/test
  ```

If it was deployed correctly the output should be like this:
```sh
Mail2CLients - Rest Service - Test Succeeded! 
```

## Send an email

Once all the services required are running, you can test the webapp by accessing:
```sh
POST rest service
    http://localhost:8080/v1/emails

Parameters
    "subject" Suject of the email, it will be used on email inbox. Example: Hello I'm here!
    "content" Text content, please do not add javascript in this filed, mailboxs validate javascript conent and they will reject the email. 
    "recipients" List of emails addresses to send the email, if they are more that one separate them with ';'. Example: test@mail.com; test2@mail.com;.
  ```

If the request was process correctly the output should be like this:
```sh
{
  "code": 202,
  "status": "ACCEPTED",
  "url": "[POST] http://localhost:8080/v1/emails?subject=Subject%20test&content=Testing%20content%20on%20email&recipients=mail@mail.com;",
  "message": "Email task was accepted and sent to SMTP",
  "data": {
    "serial": 1491218659389,
    "from": "127.0.0.1",
    "mailsList": "test@mail.com;",
    "subject": "Subject test",
    "content": "Testing content on email",
    "warnings": null,
    "malformedDirectEmails": null
  }
} 
```

## List of emails logged

Another REST service include in the JAVA app is list all delivered emails
```sh
GET rest service
    http://localhost:8080/v1/logger?startDate=&endDate=2017-12-01 00:00&onlyDelivered=false

Parameters
    "startDate" Initial date to search. Example: 2017-01-01 00:00
    "endDate" Final date to search. Example: 2017-12-01 00:00
    "onlyDelivered" It will filter the results by only deliverd emails. Example: false *To return all emails on log.
  ```

This is the expected output:

```sh
{
  "code": 200,
  "status": "OK",
  "url": "[GET] http://localhost:8080/v1/logger?startDate=2017-01-01%2000:00&endDate=2017-12-01%2000:00&onlyDelivered=false",
  "message": "Mail2Clients request response.",
  "data": [
    {
      "serial": "1491359400489",
      "subject": "Hello Mail2Clients",
      "delivered": true,
      "content": "Working as a charm!",
      "versionDate": "04/03/2017 05:08:51"
    }
  ]
}
```

## List of emails logged by Serial or Subject

Search REST services included: By Serial or Subject. Filter by subject will return all the emails that contains the keywords sent.
 
```sh
GET rest services
    http://localhost:8080/v1/logger/suject/Docker Test
    http://localhost:8080/v1/logger/serial/1491359396709

Parameters
    "subject" Text to filter by. Example: Docker Test
    "serial" Generated id to filter by. Example: 1491359396709
  ```

This is the expected output:

```sh
{
  "code": 200,
  "status": "OK",
  "url": "[GET] http://localhost:8080/v1/logger/subject/Docker%20Test",
  "message": "Mail2Clients request response.",
  "data": [
    {
      "serial": "1491359396709",
      "subject": "Docker Test",
      "delivered": true,
      "content": "Email build and send to SMTP",
      "versionDate": "04/05/2017 02:29:56"
    },
    {
      "serial": "1491359400486",
      "subject": "Docker Test 1",
      "delivered": true,
      "content": "Email build and send to SMTP",
      "versionDate": "04/05/2017 02:30:00"
    },
    {
      "serial": "1491359403198",
      "subject": "Docker Test 2",
      "delivered": true,
      "content": "Email build and send to SMTP",
      "versionDate": "04/05/2017 02:30:03"
    }
  ]
}
```
