# Small Spring Framework - Usage Guide

## Getting Started

This guide provides step-by-step instructions for using the Small Spring Framework, from basic setup to advanced features.

## Table of Contents

1. [Quick Start](#quick-start)
2. [Project Setup](#project-setup)
3. [Basic Bean Configuration](#basic-bean-configuration)
4. [Dependency Injection](#dependency-injection)
5. [Annotation-Based Configuration](#annotation-based-configuration)
6. [Property Configuration](#property-configuration)
7. [Bean Lifecycle Management](#bean-lifecycle-management)
8. [Aspect-Oriented Programming](#aspect-oriented-programming)
9. [Event Handling](#event-handling)
10. [Advanced Topics](#advanced-topics)

---

## Quick Start

### 1. Add Dependencies

Add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <!-- Small Spring Framework -->
    <dependency>
        <groupId>com.avezah.springframework</groupId>
        <artifactId>small-spring-step-13</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Required dependencies -->
    <dependency>
        <groupId>cglib</groupId>
        <artifactId>cglib</artifactId>
        <version>3.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.dom4j</groupId>
        <artifactId>dom4j</artifactId>
        <version>2.1.3</version>
    </dependency>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.9.7</version>
    </dependency>
    <dependency>
        <groupId>aopalliance</groupId>
        <artifactId>aopalliance</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

### 2. Create Your First Bean

```java
public class HelloWorldService {
    private String message;
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void sayHello() {
        System.out.println("Hello: " + message);
    }
}
```

### 3. Configure in XML

Create `applicationContext.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <bean id="helloWorldService" class="com.example.HelloWorldService">
        <property name="message" value="Welcome to Small Spring!"/>
    </bean>
    
</beans>
```

### 4. Use the Application Context

```java
import com.avezah.springframework.context.ApplicationContext;
import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        HelloWorldService service = context.getBean("helloWorldService", HelloWorldService.class);
        service.sayHello(); // Output: Hello: Welcome to Small Spring!
        
        // Graceful shutdown
        ((ConfigurableApplicationContext) context).close();
    }
}
```

---

## Project Setup

### Maven Project Structure

```
my-spring-app/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── Application.java
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── applicationContext.xml
│   │       └── application.properties
│   └── test/
│       ├── java/
│       └── resources/
└── README.md
```

### Complete pom.xml Template

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-spring-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- Small Spring Framework -->
        <dependency>
            <groupId>com.avezah.springframework</groupId>
            <artifactId>small-spring-step-13</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        
        <!-- Required Runtime Dependencies -->
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>3.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.dom4j</groupId>
            <artifactId>dom4j</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.7</version>
        </dependency>
        <dependency>
            <groupId>aopalliance</groupId>
            <artifactId>aopalliance</artifactId>
            <version>1.0</version>
        </dependency>
        
        <!-- Test Dependencies -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.24.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
</project>
```

---

## Basic Bean Configuration

### XML-Based Configuration

#### Simple Bean Definition

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <!-- Simple bean with no dependencies -->
    <bean id="messageService" class="com.example.MessageService"/>
    
    <!-- Bean with property injection -->
    <bean id="emailService" class="com.example.EmailService">
        <property name="smtpHost" value="smtp.gmail.com"/>
        <property name="smtpPort" value="587"/>
    </bean>
    
    <!-- Bean with constructor arguments -->
    <bean id="databaseService" class="com.example.DatabaseService">
        <constructor-arg value="localhost"/>
        <constructor-arg value="5432"/>
    </bean>
    
</beans>
```

#### Bean References

```xml
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <bean id="userRepository" class="com.example.UserRepository">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
    <bean id="userService" class="com.example.UserService">
        <property name="userRepository" ref="userRepository"/>
        <property name="emailService" ref="emailService"/>
    </bean>
    
    <bean id="dataSource" class="com.example.DataSource">
        <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
        <property name="username" value="admin"/>
        <property name="password" value="password"/>
    </bean>
    
    <bean id="emailService" class="com.example.EmailService"/>
    
</beans>
```

### Programmatic Configuration

```java
import com.avezah.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.PropertyValues;
import com.avezah.springframework.beans.PropertyValue;

public class ProgrammaticConfiguration {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // Create bean definition
        BeanDefinition userServiceDef = new BeanDefinition(UserService.class);
        
        // Configure properties
        PropertyValues properties = new PropertyValues();
        properties.addPropertyValue(new PropertyValue("maxUsers", 1000));
        userServiceDef.setPropertyValues(properties);
        
        // Register bean
        beanFactory.registerBeanDefinition("userService", userServiceDef);
        
        // Get bean instance
        UserService userService = beanFactory.getBean("userService", UserService.class);
    }
}
```

---

## Dependency Injection

### Setter Injection

**Bean Class:**
```java
public class OrderService {
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private String currency;
    
    // Setter methods for injection
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public void processOrder(Order order) {
        inventoryService.reserveItems(order.getItems());
        paymentService.processPayment(order.getTotal(), currency);
    }
}
```

**XML Configuration:**
```xml
<bean id="orderService" class="com.example.OrderService">
    <property name="paymentService" ref="paymentService"/>
    <property name="inventoryService" ref="inventoryService"/>
    <property name="currency" value="USD"/>
</bean>

<bean id="paymentService" class="com.example.PaymentService"/>
<bean id="inventoryService" class="com.example.InventoryService"/>
```

### Constructor Injection

**Bean Class:**
```java
public class NotificationService {
    private final EmailService emailService;
    private final SmsService smsService;
    
    public NotificationService(EmailService emailService, SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }
    
    public void sendNotification(String message, String recipient) {
        emailService.send(recipient, message);
        smsService.send(recipient, message);
    }
}
```

**XML Configuration:**
```xml
<bean id="notificationService" class="com.example.NotificationService">
    <constructor-arg ref="emailService"/>
    <constructor-arg ref="smsService"/>
</bean>

<bean id="emailService" class="com.example.EmailService"/>
<bean id="smsService" class="com.example.SmsService"/>
```

---

## Annotation-Based Configuration

### Component Scanning Setup

**XML Configuration:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                          http://www.springframework.org/schema/beans/spring-beans.xsd
                          http://www.springframework.org/schema/context
                          http://www.springframework.org/schema/context/spring-context.xsd">
    
    <!-- Enable component scanning -->
    <context:component-scan base-package="com.example"/>
    
</beans>
```

### Component Classes

**Service Component:**
```java
package com.example.service;

import com.avezah.springframework.stereotype.Component;
import com.avezah.springframework.context.annotation.Scope;

@Component
@Scope("singleton")
public class UserService {
    
    public void createUser(String username) {
        System.out.println("Creating user: " + username);
    }
    
    public void deleteUser(String username) {
        System.out.println("Deleting user: " + username);
    }
}
```

**Repository Component:**
```java
package com.example.repository;

import com.avezah.springframework.stereotype.Component;

@Component("userRepo")  // Custom bean name
public class UserRepository {
    
    public void save(User user) {
        System.out.println("Saving user: " + user.getUsername());
    }
    
    public User findByUsername(String username) {
        // Database lookup logic
        return new User(username);
    }
}
```

**Configuration Component:**
```java
package com.example.config;

import com.avezah.springframework.stereotype.Component;
import com.avezah.springframework.beans.factory.InitializingBean;

@Component
public class DatabaseConfig implements InitializingBean {
    private String connectionUrl;
    private boolean initialized = false;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.connectionUrl = "jdbc:mysql://localhost:3306/mydb";
        this.initialized = true;
        System.out.println("Database configuration initialized");
    }
    
    public String getConnectionUrl() {
        return connectionUrl;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
```

---

## Property Configuration

### External Properties

**Step 1: Create Properties File**

Create `application.properties`:
```properties
# Database configuration
database.url=jdbc:mysql://localhost:3306/myapp
database.username=admin
database.password=secret
database.maxConnections=20

# Email configuration
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.from=noreply@myapp.com

# Application settings
app.name=My Spring Application
app.version=1.0.0
app.debug=false
```

**Step 2: Configure Property Placeholder**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <!-- Property placeholder configurer -->
    <bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:application.properties"/>
    </bean>
    
    <!-- Beans using properties -->
    <bean id="databaseConfig" class="com.example.DatabaseConfig">
        <property name="url" value="${database.url}"/>
        <property name="username" value="${database.username}"/>
        <property name="password" value="${database.password}"/>
        <property name="maxConnections" value="${database.maxConnections}"/>
    </bean>
    
    <bean id="emailConfig" class="com.example.EmailConfig">
        <property name="smtpHost" value="${email.smtp.host}"/>
        <property name="smtpPort" value="${email.smtp.port}"/>
        <property name="fromAddress" value="${email.from}"/>
    </bean>
    
</beans>
```

**Step 3: Use Configured Beans**

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        DatabaseConfig dbConfig = context.getBean("databaseConfig", DatabaseConfig.class);
        System.out.println("Database URL: " + dbConfig.getUrl());
        
        EmailConfig emailConfig = context.getBean("emailConfig", EmailConfig.class);
        System.out.println("SMTP Host: " + emailConfig.getSmtpHost());
    }
}
```

---

## Bean Lifecycle Management

### Initialization and Destruction

**Method 1: Implement Interfaces**

```java
import com.avezah.springframework.beans.factory.InitializingBean;
import com.avezah.springframework.beans.factory.DisposableBean;
import com.avezah.springframework.stereotype.Component;

@Component
public class DatabaseConnectionService implements InitializingBean, DisposableBean {
    private Connection connection;
    private boolean connected = false;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialize database connection
        this.connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/mydb", "admin", "password"
        );
        this.connected = true;
        System.out.println("Database connection established");
    }
    
    @Override
    public void destroy() throws Exception {
        // Cleanup resources
        if (connection != null && !connection.isClosed()) {
            connection.close();
            this.connected = false;
            System.out.println("Database connection closed");
        }
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void executeQuery(String sql) {
        if (!connected) {
            throw new IllegalStateException("Database not connected");
        }
        // Execute query logic
    }
}
```

**Method 2: XML Configuration**

```xml
<bean id="fileProcessor" class="com.example.FileProcessor" 
      init-method="initialize" 
      destroy-method="cleanup">
    <property name="inputDirectory" value="/tmp/input"/>
    <property name="outputDirectory" value="/tmp/output"/>
</bean>
```

```java
public class FileProcessor {
    private String inputDirectory;
    private String outputDirectory;
    private boolean initialized = false;
    
    public void initialize() {
        // Create directories, validate paths
        new File(inputDirectory).mkdirs();
        new File(outputDirectory).mkdirs();
        this.initialized = true;
        System.out.println("FileProcessor initialized");
    }
    
    public void cleanup() {
        // Cleanup temporary files, close resources
        this.initialized = false;
        System.out.println("FileProcessor cleaned up");
    }
    
    // Setters and business methods...
}
```

### Bean Post Processors

**Custom Bean Post Processor:**

```java
import com.avezah.springframework.beans.factory.config.BeanPostProcessor;
import com.avezah.springframework.stereotype.Component;

@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Before initialization of bean: " + beanName);
        
        // Custom logic before initialization
        if (bean instanceof Configurable) {
            ((Configurable) bean).configure();
        }
        
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("After initialization of bean: " + beanName);
        
        // Custom logic after initialization
        if (bean instanceof Validatable) {
            ((Validatable) bean).validate();
        }
        
        return bean;
    }
}
```

---

## Aspect-Oriented Programming

### Basic AOP Setup

**Step 1: Create Advice**

```java
import com.avezah.springframework.aop.MethodBeforeAdvice;
import java.lang.reflect.Method;

public class LoggingAdvice implements MethodBeforeAdvice {
    
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("=== Method Execution Log ===");
        System.out.println("Target: " + target.getClass().getSimpleName());
        System.out.println("Method: " + method.getName());
        System.out.println("Arguments: " + Arrays.toString(args));
        System.out.println("Timestamp: " + new Date());
        System.out.println("============================");
    }
}
```

**Step 2: Configure Pointcut and Advisor**

```java
import com.avezah.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.avezah.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;

public class AopConfiguration {
    
    public static void configureAop(ConfigurableApplicationContext context) {
        // Create pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
            "execution(* com.example.service.*.*(..))"
        );
        
        // Create advisor
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("execution(* com.example.service.*.*(..))");
        advisor.setAdvice(new LoggingAdvice());
        
        // Register advisor
        context.getBeanFactory().registerSingleton("loggingAdvisor", advisor);
    }
}
```

**Step 3: Enable Auto-Proxying**

```xml
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <!-- Enable auto-proxy creation -->
    <bean class="com.avezah.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
    
    <!-- Your service beans -->
    <bean id="userService" class="com.example.service.UserService"/>
    <bean id="orderService" class="com.example.service.OrderService"/>
    
    <!-- Advisor configuration -->
    <bean id="loggingAdvisor" class="com.avezah.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
        <property name="expression" value="execution(* com.example.service.*.*(..))"/>
        <property name="advice" ref="loggingAdvice"/>
    </bean>
    
    <bean id="loggingAdvice" class="com.example.LoggingAdvice"/>
    
</beans>
```

### Advanced AOP Examples

**Performance Monitoring Advice:**

```java
public class PerformanceMonitoringAdvice implements MethodBeforeAdvice {
    private final Map<String, Long> executionTimes = new ConcurrentHashMap<>();
    
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        String key = target.getClass().getSimpleName() + "." + method.getName();
        long startTime = System.nanoTime();
        
        // Store start time (in real implementation, you'd use ThreadLocal)
        executionTimes.put(Thread.currentThread().getName() + ":" + key, startTime);
    }
    
    // Note: This would typically be implemented as MethodInterceptor for around advice
    public void afterReturning(Method method, Object[] args, Object target) {
        String key = target.getClass().getSimpleName() + "." + method.getName();
        String threadKey = Thread.currentThread().getName() + ":" + key;
        Long startTime = executionTimes.remove(threadKey);
        
        if (startTime != null) {
            long executionTime = (System.nanoTime() - startTime) / 1_000_000; // Convert to ms
            System.out.println("Method " + key + " executed in " + executionTime + "ms");
        }
    }
}
```

**Security Advice:**

```java
public class SecurityAdvice implements MethodBeforeAdvice {
    
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        // Check if method requires authentication
        if (method.isAnnotationPresent(RequiresAuthentication.class)) {
            if (!SecurityContext.isAuthenticated()) {
                throw new SecurityException("Authentication required for method: " + method.getName());
            }
        }
        
        // Check role-based access
        if (method.isAnnotationPresent(RequiresRole.class)) {
            RequiresRole roleAnnotation = method.getAnnotation(RequiresRole.class);
            if (!SecurityContext.hasRole(roleAnnotation.value())) {
                throw new SecurityException("Insufficient privileges for method: " + method.getName());
            }
        }
    }
}
```

---

## Event Handling

### Creating Custom Events

**Step 1: Define Event Class**

```java
import com.avezah.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    private final String username;
    private final String email;
    private final Date registrationTime;
    
    public UserRegistrationEvent(Object source, String username, String email) {
        super(source);
        this.username = username;
        this.email = email;
        this.registrationTime = new Date();
    }
    
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Date getRegistrationTime() { return registrationTime; }
}
```

**Step 2: Create Event Publisher**

```java
import com.avezah.springframework.context.ApplicationContextAware;
import com.avezah.springframework.stereotype.Component;

@Component
public class UserRegistrationService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public void registerUser(String username, String email) {
        // Registration logic
        User user = new User(username, email);
        saveUser(user);
        
        // Publish event
        UserRegistrationEvent event = new UserRegistrationEvent(this, username, email);
        applicationContext.publishEvent(event);
        
        System.out.println("User registered: " + username);
    }
    
    private void saveUser(User user) {
        // Database save logic
    }
}
```

**Step 3: Create Event Listeners**

```java
import com.avezah.springframework.context.ApplicationListener;
import com.avezah.springframework.stereotype.Component;

