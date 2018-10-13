[![build_status](https://travis-ci.org/mforoni/jcoder.svg?branch=master)](https://travis-ci.org/mforoni/jcoder)
[![Release](https://jitpack.io/v/com.github.mforoni/jcoder.svg)](https://jitpack.io/#com.github.mforoni/jcoder)

# JCoder

Provides useful APIs for the automatic generation of Java source code, i.e. it allows to create JavaBeans and immutable classes starting from CSV files and spreadsheets.

## Built With

* [Maven](https://maven.apache.org) - Dependency Management

## Getting Started

### Minimum Requirements

* Java 1.7 or above - tested with [OracleJDK 7.0](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)
* One build automation tool:
  * [Maven](https://maven.apache.org/download.cgi)
  * [Gradle](https://gradle.org/)

### Adding JCoder to your build

This project is not yet available on the official maven repository but with [JitPack](https://jitpack.io/) 
it can easily be overcome just by following these two steps:

1. Add the JitPack repository to your build file

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

1. Add the dependency on JCoder

```xml
<dependency>
  <groupId>com.github.mforoni.jcoder</groupId>
  <artifactId>jcoder</artifactId>
  <version>master-SNAPSHOT</version>
</dependency>
```

For Gradle add the following in your root `build.gradle` at the end of repositories:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.mforoni.jcoder:jcoder:master-SNAPSHOT'
}
```

## Code Style

This project follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).


## Author

* **Marco Foroni** - [mforoni](https://github.com/mforoni)

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/mforoni/jcoder/blob/master/LICENSE) file for details

## IMPORTANT WARNINGS

1. This project is under development.

1. APIs marked with the `@Beta` annotation at the class or method level
are subject to change. They can be modified in any way, or even
removed, at any time. Read more about [`@Beta`](https://github.com/google/guava#important-warnings) annotation.
