# WTFSocket-Server-JAVA

### 简介

轻量的 **Tcp/WebSocket** 服务器框架

帮助速搭建需要保持长连接的双工消息型服务器

适用于消息短且转发频繁的业务场合，不适合大文件的传输

需要`JDK-1.8 or later`

更多使用的方法查看 [**`WiKi`**]()

### 集成

添加 Maven 仓库

- **Maven**

```xml
<repositories>
    <repository>
    	<id>ZFly-repo</id>
        <url>https://raw.githubusercontent.com/ZhangFly/mvn-repo/master</url>
    </repository>
</repositories>
```

- **Gradle**

```groovy
repositories {
    ZFly-repo urls: "https://raw.githubusercontent.com/ZhangFly/mvn-repo/master"
}
```

添加项目依赖

- **Maven**

```xml
<dependencies>
	<dependency>
    	<groupId>ZFly</groupId>
        <artifactId>wtfsocket-server</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

- **Gradle**

```groovy
dependencies {
    compile('ZFly:wtfsocket-server:1.0.0')
}
```

更多可用版本请查看 [**`ZFly的Maven仓库`**](https://github.com/ZhangFly/mvn-repo)

### 致谢

感谢`netty` 、`fastjson`、`spring`等优秀的开源框架。