@Component
public class WelcomeEmailListener implements ApplicationListener<UserRegistrationEvent> {
    
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        System.out.println("Sending welcome email to: " + event.getEmail());
        sendWelcomeEmail(event.getUsername(), event.getEmail());
    }
    
    private void sendWelcomeEmail(String username, String email) {
        // Email sending logic
        System.out.println("Welcome email sent to " + username + " at " + email);
    }
}

@Component
public class AuditListener implements ApplicationListener<UserRegistrationEvent> {
    
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        System.out.println("Audit log: User " + event.getUsername() + 
                          " registered at " + event.getRegistrationTime());
        
        // Write to audit log
        writeAuditLog(event);
    }
    
    private void writeAuditLog(UserRegistrationEvent event) {
        // Audit logging logic
    }
}
```

**Step 4: Usage Example**

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        UserRegistrationService registrationService = 
            context.getBean("userRegistrationService", UserRegistrationService.class);
        
        // This will trigger events
        registrationService.registerUser("john.doe", "john@example.com");
        
        // Output:
        // User registered: john.doe
        // Sending welcome email to: john@example.com
        // Welcome email sent to john.doe at john@example.com
        // Audit log: User john.doe registered at [timestamp]
    }
}
```

---

## Advanced Topics

### Factory Beans

