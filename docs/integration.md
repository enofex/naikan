## Prerequisites

* **Access to a *Naikan* instance**
* **Access to a REST API that *Naikan* can consume.**: Ensure that you have the necessary authentication and permissions to perform this action.
* **Understand the *Naikan* Schema**: Before creating the `naikan.json` file, familiarize yourself with the *Naikan* data schema.

## Creating a Naikan JSON File

To manually create a `naikan.json` file for integration, follow these steps:

* **Data Collection**: Collect the data you want to include in the *Naikan* instance. Ensure that your data adheres to the schema.

* **Create the JSON File**: Create a `naikan.json` file in the required format and place it at the root of your project. 

* **Push Data to Naikan**: To push the `naikan.json` file via the REST API. Ensure that you have the necessary authentication and permissions to perform this action.

## Using Naikan Maven Plugin

The [Naikan Maven Plugin](https://github.com/enofex/naikan-maven-plugin) simplifies data integration by automatically generating information from your project's `pom.xml` and Git repository. 

The *Naikan Maven Plugin* is designed to extract information from your project's `pom.xml` by default. However, you can enhance the data retrieval process by including missing details within the `pom.xml` file. When the plugin encounters specific elements or properties in your `pom.xml`, it will retrieve this information into the generated data for integration with *Naikan*.

* **Add the *Naikan Maven Plugin* to your project's `pom.xml` as a build plugin**:

```
<!-- uses default configuration -->
<plugin>
    <groupId>com.enofex</groupId>
    <artifactId>naikan-maven-plugin</artifactId>
    <version>lastest version</version>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>aggregate</goal>
        </goals>
      </execution>
    </executions>
</plugin>
```

The *Naikan Maven Plugin* can merge the generated data with an existing `naikan.json` file if available and store it to your build target of your project.

* **Push Data to Naikan**: To push the `naikan.json` file via the REST API. Ensure that you have the necessary authentication and permissions to perform this action.

## Custom Naikan Maven Plugin Provider

The *Naikan Maven Plugin* is designed to be extensible, allowing you to incorporate custom data collection and formatting logic into your integration process. You can achieve this by creating custom providers that adhere to the Java Service API. These providers are dynamically discovered and executed by the plugin, giving you flexibility in gathering and preparing data for integration with *Naikan*.

By extending the Plugin with custom providers and defining their order of execution, you can seamlessly integrate diverse data sources into your *Naikan* instance while maintaining control over the integration process. This flexibility ensures that your integration aligns perfectly with your project's unique requirements.

By exploring custom provider implementations in [Naikan Maven Plugin](https://github.com/enofex/naikan-maven-plugin), you can gain valuable insights into how these providers collect and format data. 