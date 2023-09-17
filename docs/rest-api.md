# Rest API

## Bearer Token

To ensure secure access to all endpoints within your application, the use of a bearer token is required.

Contact your project administrator and refer to the project-specific documentation to obtain a valid bearer token.

## OpenAPI definition

The OpenAPI description is available at the following url:

`{schema}://{hostname}:{port}/api/public/v3/api-docs`

## Base URL

The base URL is the root endpoint where all API requests originate:

`{schema}://{hostname}:{port}/api/public`

**{schema}** Refers to the protocol or scheme used.

**{hostname}** Represents the hostname or domain name of the server hosting your API.

**{port}** Signifies the port number through which your API is accessible.

**/api/public** Specifies the path to the public API endpoints.

## **Projects**

### Create or Update

!!! note "Project name"

     If the provided project name is found the project will be updated, if not it will be created.

**Endpoint:** `POST /projects`

**Description:** Creates or update a project.

**Request Headers:**

- `Authorization`: Bearer token for authentication
- `Content-Type`: application/json

**Request Body:**

```json
{
  "specVersion": "1.0",
  "bomFormat": "Naikan",
  "project": {
    "name": "Naikan III"
  }
}
```

**Response:**

If the project was found with the project name, then it will be updated.

``` title="HTTP Status: 200 (Ok)"
HTTP/1.1 200
```

If the project was not found with the project name, then it will be created.

``` title="HTTP Status: 201 (Created)"
HTTP/1.1 201
Location: /projects/{projectId}
```

### Update

**Endpoint:** `POST /projects/{projectId}`

**Description:** Update a project.

**Request Headers:**

- `Authorization`: Bearer token for authentication
- `Content-Type`: application/json

**Path Parameters:**

- `projectId`: ID of the project which should be updated

**Request Body:**

```json
{
  "specVersion": "1.0",
  "bomFormat": "Naikan",
  "project": {
    "name": "Naikan III"
  }
}
```

**Response:**

If the project was found with the project id, then it will be updated.

``` title="HTTP Status: 200 (Ok)"
HTTP/1.1 200
```

## **Deployments**

### Create

**Endpoint:** `POST /projects/{projectId}/deployments`

**Description:** Creates a deployment for a specific project.

**Request Headers:**

- `Authorization`: Bearer token for authentication
- `Content-Type`: application/json

**Path Parameters:**

- `projectId`: ID of the project to create the deployment for

**Request Body:**

```json
{
  "version": "1.0.10",
  "environment": "Integration"
}
```

**Response:**

``` title="HTTP Status: 201 (Created)"
HTTP/1.1 201
Location: /{projectId}/deployments/{index}
```