**Creating Complex Objects:**

```java
import com.avezah.springframework.beans.factory.FactoryBean;
import com.avezah.springframework.stereotype.Component;

@Component
public class DataSourceFactoryBean implements FactoryBean<DataSource> {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int maxConnections = 10;
    
    @Override
    public DataSource getObject() throws Exception {
        // Create and configure data source
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maxConnections);
        
        return new HikariDataSource(config);
    }
    
    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }
    
    // Setters for properties...
    public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
    public void setUrl(String url) { this.url = url; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
}
```

**XML Configuration for Factory Bean:**

```xml
<bean id="dataSourceFactory" class="com.example.DataSourceFactoryBean">
    <property name="driverClassName" value="${database.driver}"/>
    <property name="url" value="${database.url}"/>
    <property name="username" value="${database.username}"/>
    <property name="password" value="${database.password}"/>
    <property name="maxConnections" value="${database.maxConnections}"/>
</bean>

<!-- The actual DataSource bean (created by factory) -->
<bean id="dataSource" factory-bean="dataSourceFactory" factory-method="getObject"/>
```

### Aware Interfaces

**Comprehensive Aware Implementation:**

```java
import com.avezah.springframework.beans.factory.*;
import com.avezah.springframework.context.ApplicationContextAware;
import com.avezah.springframework.stereotype.Component;

@Component
public class FrameworkAwareService implements 
    BeanNameAware, 
    BeanFactoryAware, 
    ApplicationContextAware, 
    BeanClassLoaderAware {
    
    private String beanName;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    private ClassLoader classLoader;
    
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Bean name set: " + name);
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        System.out.println("Bean factory injected");
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("Application context injected");
    }
    
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        System.out.println("Class loader injected");
    }
    
    public void demonstrateAwareness() {
        System.out.println("My bean name: " + beanName);
        System.out.println("Available beans: " + Arrays.toString(
            ((ListableBeanFactory) beanFactory).getBeanDefinitionNames()
        ));
        System.out.println("Context class: " + applicationContext.getClass().getSimpleName());
        System.out.println("Class loader: " + classLoader.getClass().getSimpleName());
    }
}
```

