# Getting started

The preferred method for production environments is to use  `docker-compose.yml ` with a corresponding MongoDB and LDAP instance.

## Deploying Docker Container

Getting started is made effortless and rapid by utilizing Docker for deployment, without the need for any prerequisites except for a contemporary Docker version.

### Prerequisites

* Docker
* Docker Compose 
* Running MongoDB server
* Running LDAP server

### Installation

After downloading the docker-compose.yml file, review it and identify the service for which you want to adjust the configuration parameters.

=== "Docker Compose"

    ``` sh
    curl -LO https://naikan.io/docker-compose.yml
    docker-compose up -d
    ```

!!! note "Version"

    The 'latest' tag available on Docker Hub consistently represents the most up-to-date stable release.

### Configuration

The Naikan container can be configured using any of the available configuration properties:

#### MongoDB 

##### Via URI

[Connection String URI Format](https://www.mongodb.com/docs/manual/reference/connection-string/)

| Property                             | Default   | Description                           |
|:-------------------------------------|:----------|:--------------------------------------|
| `NAIKAN_MONGODB_URI`                 |           | Mongo database URI.                   | 
| `NAIKAN_MONGODB_DATABASE`            | `naikan`  | Database name. Override URI database. |

##### Via specific property's

| Property                          | Default     | Description                         |
|:----------------------------------|:------------|:------------------------------------|
| `NAIKAN_MONGODB_HOST`             | `localhost` | Mongo server host.                  |   
| `NAIKAN_MONGODB_PORT`             | `27017`     | Mongo server port.                  |
| `NAIKAN_MONGODB_DATABASE`         | `naikan`    | Database name.                      |
| `NAIKAN_MONGODB_REPLICA_SET_NAME` |             | Replica set name.                   |
| `NAIKAN_MONGODB_USERNAME`         |             | Login user of the mongo server.     |
| `NAIKAN_MONGODB_PASSWORD`         |             | Login password of the mongo server. |

#### Common property

| Property                             | Default     | Description                          |
|:-------------------------------------|:------------|:-------------------------------------|
| `NAIKAN_MONGODB_TRANSACTION_ENABLED` | `true`      | Enable transaction.                  |

#### LDAP

| Property                                | Default                | Description                                                                                                              |
|:----------------------------------------|:-----------------------|:-------------------------------------------------------------------------------------------------------------------------|
| `NAIKAN_LDAP_ENABLED`                   | `true`                 | Enable LDAP authentication.                                                                                              |
| `NAIKAN_LDAP_SERVER_URL`                | `ldap://localhost:389` | LDAP URL of the server.                                                                                                  |
| `NAIKAN_LDAP_BASEDN`                    | `dc=example,dc=com`    | Base suffix from which all operations should originate.                                                                  |
| `NAIKAN_LDAP_BIND_USERNAME`             |                        | Login username of the server.                                                                                            |
| `NAIKAN_LDAP_BIND_PASSWORD`             |                        | Login password of the server.                                                                                            |
| `NAIKAN_LDAP_ANONYMOUS_READ_ONLY`       | `true`                 | Whether read-only operations should use an anonymous environment. Disabled by default unless a username is set.          | 
| `NAIKAN_LDAP_USER_DN_PATTERNS`          |                        | Sets the pattern which will be used to supply a DN for the user. The pattern should be the name relative to the root DN. |
| `NAIKAN_LDAP_USERS_SEARCH_BASE`         |                        | The base used in the user search.                                                                                        |
| `NAIKAN_LDAP_USERS_SEARCH_FILTER`       |                        | The filter expression used in the user search.                                                                           |
| `NAIKAN_LDAP_ACTIVE_DIRECTORY_DOMAIN`   |                        | The domain name. Must be provided for Active Directory.                                                                  |

#### CORS Headers

| Property                        | Default                                            | Description                                                                                             |
|:--------------------------------|:---------------------------------------------------|:--------------------------------------------------------------------------------------------------------|
| `NAIKAN_CORS_ENABLED`           | `true`                                             | Enable CORS.                                                                                            |   
| `NAIKAN_CORS_ALLOW_ORIGIN`      | `*`                                                | A list of origins for which cross-origin requests are allowed.                                          |
| `NAIKAN_CORS_ALLOW_METHODS`     | `GET`, `PUT`, `POST`, `PATCH`, `DELETE`, `OPTIONS` | The HTTP methods to allow, e.g. `GET`, `POST`, `PUT`...                                                 |
| `NAIKAN_CORS_ALLOW_HEADERS`     | `*`                                                | Set the list of headers that a pre-flight request can list as allowed for use during an actual request. |
| `NAIKAN_CORS_ALLOW_CREDENTIALS` | `true`                                             | Whether user credentials are supported.                                                                 |
| `NAIKAN_CORS_MAX_AGE`           | `1800`                                             | Configure how long, as a duration, the response from a pre-flight request can be cached by clients.     |

#### JVM

| Property               | Default | Description                                                                       |
|:-----------------------|:--------|:----------------------------------------------------------------------------------|
| `EXTRA_JAVA_OPTIONS`   |         | To provide more JVM arguments to the JVM, <br/>i.e. `-XX:ActiveProcessorCount=4`  |

