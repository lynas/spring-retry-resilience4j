# spring-retry-resilience4j
- service to service api call retry if one of the service not available via api call

### required dependency
```
implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
implementation("org.springframework.boot:spring-boot-starter-actuator") // required in classpath for resilience4j
implementation("org.springframework.boot:spring-boot-starter-aop") // required in classpath for resilience4j

```

### yml config

```
resilience4j.retry:
  configs:
    default:
      maxAttempts: 10
      waitDuration: 1000
      retryExceptions:
        - java.lang.Exception
  instances:
    order-service: // name of the instance
      baseConfig: default

```

### code
- put this on top of the method where you want to retry
```
@Retry(name = "order-service")
```