### Bean Scopes

**Prototype Scope Example:**

```java
import com.avezah.springframework.stereotype.Component;
import com.avezah.springframework.context.annotation.Scope;

@Component
@Scope("prototype")
public class ShoppingCart {
    private final List<Item> items = new ArrayList<>();
    private final String sessionId;
    
    public ShoppingCart() {
        this.sessionId = UUID.randomUUID().toString();
        System.out.println("New shopping cart created: " + sessionId);
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
    
    public String getSessionId() {
        return sessionId;
    }
}
```

**Usage:**

```java
public class ShoppingApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // Each call creates a new instance
        ShoppingCart cart1 = context.getBean("shoppingCart", ShoppingCart.class);
        ShoppingCart cart2 = context.getBean("shoppingCart", ShoppingCart.class);
        
        System.out.println("Cart 1 ID: " + cart1.getSessionId());
        System.out.println("Cart 2 ID: " + cart2.getSessionId());
        System.out.println("Are they the same? " + (cart1 == cart2)); // false
    }
}
```

---

## Complete Application Example

### E-commerce Application

**Step 1: Domain Models**

```java
public class Product {
    private Long id;
    private String name;
    private double price;
    private int stock;
    
    // Constructors, getters, setters...
}

public class Order {
    private Long id;
    private String customerId;
    private List<OrderItem> items;
    private double total;
    private OrderStatus status;
    
    // Constructors, getters, setters...
}
```

