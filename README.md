# The naikan module ![maven workflow](https://github.com/enofex/naikan/actions/workflows/maven.yml/badge.svg) [![](https://img.shields.io/badge/Java%20Version-20-orange)](/pom.xml)

## Prerequisites

* Git
* Java
* Docker
* Node.js
* Angular CLI

## Installation

### Clone the repository

Use git to clone the Naikan repository by running the following command:

`git clone git@github.com:naikan-projects/naikan.git`

### Install dependencies

Use npm to install the required dependencies for Naikan by running the following command:

1. Switch to the `naikan-client` directory
2. `npm install`

### Start the server

Use npm to start the Naikan server by running the following command:

`ng serve`

## Starting all dependencies as docker compose automatically

When you start the `NaikanApplication`, with the profile `dev` then the application will start
docker compose
`development/docker/compose.yml` automatically. 

These will start MongoDB and OpenLdap and init both with sample data from:

*  `development/docker/boms.json`
*  `development/docker/tokens.json`
*  `development/docker/users.ldif`

### OpenLDAP Users

The `LDIF` file that we will use, `development/docker/users.ldif`. Basically, it has two
users (`Admin`, `John Rambo`). With this you are able to log in to Naikan.

```
Admin > username: admin, password: 123, role: admin
John Rambo > username: jnrambo, password: 123, role: user
```

## Contributing

If you want to contribute to this project, then follow please
these [instructions](CONTRIBUTING.md).

## Website

Visit the [Naikan](https://naikan.io) Website for general information, demos and documentation.
