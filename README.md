***`JDK 1.8 or later`***  [***`WiKi`***]()

### 简介

**WTFSocket-Server-JAVA** 是一个Java语言开发的轻量级 **Tcp/WebSocket** 服务器框架。用于速搭建双向即时通讯型服务器，适用于消息短且转发频繁的业务场合，不适合大文件的传输。

**WTFSocket-Server-JAVA** 是 **WTFSocket** 系统中的一部分。**WTFSocket** 系统还还包括了 **WTFSocket-Client-JAVA_Android**客户端和 **WTFSocket-Protocol**传输协议，是一整套双向即时通讯解决方案。

### 使用

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