**Step 2: Repository Layer**

```java
@Component
public class ProductRepository {
    private final Map<Long, Product> products = new HashMap<>();
    
    public void save(Product product) {
        products.put(product.getId(), product);
    }
    
    public Product findById(Long id) {
        return products.get(id);
    }
    
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}

@Component
public class OrderRepository {
    private final Map<Long, Order> orders = new HashMap<>();
    private Long nextId = 1L;
    
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(nextId++);
        }
        orders.put(order.getId(), order);
        return order;
    }
    
    public Order findById(Long id) {
        return orders.get(id);
    }
}
```

**Step 3: Service Layer**

```java
@Component
public class ProductService implements ApplicationContextAware {
    private ProductRepository productRepository;
    private ApplicationContext applicationContext;
    
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public Product createProduct(String name, double price, int stock) {
        Product product = new Product();
        product.setId(System.currentTimeMillis()); // Simple ID generation
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        
        productRepository.save(product);
        
        // Publish event
        applicationContext.publishEvent(new ProductCreatedEvent(this, product));
        
        return product;
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

@Component
public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Order createOrder(String customerId, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setItems(items);
        order.setStatus(OrderStatus.PENDING);
        
        // Calculate total
        double total = items.stream()
            .mapToDouble(item -> {
                Product product = productRepository.findById(item.getProductId());
                return product.getPrice() * item.getQuantity();
            })
            .sum();
        order.setTotal(total);
        
        return orderRepository.save(order);
    }
}
```

**Step 4: Event Handling**

