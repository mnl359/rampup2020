# Endava's RampUp 2020-1: Mail2Clients 

[![Build Status](https://cloud.drone.io/api/badges/mnl359/rampup2020/status.svg)](https://cloud.drone.io/mnl359/rampup2020)

This web application allows the user to send emails by using **JAVA 1.8** REST services.

## Software requirements

<p align="center">
  <img width="200" height="100" src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Maven_logo.svg/1200px-Maven_logo.svg.png">
  <img width="150" height="100" src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Tomcat-logo.svg/1200px-Tomcat-logo.svg.png">
  <img width="150" height="100" src="https://www.websodi.com/wp-content/uploads/2019/01/wordpress-smtp-ayarlari-539x330.jpg">
  <img width="150" height="100" src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Postgresql_elephant.svg/1200px-Postgresql_elephant.svg.png">
</p>

This aplication use a **Tomcat server** that runs .war artifact, a **SMTP server** to send the emails and a **Postgres database** where the emails sent are storaged. Also, the artifact are build with **Maven**.

For this case, you will need:

* Docker 
<img width="150" height="100" src="https://1000marcas.net/wp-content/uploads/2020/02/Docker-Logo.png">

* Docker-compose 
<img width="125" height="100" src="https://codeblog.dotsandbrackets.com/wp-content/uploads/2016/10/compose-logo.jpg">

## Usage

### Deploy
For deploy, the variables POSTGRES_USER, POSTGRES_PASSWORD, GMAIL_USER and GMAIL_PASSWORD must be set.
```sh
export POSTGRES_USER=admin_db
export POSTGRES_PASSWORD=<password of your choice>
export GMAIL_USER=<your gmail username>
export GMAIL_PASSWORD=<your gmail password>
docker-compose up --build
```
> **_NOTE:_**  You must enable less secure apps to access Gmail with this [Tutorial](https://hotter.io/docs/email-accounts/secure-app-gmail/).

### Test
In this examples, It is recommended to use the **Python 3** with the requests module to access the endpoints.

#### Initial service test 

After Tomcat has properly deployed the app you can test the service status calling this endpoint. 
```sh
http://localhost:8080/test
```

If it was deployed correctly the output should be like this:
```sh
Mail2CLients - Rest Service - Test Succeeded! 
```

#### Send an email

Once all the services required are running, you can test the webapp by accessing:
```sh
POST rest service
    http://localhost:8080/v1/emails

Parameters
    "subject" Suject of the email, it will be used on email inbox. Example: Hello I'm here!
    "content" Text content, please do not add javascript in this filed, mailboxs validate javascript conent and they will reject the email. 
    "recipients" List of emails addresses to send the email, if they are more that one separate them with ';'. Example: test@mail.com; test2@mail.com;.
```

```python
from requests import get, post

r = post("http://localhost:8080/v1/emails", data={'subject': 'Test', 'content': 'This is a test', 'recipients': 'test@example.com'})
print(r.text[:700] + '...')

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

#### List of emails logged

Another REST service include in the JAVA app is list all delivered emails.
```sh
GET rest service
    http://localhost:8080/v1/logger?startDate=&endDate=2017-12-01 00:00&onlyDelivered=false

Parameters
    "startDate" Initial date to search. Example: 2017-01-01 00:00
    "endDate" Final date to search. Example: 2017-12-01 00:00
    "onlyDelivered" It will filter the results by only deliverd emails. Example: false *To return all emails on log.
  ```

```python
from requests import get, post

r = get("http://localhost:8080/v1/logger?startDate=2020-01-01%2000:00&endDate=2021-01-01%2000:00&onlyDelivered=false")
print(r.text[:700] + '...')

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

#### List of emails logged by Serial or Subject

Search REST services included: By Serial or Subject. Filter by subject will return all the emails that contains the keywords sent.
 
```sh
GET rest services
    http://localhost:8080/v1/logger/suject/Docker Test
    http://localhost:8080/v1/logger/serial/1491359396709

Parameters
    "subject" Text to filter by. Example: Docker Test
    "serial" Generated id to filter by. Example: 1491359396709
  ```

```python
from requests import get, post

r = get("http://localhost:8080/v1/logger/subject/Docker%20Test")
print(r.text[:700] + '...')

s = get("http://localhost:8080/v1/logger/serial/1491359396709")
print(s.text[:700] + '...')
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

### App configuration 

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
