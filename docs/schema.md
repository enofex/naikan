# Schema

You can find the project schema JSON files in the following
location: [Naikan Model - Project Schema Files](https://github.com/enofex/naikan-model/tree/main/src/main/resources)

These schema files define the structure and properties of the project data in a standardized JSON
format. By using these schemas, you can ensure consistency and validation of the project information
stored in JSON files. Please navigate to the provided link to access the project schema JSON files
or the last one below.

!!! note "Required properties"

     The **project name**, **specVersion** and **bomFormat** are required. All others are optional.

``` json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "specVersion": {
      "enum": [
        "1.0"
      ]
    },
    "bomFormat": {
      "enum": [
        "Naikan"
      ]
    },
    "timestamp": {
      "type": "string"
    },
    "project": {
      "type": "object",
      "properties": {
        "name": {
          "type": "string"
        },
        "url": {
          "type": "string"
        },
        "repository": {
          "type": "string"
        },
        "packaging": {
          "type": "string"
        },
        "groupId": {
          "type": "string"
        },
        "artifactId": {
          "type": "string"
        },
        "version": {
          "type": "string"
        },
        "description": {
          "type": "string"
        },
        "notes": {
          "type": "string"
        }
      },
      "required": [
        "name"
      ]
    },
    "organization": {
      "type": "object",
      "properties": {
        "name": {
          "type": "string"
        },
        "url": {
          "type": "string"
        },
        "department": {
          "type": "string"
        },
        "description": {
          "type": "string"
        }
      }
    },
    "integrations": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "location": {
              "type": "string"
            },
            "description": {
              "type": "string"
            },
            "tags": {
              "type": "array",
              "items": [
                {
                  "type": "string"
                }
              ]
            }
          }
        }
      ]
    },
    "teams": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "description": {
              "type": "string"
            }
          }
        }
      ]
    },
    "developers": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "title": {
              "type": "string"
            },
            "department": {
              "type": "string"
            },
            "email": {
              "type": "string"
            },
            "phone": {
              "type": "string"
            },
            "organization": {
              "type": "string"
            },
            "organizationUrl": {
              "type": "string"
            },
            "timezone": {
              "type": "string"
            },
            "description": {
              "type": "string"
            },
            "roles": {
              "type": "array",
              "items": [
                {
                  "type": "string"
                },
                {
                  "type": "string"
                }
              ]
            }
          }
        }
      ]
    },
    "contacts": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "title": {
              "type": "string"
            },
            "email": {
              "type": "string"
            },
            "phone": {
              "type": "string"
            },
            "description": {
              "type": "string"
            },
            "roles": {
              "type": "array",
              "items": [
                {
                  "type": "string"
                },
                {
                  "type": "string"
                }
              ]
            }
          }
        }
      ]
    },
    "technologies": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "version": {
              "type": "string"
            },
            "description": {
              "type": "string"
            },
            "tags": {
              "type": "array",
              "items": [
                {
                  "type": "string"
                }
              ]
            }
          }
        }
      ]
    },
    "licenses": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "url": {
              "type": "string"
            },
            "description": {
              "type": "string"
            }
          }
        }
      ]
    },
    "documentations": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "location": {
              "type": "string"
            },
            "description": {
              "type": "string"
            },
            "tags": {
              "type": "array",
              "items": [
                {
                  "type": "string"
                }
              ]
            }
          }
        }
      ]
    },
    "integrations": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "name": {
              "type": "string"
            },
            "url": {
              "type": "string"
            },
            "description": {
              "type": "string"
            },
            "tags": {
              "type": "array",
              "items": [
                {
                  "type": "string"
                }
              ]
            }
          }
        }
      ]
    },
    "tags": {
      "type": "array",
      "items": [
        {
          "type": "string"
        }
      ]
    },
    "deployments": {
      "type": "array",
      "items": [
        {
          "type": "object",
          "properties": {
            "environment": {
              "type": "string"
            },
            "location": {
              "type": "string"
            },
            "version": {
              "type": "string"
            },
            "timestamp": {
              "type": "string"
            }
          }
        }
      ]
    }
  },
  "required": [
    "specVersion",
    "bomFormat",
    "project"
  ]
}
```