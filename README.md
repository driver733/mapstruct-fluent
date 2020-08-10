# Mapstruct fluent extensions generator

[![Build](https://github.com/driver733/mapstruct-fluent/workflows/Build/badge.svg?branch=master)](https://github.com/driver733/mapstruct-fluent/actions?query=workflow%3ABuild+branch%3Amaster)
[![Maven Central](https://img.shields.io/maven-central/v/com.driver733.mapstruct-fluent/processor)](https://search.maven.org/search?q=com.driver733.mapstruct)

[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/driver733/mapstruct-fluent/actions?query=workflow%3ARelease)

[![Licence](https://img.shields.io/github/license/driver733/mapstruct-fluent)](https://github.com/driver733/mapstruct-fluent/blob/master/LICENSE)

This project provides a generator of [fluent](https://en.wikipedia.org/wiki/Fluent_interface) [extension functions](https://kotlinlang.org/docs/reference/extensions.html) for [mapstruct](https://github.com/mapstruct/mapstruct) [mappers](https://mapstruct.org/documentation/dev/reference/html/#defining-mapper).

### Examples

#### [Extension method for the mappers' factory](examples/example/src/test/kotlin/com/driver733/mapstructfluent/EmployeeMapperTest.kt)

```kotlin
data class EmployeeModel(val firstName: String? = null)
data class EmployeeView(val firstName: String? = null)
```

```kotlin
@Mapper
abstract class EmployeeMapper {
    abstract fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView
}
```

```kotlin
val employee = EmployeeView("Alex")
employee.toEmployeeView() == Mappers.getMapper(EmployeeMapper::class.java).toEmployeeView(employee)
```

#### [Extension method for the mapper's Spring bean](examples/example-spring/src/test/kotlin/com/driver733/mapstructfluent/EmployeeMapperTest.kt)

```kotlin
data class EmployeeModel(val firstName: String? = null)
data class EmployeeView(val firstName: String? = null)
```
```kotlin
@Mapper(componentmodel = "spring")
abstract class EmployeeMapper {
    abstract fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView
}
```

Calling the generated extension function
```kotlin
@Service
class Service {
    fun a() {
        val model = EmployeeModel("Alex")
        val view = employee.toEmployeeView()
    }
}
```

is the same as manually using the mapper's Spring bean
```kotlin
@Service
class Service(
    private val mapper: EmployeeMapper
) {
    fun a() {
        val model = EmployeeModel("Alex")
        val view = mapper.toEmployeeView(employee)
    }
}
```


## Distribution

This project is [available](https://search.maven.org/search?q=com.driver733.mapstruct-fluent) on the Maven Central repository.

## Getting Started

### Install

#### Gradle

##### Groovy DSL

Add this to your project's `build.gradle`:

```groovy
dependencies {
    annotationProcessor 'com.driver733.mapstruct-fluent:processor:1.0.2'
    implementation 'com.driver733.mapstruct-fluent:processor:1.0.2'
    annotationProcessor 'com.driver733.mapstruct-fluent:processor-spring:1.0.2'
    implementation 'com.driver733.mapstruct-fluent:processor-spring:1.0.2'
}
```

##### Kotlin DSL

1. Apply the [`KAPT` plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.kapt).

    ```kotlin
    plugins {
      id("org.jetbrains.kotlin.kapt") version "1.3.72"
    }
    ```
2. Add this to your project's `build.gradle.kts`:

    ```kotlin
    dependencies {
        kapt("com.driver733.mapstruct-fluent:processor:1.0.2")
        implementation("com.driver733.mapstruct-fluent:processor:1.0.2")
        kapt("com.driver733.mapstruct-fluent:processor-spring:1.0.2")
        implementation("com.driver733.mapstruct-fluent:processor-spring:1.0.2")
    }
    ``` 

#### Maven

Add this to your project's `pom.xml`:

```xml
<dependencies>
    <dependency>
      <groupId>com.driver733.mapstruct-fluent</groupId>
      <artifactId>processor</artifactId>
      <version>1.0.2</version>
    </dependency>
    <dependency>
      <groupId>com.driver733.mapstruct-fluent</groupId>
      <artifactId>processor-spring</artifactId>
      <version>1.0.2</version>
    </dependency>
</dependencies>
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>...</version>
            <configuration>
                <annotationProcessorPaths>
                    <annotationProcessorPath>
                        <groupId>com.driver733.mapstruct-fluent</groupId>
                        <artifactId>processor</artifactId>
                        <version>1.0.2</version>
                    </annotationProcessorPath>
                   <annotationProcessorPath>
                        <groupId>com.driver733.mapstruct-fluent</groupId>
                        <artifactId>processor-spring</artifactId>
                        <version>1.0.2</version>
                    </annotationProcessorPath>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</pluginManagement>
```

## Development

### Prerequisites

[JDK](https://stackoverflow.com/a/52524114/2441104), preferably >= `v. 1.8`

### Build

```
./gradlew clean build
```

### CI/CD

[Github actions](https://github.com/driver733/mapstruct-fluent/actions) is used for CI/CD.

### Releases

Releases to the Maven Central repository are [automatically](https://github.com/driver733/mapstruct-fluent/actions?query=workflow%3ARelease)
made on each commit on the master branch with the help of the [semantic-release](https://github.com/semantic-release/semantic-release).

## Contributing

1. Create an issue and describe your problem/suggestion in it.
2. Submit a pull request with a reference to the issue that the pull request closes.
3. I will review your changes and merge them.
4. A new version with your changes will be released automatically right after merging.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags](https://github.com/driver733/mapstruct-fluent/tags). 

## Authors

* **Mikhail [@driver733](https://www.driver733.com) Yakushin** - *Initial work*

See also the list of [contributors](https://github.com/driver733/mapstruct-fluent/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/driver733/mapstruct-fluent/blob/master/LICENSE) file for details.

## Acknowledgments

* [semantic-release](https://github.com/semantic-release/semantic-release)
