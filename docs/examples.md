# Examples

!!! note "Required properties"

     The **project name**, **specVersion** and **bomFormat** are required. All others are optional.

## Minimum example

``` json
{
  "specVersion": "1.0",
  "bomFormat": "Naikan",
  "project": {
    "name": "Naikan III"
 }
}
```

## Full example

``` json
{
  "specVersion": "1.0",
  "bomFormat": "Naikan",
  "timestamp": "2022-12-29T08:29:10.079226",
  "project": {
    "name": "Naikan I",
    "inceptionYear" : "2003",
    "url": "https://naikan.io",
    "repository": "https://github.com/enofex/naikan",
    "packaging": "jar",
    "groupId": "com.enofex",
    "artifactId": "naikan-core",
    "version": "1.0.0",
    "description": "Naikan core module",
    "notes": "Naikan notes"
  },
  "organization": {
    "name": "Naikan",
    "url": "https://naikan.io",
    "department": "Software department",
    "description": "Company projects at a glance"
  },
  "environments": [
    {
      "name": "Staging",
      "location": "staging.naikan.io",
      "description": "Staging description",
      "tags": [
        "Staging"
      ]
    },
    {
      "name": "Production",
      "location": "naikan.io",
      "description": "Production description"
    }
  ],
  "teams": [
    {
      "name": "Naikan Team",
      "description": "Naikan Core Team"
    }
  ],
  "developers": [
    {
      "name": "Trev Cooksey",
      "username" : "cytrev",
      "title": "Principal Software Engineer",
      "department": "Naikan Software Engineering",
      "email": "tcooksey1@geocities.jp",
      "phone": "1212-12-90999",
      "organization": "Edgetag",
      "organizationUrl": "https://www.edgetag.tech",
      "timezone": "America/New_York",
      "description": "Best developer!",
      "roles": [
        "architect",
        "developer"
      ]
    }
  ],
  "contacts": [
    {
      "name": "John Doe",
      "title": "Product Owner",
      "email": "jdoe@example.com",
      "phone": "461-355-2912",
      "description": "Responsible for this project",
      "roles": [
        "PO"
      ]
    }
  ],
  "technologies": [
    {
      "name": "Java",
      "version": "19",
      "description": "Best programming language",
      "tags": [
        "backend"
      ]
    },
    {
      "name": "Angular",
      "version": "15.0",
      "description": "React or Angular",
      "tags": [
        "frontend"
      ]
    }
  ],
  "licenses": [
    {
      "name": "Apache-2.0",
      "url": "https://www.apache.org/licenses/LICENSE-2.0.txt",
      "description": "Or MIT license?"
    }
  ],
  "documentations": [
    {
      "name": "Architecture overview",
      "location": "naikan.io/arch24",
      "description": "ARC24",
      "tags": [
        "Architecture",
        "ARC24"
      ]
    },
    {
      "name": "Technical debt",
      "location": "wiki.naikan.io/techdebt",
      "description": "Should be reduced!"
    }
  ],
  "integrations": [
    {
      "name": "Bitbucket",
      "url": "http://127.0.0.1/bitbucket/naikan",
      "description": "Scm description",
      "tags": [
        "Scm"
      ]
    },
    {
      "name": "Bamboo",
      "url": "http://127.0.0.1:8080/bamboo/naikan",
      "description": "Ci description"
    },
    {
      "name": "Jira",
      "url": "http://127.0.0.1/jira/naikan",
      "description": "JDD"
    },
    {
      "name": "SonarQube",
      "url": "http://127.0.0.1/sonar/naikan",
      "description": "Great tools!",
      "tags": [
        "Security"
      ]
    },
    {
      "name": "DependencyTrack",
      "url": "http://127.0.0.1/dtrack/naikan",
      "description": "Must have!",
      "tags": [
        "Security"
      ]
    }
  ],
  "tags": [
    "web",
    "intern"
  ],
  "deployments": [
    {
      "environment": "Staging",
      "location": "staging.naikan.io",
      "version": "1.0.0",
      "timestamp": "2022-12-28T08:29:10.079226"
    },
    {
      "environment": "Production",
      "location": "naikan.io",
      "version": "1.0.1"
    }
  ]
}
```
