# Installation

## Prerequisites

Before you can begin with the installation, make sure you have the following prerequisites installed on your system:

* Git
* Java
* Maven
* Docker Desktop
* Node.js
* Angular CLI

## Clone the repository

Use git to clone the Naikan repository by running the following command:

`git clone git@github.com:enofex/naikan.git`

## Install dependencies

Use npm to install the required dependencies for Naikan by running the following command:

1. Switch to the `naikan/naikan-client` directory
2. `npm install`

## Start the frontend

Use npm to start the Naikan server by running the following command:

`ng serve`

## Start the backend

When you launch the `NaikanApplication` with the `dev` profile, it will automatically start Docker Compose using the configuration found in `development/docker/compose.yml`. 

The profile can be set in our IDE or passed via a JVM system parameter:

`-Dspring.profiles.active=dev`

These will initialize MongoDB and OpenLdap with sample data from:

*  `development/docker/boms.json`
*  `development/docker/tokens.json`
*  `development/docker/users.ldif`

## OpenLDAP Users

The `users.ldif` file located in `development/docker` contains two pre-configured users, `Admin` and `John Rambo`. You can use the following credentials to log in to Naikan:

```
Admin:
  Username: admin
  Password: 123
  Role: admin

John Rambo:
  Username: jnrambo
  Password: 123
  Role: user
```