```java
public class ProductCreatedEvent extends ApplicationEvent {
    private final Product product;
    
    public ProductCreatedEvent(Object source, Product product) {
        super(source);
        this.product = product;
    }
    
    public Product getProduct() {
        return product;
    }
}

@Component
public class InventoryEventListener implements ApplicationListener<ProductCreatedEvent> {
    
    @Override
    public void onApplicationEvent(ProductCreatedEvent event) {
        Product product = event.getProduct();
        System.out.println("New product added to inventory: " + product.getName());
        
        // Update inventory tracking
        updateInventoryTracking(product);
    }
    
    private void updateInventoryTracking(Product product) {
        // Inventory management logic
    }
}
```

**Step 5: AOP for Logging**

```java
public class ServiceLoggingAdvice implements MethodBeforeAdvice {
    
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (target.getClass().getSimpleName().endsWith("Service")) {
            System.out.println("Service call: " + target.getClass().getSimpleName() + 
                             "." + method.getName() + "()");
        }
    }
}
```

**Step 6: Complete Configuration**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                          http://www.springframework.org/schema/beans/spring-beans.xsd
                          http://www.springframework.org/schema/context
                          http://www.springframework.org/schema/context/spring-context.xsd">
    
    <!-- Property placeholder -->
    <bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:application.properties"/>
    </bean>
    
    <!-- Component scanning -->
    <context:component-scan base-package="com.example"/>
    
    <!-- AOP configuration -->
    <bean class="com.avezah.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
    
    <bean id="serviceLoggingAdvisor" class="com.avezah.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
        <property name="expression" value="execution(* com.example.service.*.*(..))"/>
        <property name="advice" ref="serviceLoggingAdvice"/>
    </bean>
    
    <bean id="serviceLoggingAdvice" class="com.example.ServiceLoggingAdvice"/>
    
</beans>
```

**Step 7: Main Application**

```java
public class EcommerceApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // Get services
        ProductService productService = context.getBean("productService", ProductService.class);
        OrderService orderService = context.getBean("orderService", OrderService.class);
        
        // Create products
        Product laptop = productService.createProduct("Gaming Laptop", 1299.99, 10);
        Product mouse = productService.createProduct("Wireless Mouse", 29.99, 50);
        
        // Create order
        List<OrderItem> items = Arrays.asList(
            new OrderItem(laptop.getId(), 1),
            new OrderItem(mouse.getId(), 2)
        );
        
        Order order = orderService.createOrder("customer123", items);
        System.out.println("Order created with total: $" + order.getTotal());
        
        // Graceful shutdown
        ((ConfigurableApplicationContext) context).registerShutdownHook();
    }
}
```

---

## Testing Your Application

### Unit Testing with Small Spring

**Test Configuration:**

```xml
<!-- test-applicationContext.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context">
    
    <context:component-scan base-package="com.example"/>
    
    <!-- Test-specific beans -->
    <bean id="testDataSource" class="com.example.TestDataSource"/>
    
</beans>
```

**Test Class:**

```java
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {
    private ApplicationContext context;
    private UserService userService;
    
    @Before
    public void setUp() {
        context = new ClassPathXmlApplicationContext("test-applicationContext.xml");
        userService = context.getBean("userService", UserService.class);
    }
    
    @After
    public void tearDown() {
        ((ConfigurableApplicationContext) context).close();
    }
    
    @Test
    public void testUserCreation() {
        // Given
        String username = "testuser";
        
        // When
        userService.createUser(username);
        
        // Then
        // Verify user was created (would need repository access)
        assertThat(userService).isNotNull();
    }
    
    @Test
    public void testBeanInjection() {
        // Verify that dependencies were injected
        assertThat(userService).isNotNull();
        
        // Test that the service has its dependencies
        UserRepository repository = context.getBean("userRepository", UserRepository.class);
        assertThat(repository).isNotNull();
    }
}
```

---

## Configuration Patterns

### Environment-Specific Configuration

**Development Configuration (dev.properties):**
```properties
database.url=jdbc:h2:mem:devdb
database.username=dev
database.password=dev
logging.level=DEBUG
cache.enabled=false
```

**Production Configuration (prod.properties):**
```properties
database.url=jdbc:mysql://prod-server:3306/proddb
database.username=${DB_USER}
database.password=${DB_PASSWORD}
logging.level=INFO
cache.enabled=true
```

**Environment-Aware Configuration:**

```java
@Component
public class EnvironmentConfig implements InitializingBean {
    private String environment;
    private String databaseUrl;
    private String loggingLevel;
    private boolean cacheEnabled;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Running in environment: " + environment);
        System.out.println("Database URL: " + databaseUrl);
        System.out.println("Logging level: " + loggingLevel);
        System.out.println("Cache enabled: " + cacheEnabled);
    }
    
    // Setters for property injection...
}
```

### Modular Configuration

**Core Configuration (core-beans.xml):**
```xml
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <!-- Core infrastructure beans -->
    <bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:application.properties"/>
    </bean>
    
    <bean class="com.avezah.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
    
