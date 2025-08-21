# Small Spring Framework - API Documentation

## Overview

The Small Spring Framework is a lightweight implementation of the Spring Framework's core features, including Inversion of Control (IoC), Dependency Injection (DI), and Aspect-Oriented Programming (AOP). This documentation covers all public APIs, components, and their usage.

## Table of Contents

1. [Core Concepts](#core-concepts)
2. [Bean Factory API](#bean-factory-api)
3. [Application Context API](#application-context-api)
4. [Bean Configuration](#bean-configuration)
5. [Lifecycle Management](#lifecycle-management)
6. [Aspect-Oriented Programming (AOP)](#aspect-oriented-programming-aop)
7. [Annotations](#annotations)
8. [Resource Management](#resource-management)
9. [Event System](#event-system)
10. [Usage Examples](#usage-examples)

---

## Core Concepts

### Bean Factory

The Bean Factory is the central registry of beans and provides basic dependency injection capabilities.

### Application Context

The Application Context extends Bean Factory with additional enterprise features like event publishing, resource loading, and configuration management.

### Bean Definition

Bean Definition contains metadata about how to create and configure beans.

---

## Bean Factory API

### BeanFactory Interface

The core interface for accessing beans from the Spring container.

```java
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    Object getBean(String beanName, Object... args) throws BeansException;
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
```

#### Methods

- **`getBean(String beanName)`**: Retrieves a bean by name
- **`getBean(String beanName, Object... args)`**: Retrieves a bean by name with constructor arguments
- **`getBean(String name, Class<T> requiredType)`**: Retrieves a bean by name and expected type

#### Example Usage

```java
BeanFactory beanFactory = new DefaultListableBeanFactory();
MyBean bean = beanFactory.getBean("myBean", MyBean.class);
```

### ListableBeanFactory Interface

Extends BeanFactory with capabilities to list and query beans.

```java
public interface ListableBeanFactory extends BeanFactory {
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
    String[] getBeanDefinitionNames();
}
```

#### Methods

- **`getBeansOfType(Class<T> type)`**: Returns all beans of the specified type
- **`getBeanDefinitionNames()`**: Returns names of all registered bean definitions

#### Example Usage

```java
ListableBeanFactory factory = new DefaultListableBeanFactory();
Map<String, MyService> services = factory.getBeansOfType(MyService.class);
String[] beanNames = factory.getBeanDefinitionNames();
```

### ConfigurableListableBeanFactory Interface

Advanced bean factory interface with configuration capabilities.

```java
public interface ConfigurableListableBeanFactory extends 
    AutowireCapableBeanFactory, ConfigurableBeanFactory, ListableBeanFactory {
    
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    void preInstantiateSingletons() throws BeansException;
}
```

#### Methods

- **`getBeanDefinition(String beanName)`**: Retrieves bean definition metadata
- **`preInstantiateSingletons()`**: Pre-instantiates all singleton beans

---

## Application Context API

### ApplicationContext Interface

The central interface for the Spring application context.

```java
public interface ApplicationContext extends 
    ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
```

This interface combines multiple capabilities:
- Bean factory operations
- Hierarchical bean factory support
- Resource loading
- Event publishing

### ClassPathXmlApplicationContext Class

The main implementation for XML-based configuration.

```java
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    public ClassPathXmlApplicationContext();
    public ClassPathXmlApplicationContext(String configLocations) throws BeansException;
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException;
}
```

#### Constructors

- **`ClassPathXmlApplicationContext()`**: Default constructor
- **`ClassPathXmlApplicationContext(String configLocations)`**: Single configuration file
- **`ClassPathXmlApplicationContext(String[] configLocations)`**: Multiple configuration files

#### Example Usage

```java
// Single configuration file
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

// Multiple configuration files
ApplicationContext context = new ClassPathXmlApplicationContext(
    new String[]{"beans.xml", "services.xml"}
);

// Get beans from context
MyService service = context.getBean("myService", MyService.class);
```

### ConfigurableApplicationContext Interface

Provides configuration capabilities for application contexts.

```java
public interface ConfigurableApplicationContext extends ApplicationContext {
    void refresh() throws BeansException;
    void registerShutdownHook();
    void close();
}
```

#### Methods

- **`refresh()`**: Refreshes the application context
- **`registerShutdownHook()`**: Registers a shutdown hook for graceful cleanup
- **`close()`**: Closes the application context

---

## Bean Configuration

### BeanDefinition Class

Contains metadata for bean creation and configuration.

```java
public class BeanDefinition {
    public BeanDefinition(Class<?> beanClass);
    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues);
    
    // Scope management
    public void setScope(String scope);
    public boolean isSingleton();
    public boolean isPrototype();
    
    // Property management
    public PropertyValues getPropertyValues();
    public void setPropertyValues(PropertyValues propertyValues);
    
    // Lifecycle methods
    public String getInitMethodName();
    public void setInitMethodName(String initMethodName);
    public String getDestroyMethodName();
    public void setDestroyMethodName(String destroyMethodName);
}
```

#### Scopes

- **`SCOPE_SINGLETON`**: Single instance per container (default)
- **`SCOPE_PROTOTYPE`**: New instance for each request

#### Example Usage

```java
BeanDefinition beanDefinition = new BeanDefinition(MyService.class);
beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
beanDefinition.setInitMethodName("init");
beanDefinition.setDestroyMethodName("cleanup");

PropertyValues propertyValues = new PropertyValues();
propertyValues.addPropertyValue(new PropertyValue("name", "MyService"));
beanDefinition.setPropertyValues(propertyValues);
```

### PropertyValue and PropertyValues Classes

For managing bean properties.

```java
public class PropertyValue {
    public PropertyValue(String name, Object value);
    public String getName();
    public Object getValue();
}

public class PropertyValues {
    public void addPropertyValue(PropertyValue propertyValue);
    public PropertyValue[] getPropertyValues();
    public PropertyValue getPropertyValue(String propertyName);
}
```

#### Example Usage

```java
PropertyValues properties = new PropertyValues();
properties.addPropertyValue(new PropertyValue("host", "localhost"));
properties.addPropertyValue(new PropertyValue("port", 8080));
```

---

## Lifecycle Management

### InitializingBean Interface

For beans that need initialization after property setting.

```java
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
```

#### Example Implementation

```java
@Component
public class DatabaseService implements InitializingBean {
    private String connectionUrl;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialize database connection
        connectToDatabase();
    }
    
    private void connectToDatabase() {
        // Connection logic
    }
}
```

### DisposableBean Interface

For beans that need cleanup on container shutdown.

```java
public interface DisposableBean {
    void destroy() throws Exception;
}
```

#### Example Implementation

```java
@Component
public class DatabaseService implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        // Cleanup resources
        closeConnections();
    }
    
    private void closeConnections() {
        // Cleanup logic
    }
}
```

### BeanPostProcessor Interface

For processing beans before and after initialization.

```java
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
```

#### Example Implementation

```java
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Initializing bean: " + beanName);
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Bean initialized: " + beanName);
        return bean;
    }
}
```

---

## Aspect-Oriented Programming (AOP)

### Core AOP Interfaces

#### Pointcut Interface

Defines where advice should be applied.

```java
public interface Pointcut {
    ClassFilter getClassFilter();
    MethodMatcher getMethodMatcher();
}
```

#### MethodMatcher Interface

Matches methods for advice application.

```java
public interface MethodMatcher {
    boolean matches(Method method, Class<?> clazz);
}
```

#### ClassFilter Interface

Filters classes for advice application.

```java
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
```

### Advice Interfaces

#### MethodBeforeAdvice Interface

Advice that runs before method execution.

```java
public interface MethodBeforeAdvice extends BeforeAdvice {
    void before(Method method, Object[] args, Object target) throws Throwable;
}
```

#### Example Implementation

```java
public class LoggingAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Executing method: " + method.getName());
    }
}
```

### AspectJExpressionPointcut Class

AspectJ expression-based pointcut implementation.

```java
public class AspectJExpressionPointcut implements Pointcut, MethodMatcher, ClassFilter {
    public AspectJExpressionPointcut(String expression);
    
    @Override
    public boolean matches(Class<?> clazz);
    @Override
    public boolean matches(Method method, Class<?> clazz);
}
```

#### Example Usage

```java
AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
    "execution(* com.example.service.*.*(..))"
);
```

### ProxyFactory Class

Factory for creating AOP proxies.

```java
public class ProxyFactory {
    public ProxyFactory(AdvisedSupport advisedSupport);
    public Object getProxy();
}
```

#### Example Usage

```java
AdvisedSupport advisedSupport = new AdvisedSupport();
advisedSupport.setTargetSource(new TargetSource(targetObject));
advisedSupport.setMethodInterceptor(new LoggingInterceptor());

ProxyFactory proxyFactory = new ProxyFactory(advisedSupport);
Object proxy = proxyFactory.getProxy();
```

---

## Annotations

### @Component Annotation

Marks a class as a Spring component for automatic detection.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
```

#### Example Usage

```java
@Component
public class UserService {
    // Service implementation
}

@Component("customName")
public class ProductService {
    // Service implementation
}
```

### @Scope Annotation

Defines the scope of a bean.

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
    String value() default "singleton";
}
```

#### Example Usage

```java
@Component
@Scope("prototype")
public class PrototypeService {
    // New instance for each request
}

@Component
@Scope("singleton")  // Default scope
public class SingletonService {
    // Single instance per container
}
```

---

## Resource Management

### Resource Interface

Abstraction for accessing resources.

```java
public interface Resource {
    InputStream getInputStream() throws IOException;
}
```

### ResourceLoader Interface

Interface for loading resources.

```java
public interface ResourceLoader {
    String CLASSPATH_URL_PREFIX = "classpath:";
    Resource getResource(String location);
}
```

#### Example Usage

```java
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:config.properties");
InputStream inputStream = resource.getInputStream();
```

### Resource Implementations

- **`ClassPathResource`**: Loads resources from classpath
- **`FileSystemResource`**: Loads resources from file system
- **`UrlResource`**: Loads resources from URLs

---

## Event System

### ApplicationEvent Class

Base class for application events.

```java
public abstract class ApplicationEvent {
    public ApplicationEvent(Object source);
    public Object getSource();
    public long getTimestamp();
}
```

### ApplicationListener Interface

Interface for listening to application events.

```java
public interface ApplicationListener<E extends ApplicationEvent> {
    void onApplicationEvent(E event);
}
```

### ApplicationEventPublisher Interface

Interface for publishing events.

```java
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
```

#### Example Usage

```java
// Custom event
public class UserRegisteredEvent extends ApplicationEvent {
    private final String username;
    
    public UserRegisteredEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
}

// Event listener
@Component
public class EmailNotificationListener implements ApplicationListener<UserRegisteredEvent> {
    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        System.out.println("Sending welcome email to: " + event.getUsername());
    }
}

// Publishing events
ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
context.publishEvent(new UserRegisteredEvent(this, "john.doe"));
```

### Built-in Events

- **`ContextRefreshedEvent`**: Published when context is initialized or refreshed
- **`ContextClosedEvent`**: Published when context is closed

---

## Aware Interfaces

### Core Aware Interfaces

Beans can implement these interfaces to be injected with container references:

#### BeanNameAware

```java
public interface BeanNameAware extends Aware {
    void setBeanName(String name);
}
```

#### BeanFactoryAware

```java
public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
```

#### ApplicationContextAware

```java
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

#### BeanClassLoaderAware

```java
public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader classLoader);
}
```

#### Example Implementation

```java
@Component
public class ContextAwareService implements ApplicationContextAware, BeanNameAware {
    private ApplicationContext applicationContext;
    private String beanName;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
    
    public void doSomething() {
        // Use injected context
        MyBean bean = applicationContext.getBean("myBean", MyBean.class);
        System.out.println("My bean name is: " + beanName);
    }
}
```

---

## Factory Beans

### FactoryBean Interface

For creating complex beans or beans that require special instantiation logic.

```java
public interface FactoryBean<T> {
    T getObject() throws Exception;
    Class<?> getObjectType();
    boolean isSingleton();
}
```

#### Example Implementation

```java
@Component
public class DatabaseConnectionFactoryBean implements FactoryBean<DataSource> {
    private String url;
    private String username;
    private String password;
    
    @Override
    public DataSource getObject() throws Exception {
        return new DatabaseConnection(url, username, password);
    }
    
    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }
    
    // Setters for properties
    public void setUrl(String url) { this.url = url; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
```

---

## Configuration and Property Management

### PropertyPlaceholderConfigurer Class

Resolves property placeholders in bean definitions.

```java
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
    public void setLocation(Resource location);
    public void setLocations(Resource[] locations);
}
```

#### Example Usage

**XML Configuration:**
```xml
<bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:database.properties"/>
</bean>

<bean id="dataSource" class="com.example.DataSource">
    <property name="url" value="${database.url}"/>
    <property name="username" value="${database.username}"/>
</bean>
```

**Properties File (database.properties):**
```properties
database.url=jdbc:mysql://localhost:3306/mydb
database.username=admin
```

---

## Package Scanning

### ClassPathBeanDefinitionScanner Class

Scans classpath for annotated components.

```java
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry);
    public int scan(String... basePackages);
}
```

#### Example Usage

**XML Configuration:**
```xml
<context:component-scan base-package="com.example.service"/>
```

**Java Configuration:**
```java
BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
scanner.scan("com.example.service");
```

---

## Exception Handling

### BeansException Class

Base exception for all bean-related errors.

```java
public class BeansException extends RuntimeException {
    public BeansException(String msg);
    public BeansException(String msg, Throwable cause);
}
```

#### Common Scenarios

- Bean not found
- Bean creation failure
- Circular dependency
- Property injection failure

---

## Utility Classes

### ClassUtils Class

Utility methods for class operations.

```java
public class ClassUtils {
    public static ClassLoader getDefaultClassLoader();
    public static boolean isCglibProxyClass(Class<?> clazz);
}
```

---

## Usage Examples

### Basic Bean Configuration

**XML Configuration (applicationContext.xml):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <bean id="userService" class="com.example.UserService">
        <property name="userRepository" ref="userRepository"/>
        <property name="maxUsers" value="1000"/>
    </bean>
    
    <bean id="userRepository" class="com.example.UserRepository"/>
    
</beans>
```

**Java Usage:**
```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        UserService userService = context.getBean("userService", UserService.class);
        userService.createUser("john.doe");
        
        // Graceful shutdown
        ((ConfigurableApplicationContext) context).close();
    }
}
```

### Annotation-Based Configuration

**Component Class:**
```java
@Component
@Scope("singleton")
public class EmailService implements InitializingBean, DisposableBean {
    
    private String smtpHost;
    private boolean initialized = false;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialize SMTP connection
        this.initialized = true;
        System.out.println("EmailService initialized");
    }
    
    @Override
    public void destroy() throws Exception {
        // Cleanup resources
        this.initialized = false;
        System.out.println("EmailService destroyed");
    }
    
    public void sendEmail(String to, String message) {
        if (!initialized) {
            throw new IllegalStateException("EmailService not initialized");
        }
        // Send email logic
    }
}
```

**XML Configuration with Component Scan:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context">
    
    <context:component-scan base-package="com.example"/>
    
</beans>
```

### Property Placeholder Configuration

**Properties File (app.properties):**
```properties
email.smtp.host=smtp.gmail.com
email.smtp.port=587
app.name=MyApplication
```

**XML Configuration:**
```xml
<bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:app.properties"/>
</bean>

<bean id="emailConfig" class="com.example.EmailConfig">
    <property name="smtpHost" value="${email.smtp.host}"/>
    <property name="smtpPort" value="${email.smtp.port}"/>
    <property name="applicationName" value="${app.name}"/>
</bean>
```

### AOP Configuration

**Advice Implementation:**
```java
public class LoggingAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Executing method: " + method.getName() + 
                          " on target: " + target.getClass().getSimpleName());
    }
}
```

**Pointcut and Advisor Configuration:**
```java
// Create pointcut
AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
    "execution(* com.example.service.*.*(..))"
);

// Create advisor
AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
advisor.setExpression("execution(* com.example.service.*.*(..))");
advisor.setAdvice(new LoggingAdvice());

// Configure proxy
AdvisedSupport advisedSupport = new AdvisedSupport();
advisedSupport.setTargetSource(new TargetSource(targetService));
advisedSupport.setMethodInterceptor(new MethodBeforeAdviceInterceptor(new LoggingAdvice()));

ProxyFactory proxyFactory = new ProxyFactory(advisedSupport);
Object proxy = proxyFactory.getProxy();
```

### Event Handling Example

**Custom Event:**
```java
public class OrderCreatedEvent extends ApplicationEvent {
    private final String orderId;
    
    public OrderCreatedEvent(Object source, String orderId) {
        super(source);
        this.orderId = orderId;
    }
    
    public String getOrderId() {
        return orderId;
    }
}
```

**Event Listener:**
```java
@Component
public class OrderEventListener implements ApplicationListener<OrderCreatedEvent> {
    @Override
    public void onApplicationEvent(OrderCreatedEvent event) {
        System.out.println("Order created: " + event.getOrderId());
        // Send notification, update inventory, etc.
    }
}
```

**Publishing Events:**
```java
@Component
public class OrderService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public void createOrder(String orderId) {
        // Create order logic
        
        // Publish event
        applicationContext.publishEvent(new OrderCreatedEvent(this, orderId));
    }
}
```

---

## Best Practices

### 1. Bean Naming Conventions

- Use camelCase for bean names
- Use descriptive names that indicate the bean's purpose
- For components, the default name is the class name with first letter lowercase

### 2. Scope Selection

- Use **singleton** scope (default) for stateless services
- Use **prototype** scope for stateful objects or when you need new instances

### 3. Lifecycle Management

- Implement `InitializingBean` for complex initialization logic
- Implement `DisposableBean` for resource cleanup
- Use init-method and destroy-method attributes in XML for POJO beans

### 4. AOP Best Practices

- Keep pointcut expressions specific to avoid unintended advice application
- Use before advice for logging, security checks
- Prefer composition over inheritance when possible

### 5. Configuration Management

- Use property placeholders for environment-specific values
- Organize configuration files logically
- Use component scanning for annotation-based configuration

---

## Error Handling

### Common Exceptions

- **`BeansException`**: General bean-related errors
- **`NoSuchBeanDefinitionException`**: Bean not found
- **`BeanCreationException`**: Error during bean creation
- **`CircularDependencyException`**: Circular dependency detected

### Error Prevention

1. **Avoid Circular Dependencies**: Use setter injection or `@Lazy` annotation
2. **Validate Configuration**: Check XML syntax and property references
3. **Handle Resource Loading**: Ensure resources exist and are accessible
4. **Proper Exception Handling**: Implement proper error handling in lifecycle methods

---

## Migration Guide

### From Step to Step

Each step in the Small Spring implementation builds upon the previous:

1. **Steps 1-6**: Basic IoC container functionality
2. **Steps 7-8**: Lifecycle management and awareness
3. **Steps 9-10**: Scopes, factory beans, and events
4. **Steps 11-13**: AOP, auto-proxying, and component scanning

### Integration Tips

- Start with XML configuration for simplicity
- Gradually migrate to annotation-based configuration
- Use component scanning for automatic bean discovery
- Implement AOP for cross-cutting concerns

---

## Performance Considerations

### Bean Creation

- Singleton beans are created once and cached
- Prototype beans are created on each request
- Use lazy initialization for expensive beans

### Memory Management

- Implement `DisposableBean` for proper cleanup
- Use weak references for event listeners when appropriate
- Monitor bean creation patterns to avoid memory leaks

---

## Troubleshooting

### Common Issues

1. **Bean Not Found**
   - Check bean name spelling
   - Verify component scanning configuration
   - Ensure XML configuration is correct

2. **Circular Dependencies**
   - Use setter injection instead of constructor injection
   - Consider redesigning object relationships

3. **Property Injection Failures**
   - Verify property names match setter methods
   - Check property placeholder configuration
   - Ensure property files are in classpath

4. **AOP Not Working**
   - Verify pointcut expressions
   - Check if target class is proxied
   - Ensure advisors are properly configured

### Debug Tips

- Enable debug logging to see bean creation process
- Use `getBeanDefinitionNames()` to list all registered beans
- Check `getBeansOfType()` to verify bean registration
- Inspect bean definitions for correct configuration