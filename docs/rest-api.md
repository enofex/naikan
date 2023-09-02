# Rest API

## Bearer Token

To ensure secure access to all endpoints within your application, the use of a bearer token is required.

Contact your project administrator and refer to the project-specific documentation to obtain a valid bearer token.

## **Projects**

### Create or Update

**Base URL:** `{schema}://{hostname}:{port}/api/public`

> Note: If the provided project name is found the project will be updated, if not it will be created.


1) **Method:** `POST`  **Endpoint:** `/projects`        

     **Description:** Creates or updates a project.
     
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

     | Response | Description |
   |---------|----------|
   | 200 | Successful. project was found with the project name, then it will be updated |
   | 201 | If the project was not found with the project name, then it will be created. |
     
    ```
     Location: {schema}://{hostname}:{port}/api/projects/{projectId}
     ```

### Update

**Endpoint:** `POST {schema}://{hostname}:{port}/api/public/projects/{projectId}`

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

**Endpoint:** `POST {schema}://{hostname}:{port}//api/public/projects/{projectId}/deployments`

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
Location: {schema}://{hostname}:{port}/api/projects/{projectId}/deployments/{index}
```