</beans>
```

**Service Configuration (service-beans.xml):**
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context">
    
    <context:component-scan base-package="com.example.service"/>
    
</beans>
```

**Main Configuration (applicationContext.xml):**
```xml
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <!-- Import other configuration files -->
    <import resource="core-beans.xml"/>
    <import resource="service-beans.xml"/>
    <import resource="repository-beans.xml"/>
    
</beans>
```

---

## Performance Tuning

### Lazy Initialization

```xml
<!-- Lazy initialization for expensive beans -->
<bean id="expensiveService" class="com.example.ExpensiveService" lazy-init="true">
    <property name="largeDataSet" ref="largeDataSet"/>
</bean>

<!-- Global lazy initialization -->
<beans default-lazy-init="true">
    <!-- All beans in this file will be lazy by default -->
</beans>
```

### Bean Creation Optimization

```java
@Component
public class OptimizedService implements InitializingBean {
    private ExpensiveResource resource;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialize expensive resources only once
        this.resource = createExpensiveResource();
    }
    
    private ExpensiveResource createExpensiveResource() {
        // Expensive initialization logic
        return new ExpensiveResource();
    }
}
```

### Memory Management

```java
@Component
public class MemoryEfficientService implements DisposableBean {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    public Object getCachedValue(String key) {
        return cache.get(key);
    }
    
    public void setCachedValue(String key, Object value) {
        cache.put(key, value);
    }
    
    @Override
    public void destroy() throws Exception {
        // Clear cache on shutdown
        cache.clear();
        System.out.println("Cache cleared on shutdown");
    }
}
```

---

## Error Handling and Debugging

### Common Issues and Solutions

#### 1. Bean Not Found

**Problem:**
```
BeansException: No bean named 'userService' is defined
```

**Solutions:**
```java
// Check bean registration
ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
String[] beanNames = ((ListableBeanFactory) context).getBeanDefinitionNames();
System.out.println("Registered beans: " + Arrays.toString(beanNames));

// Verify component scanning
<context:component-scan base-package="com.example.service"/>

// Check bean name in @Component
@Component("userService")  // Explicit name
public class UserService { }
```

#### 2. Circular Dependencies

**Problem:**
```
BeanCreationException: Circular dependency detected
```

**Solution - Use Setter Injection:**
```java
@Component
public class ServiceA {
    private ServiceB serviceB;
    
    public void setServiceB(ServiceB serviceB) {  // Setter injection
        this.serviceB = serviceB;
    }
}

@Component
public class ServiceB {
    private ServiceA serviceA;
    
    public void setServiceA(ServiceA serviceA) {  // Setter injection
        this.serviceA = serviceA;
    }
}
```

#### 3. Property Injection Failures

**Problem:**
```
BeanCreationException: Could not set property 'databaseUrl'
```

**Solutions:**
```java
// Ensure setter method exists
public class DatabaseConfig {
    private String databaseUrl;
    
    public void setDatabaseUrl(String databaseUrl) {  // Must match property name
        this.databaseUrl = databaseUrl;
    }
}

// Check property placeholder configuration
<bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:application.properties"/>
</bean>

// Verify property file exists and contains the property
database.url=jdbc:mysql://localhost:3306/mydb
```

### Debugging Tools

**Bean Inspection Utility:**

```java
@Component
public class DebugUtility implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public void printBeanInfo() {
        if (applicationContext instanceof ListableBeanFactory) {
            ListableBeanFactory factory = (ListableBeanFactory) applicationContext;
            
            System.out.println("\n=== Bean Factory Debug Info ===");
            String[] beanNames = factory.getBeanDefinitionNames();
            
            for (String beanName : beanNames) {
                try {
                    Object bean = factory.getBean(beanName);
                    System.out.println("Bean: " + beanName + 
                                     " | Type: " + bean.getClass().getSimpleName() +
                                     " | Hash: " + bean.hashCode());
                } catch (Exception e) {
                    System.out.println("Bean: " + beanName + " | Error: " + e.getMessage());
                }
            }
            System.out.println("===============================\n");
        }
    }
    
    public void printBeansOfType(Class<?> type) {
        if (applicationContext instanceof ListableBeanFactory) {
            ListableBeanFactory factory = (ListableBeanFactory) applicationContext;
            Map<String, ?> beans = factory.getBeansOfType(type);
            
            System.out.println("\n=== Beans of type " + type.getSimpleName() + " ===");
            beans.forEach((name, bean) -> 
                System.out.println("- " + name + ": " + bean.getClass().getName())
            );
            System.out.println("===============================\n");
        }
    }
}
```

---

## Best Practices Summary

### 1. Configuration Organization

- **Separate concerns**: Use different XML files for different layers
- **Environment-specific**: Use property files for environment-specific values
- **Modular**: Import configuration files to keep them manageable

### 2. Bean Design

- **Single Responsibility**: Each bean should have a single, well-defined purpose
- **Stateless Services**: Prefer stateless singleton services
- **Immutable Objects**: Use immutable objects when possible for thread safety

### 3. Dependency Management

- **Prefer Interfaces**: Depend on interfaces, not concrete classes
- **Avoid Circular Dependencies**: Design clear dependency hierarchies
- **Use Factory Beans**: For complex object creation

### 4. Lifecycle Management

- **Resource Cleanup**: Always implement DisposableBean for resource cleanup
- **Initialization**: Use InitializingBean for complex initialization
- **Shutdown Hooks**: Register shutdown hooks for graceful termination

### 5. AOP Usage

- **Specific Pointcuts**: Use specific pointcut expressions to avoid unintended advice
- **Separation of Concerns**: Use AOP for cross-cutting concerns only
- **Performance**: Be aware of proxy overhead

### 6. Error Handling

- **Graceful Degradation**: Handle bean creation failures gracefully
- **Meaningful Messages**: Provide clear error messages
- **Resource Cleanup**: Ensure cleanup even in error scenarios

---

## Migration from Other Frameworks

### From Spring Framework

The Small Spring Framework is designed to be compatible with basic Spring concepts:

```java
// Spring Framework code
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}

// Small Spring equivalent
@Component
public class UserService {
    private UserRepository userRepository;
    
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### From Plain Java

```java
// Plain Java
public class Application {
    public static void main(String[] args) {
        UserRepository repository = new UserRepository();
        UserService service = new UserService();
        service.setUserRepository(repository);
        
        service.createUser("john");
    }
}

// Small Spring
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        UserService service = context.getBean("userService", UserService.class);
        
        service.createUser("john");
    }
}
```

---

## Production Deployment

### Configuration for Production

**Production Properties:**
```properties
# Database
database.url=jdbc:mysql://prod-db:3306/myapp
database.username=${DB_USER}
database.password=${DB_PASSWORD}
database.pool.maxSize=50

# Logging
logging.level=WARN
logging.file=/var/log/myapp.log

# Performance
cache.enabled=true
cache.size=10000
```

**Production XML Configuration:**
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context">
    
    <!-- Production property configuration -->
    <bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <value>classpath:application.properties</value>
                <value>classpath:production.properties</value>
            </list>
        </property>
    </bean>
    
    <!-- Component scanning -->
    <context:component-scan base-package="com.example"/>
    
    <!-- Production-specific beans -->
    <bean id="connectionPool" class="com.example.ProductionConnectionPool">
        <property name="maxSize" value="${database.pool.maxSize}"/>
    </bean>
    
</beans>
```

### Monitoring and Health Checks

```java
@Component
public class HealthCheckService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public HealthStatus checkHealth() {
        HealthStatus status = new HealthStatus();
        
        try {
            // Check database connectivity
            DatabaseService dbService = applicationContext.getBean("databaseService", DatabaseService.class);
            status.setDatabaseStatus(dbService.isConnected() ? "UP" : "DOWN");
            
            // Check other services
            EmailService emailService = applicationContext.getBean("emailService", EmailService.class);
            status.setEmailStatus(emailService.isConfigured() ? "UP" : "DOWN");
            
            status.setOverallStatus("UP");
        } catch (Exception e) {
            status.setOverallStatus("DOWN");
            status.setError(e.getMessage());
        }
        
        return status;
    }
}
```

---

## Conclusion

The Small Spring Framework provides a solid foundation for building enterprise Java applications with dependency injection, AOP, and event handling capabilities. Start with simple XML configuration and gradually adopt more advanced features as your application grows.

For more detailed information, refer to:
- [API Documentation](API_DOCUMENTATION.md) - Complete API reference
- [Component Documentation](COMPONENT_DOCUMENTATION.md) - Detailed component architecture
- Test examples in the `src/test` directory

Remember to always handle resources properly, use appropriate scopes, and follow the lifecycle management patterns for robust applications.