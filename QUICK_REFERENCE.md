# Small Spring Framework - Quick Reference

## Essential Imports

```java
// Core Framework
import com.avezah.springframework.context.ApplicationContext;
import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.context.ConfigurableApplicationContext;

// Bean Factory
import com.avezah.springframework.beans.factory.BeanFactory;
import com.avezah.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.avezah.springframework.beans.factory.config.BeanDefinition;

// Annotations
import com.avezah.springframework.stereotype.Component;
import com.avezah.springframework.context.annotation.Scope;

// Lifecycle
import com.avezah.springframework.beans.factory.InitializingBean;
import com.avezah.springframework.beans.factory.DisposableBean;

// Awareness
import com.avezah.springframework.beans.factory.ApplicationContextAware;
import com.avezah.springframework.beans.factory.BeanNameAware;

// AOP
import com.avezah.springframework.aop.MethodBeforeAdvice;
import com.avezah.springframework.aop.aspectj.AspectJExpressionPointcut;

// Events
import com.avezah.springframework.context.ApplicationEvent;
import com.avezah.springframework.context.ApplicationListener;
```

---

## Common Patterns

### 1. Basic Application Setup

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // Get and use beans
        MyService service = context.getBean("myService", MyService.class);
        service.doSomething();
        
        // Graceful shutdown
        ((ConfigurableApplicationContext) context).close();
    }
}
```

### 2. Component Definition

```java
@Component
@Scope("singleton")  // or "prototype"
public class MyService implements InitializingBean, DisposableBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialization logic
    }
    
    @Override
    public void destroy() throws Exception {
        // Cleanup logic
    }
}
```

### 3. Dependency Injection

```java
@Component
public class UserService {
    private UserRepository userRepository;
    private EmailService emailService;
    
    // Setter injection
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
```

### 4. Property Configuration

```xml
<!-- applicationContext.xml -->
<bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:application.properties"/>
</bean>

<bean id="myService" class="com.example.MyService">
    <property name="host" value="${server.host}"/>
    <property name="port" value="${server.port}"/>
</bean>
```

### 5. Component Scanning

```xml
<context:component-scan base-package="com.example"/>
```

### 6. Event Handling

```java
// Event
public class MyEvent extends ApplicationEvent {
    public MyEvent(Object source, String data) {
        super(source);
        this.data = data;
    }
}

// Listener
@Component
public class MyEventListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        // Handle event
    }
}

// Publisher
@Component
public class MyService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public void doSomething() {
        applicationContext.publishEvent(new MyEvent(this, "data"));
    }
}
```

### 7. AOP Configuration

```xml
<!-- Enable auto-proxying -->
<bean class="com.avezah.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>

<!-- Define advisor -->
<bean id="loggingAdvisor" class="com.avezah.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
    <property name="expression" value="execution(* com.example.service.*.*(..))"/>
    <property name="advice" ref="loggingAdvice"/>
</bean>

<bean id="loggingAdvice" class="com.example.LoggingAdvice"/>
```

```java
public class LoggingAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Executing: " + method.getName());
    }
}
```

---

## XML Configuration Templates

### Basic Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans">
    
    <bean id="myBean" class="com.example.MyBean">
        <property name="property1" value="value1"/>
        <property name="property2" ref="otherBean"/>
    </bean>
    
    <bean id="otherBean" class="com.example.OtherBean"/>
    
</beans>
```

### With Component Scanning

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                          http://www.springframework.org/schema/beans/spring-beans.xsd
                          http://www.springframework.org/schema/context
                          http://www.springframework.org/schema/context/spring-context.xsd">
    
    <context:component-scan base-package="com.example"/>
    
</beans>
```

### With Properties and AOP

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
    
    <!-- AOP -->
    <bean class="com.avezah.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
    
</beans>
```

---

## Common Code Snippets

### Bean Factory Operations

```java
// Get bean by name and type
MyService service = beanFactory.getBean("myService", MyService.class);

// Get all beans of a type
Map<String, MyService> services = beanFactory.getBeansOfType(MyService.class);

// List all bean names
String[] beanNames = beanFactory.getBeanDefinitionNames();

// Check if bean exists
boolean exists = beanFactory.containsBeanDefinition("myBean");
```

### Programmatic Bean Registration

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

// Create bean definition
BeanDefinition beanDef = new BeanDefinition(MyService.class);
beanDef.setScope(BeanDefinition.SCOPE_SINGLETON);

// Add properties
PropertyValues properties = new PropertyValues();
properties.addPropertyValue(new PropertyValue("name", "value"));
beanDef.setPropertyValues(properties);

// Register bean
beanFactory.registerBeanDefinition("myService", beanDef);
```

### Factory Bean Pattern

```java
@Component
public class MyFactoryBean implements FactoryBean<ComplexObject> {
    
    @Override
    public ComplexObject getObject() throws Exception {
        return new ComplexObject(/* complex initialization */);
    }
    
    @Override
    public Class<?> getObjectType() {
        return ComplexObject.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

### Aware Interface Implementation

```java
@Component
public class MyService implements ApplicationContextAware, BeanNameAware {
    private ApplicationContext applicationContext;
    private String beanName;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
```

---

## Debugging Snippets

### List All Beans

```java
public void listAllBeans(ApplicationContext context) {
    if (context instanceof ListableBeanFactory) {
        ListableBeanFactory factory = (ListableBeanFactory) context;
        String[] beanNames = factory.getBeanDefinitionNames();
        
        System.out.println("=== All Registered Beans ===");
        for (String beanName : beanNames) {
            Object bean = factory.getBean(beanName);
            System.out.println(beanName + " -> " + bean.getClass().getName());
        }
    }
}
```

### Check Bean Dependencies

```java
public void checkBeanDependencies(ConfigurableListableBeanFactory factory, String beanName) {
    try {
        BeanDefinition beanDef = factory.getBeanDefinition(beanName);
        PropertyValues properties = beanDef.getPropertyValues();
        
        System.out.println("=== Dependencies for " + beanName + " ===");
        for (PropertyValue pv : properties.getPropertyValues()) {
            System.out.println(pv.getName() + " = " + pv.getValue());
        }
    } catch (Exception e) {
        System.out.println("Could not get dependencies for " + beanName + ": " + e.getMessage());
    }
}
```

### Performance Monitoring

```java
@Component
public class PerformanceMonitor implements BeanPostProcessor {
    private final Map<String, Long> initTimes = new HashMap<>();
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        initTimes.put(beanName, System.currentTimeMillis());
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        long time = System.currentTimeMillis() - initTimes.get(beanName);
        if (time > 100) { // Log slow initializations
            System.out.println("SLOW INIT: " + beanName + " took " + time + "ms");
        }
        return bean;
    }
}
```

---

## Common AspectJ Pointcut Expressions

```java
// All methods in service package
"execution(* com.example.service.*.*(..))"

// All methods starting with 'save'
"execution(* save*(..))"

// All methods with specific return type
"execution(com.example.User com.example.service.*.*(..))"

// All methods with specific parameter
"execution(* *(.., String))"

// All methods in classes annotated with @Service
"execution(* (@com.example.Service *).*(..))"

// All public methods
"execution(public * *(..))"

// All methods in specific class
"execution(* com.example.UserService.*(..))"
```

---

## Error Messages and Solutions

### Bean Not Found

**Error:**
```
BeansException: No bean named 'userService' is defined
```

**Quick Fixes:**
```java
// 1. Check bean name
context.getBean("userService", UserService.class); // Correct name?

// 2. List all beans
String[] beans = ((ListableBeanFactory) context).getBeanDefinitionNames();
System.out.println(Arrays.toString(beans));

// 3. Check component scanning
<context:component-scan base-package="com.example"/>

// 4. Verify @Component annotation
@Component("userService") // Explicit name
public class UserService { }
```

### Property Injection Failed

**Error:**
```
BeanCreationException: Could not set property 'databaseUrl'
```

**Quick Fixes:**
```java
// 1. Check setter method exists
public void setDatabaseUrl(String databaseUrl) { ... }

// 2. Check property placeholder
<bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:app.properties"/>
</bean>

// 3. Verify property exists in file
database.url=jdbc:mysql://localhost:3306/db
```

### Circular Dependency

**Error:**
```
BeanCreationException: Circular dependency detected
```

**Quick Fix:**
```java
// Use setter injection instead of constructor injection
@Component
public class ServiceA {
    private ServiceB serviceB;
    
    public void setServiceB(ServiceB serviceB) { // Setter injection
        this.serviceB = serviceB;
    }
}
```

### AOP Not Working

**Quick Checks:**
```xml
<!-- 1. Enable auto-proxy creator -->
<bean class="com.avezah.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>

<!-- 2. Check pointcut expression -->
<bean id="advisor" class="com.avezah.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
    <property name="expression" value="execution(* com.example.service.*.*(..))"/>
    <property name="advice" ref="myAdvice"/>
</bean>

<!-- 3. Verify advice implementation -->
<bean id="myAdvice" class="com.example.MyAdvice"/>
```

---

## Maven Dependencies Quick Setup

```xml
<dependencies>
    <!-- Small Spring Core -->
    <dependency>
        <groupId>com.avezah.springframework</groupId>
        <artifactId>small-spring-step-13</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Required Runtime -->
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
    
    <!-- Testing -->
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
```

---

## Bean Lifecycle Cheat Sheet

```
1. Bean Definition Created
2. Bean Instantiated
3. Properties Set
4. BeanNameAware.setBeanName()
5. BeanFactoryAware.setBeanFactory()
6. ApplicationContextAware.setApplicationContext()
7. BeanPostProcessor.postProcessBeforeInitialization()
8. InitializingBean.afterPropertiesSet() OR init-method
9. BeanPostProcessor.postProcessAfterInitialization()
10. Bean Ready for Use
11. DisposableBean.destroy() OR destroy-method (on shutdown)
```

---

## Scope Comparison

| Scope | Instance Creation | Use Case | Thread Safety |
|-------|------------------|----------|---------------|
| singleton | Once per container | Stateless services | Must be thread-safe |
| prototype | Every getBean() call | Stateful objects | Inherently safe |

---

## Interface Quick Reference

### Core Interfaces

```java
// Basic bean access
BeanFactory.getBean(String name)
BeanFactory.getBean(String name, Class<T> type)

// Bean listing
ListableBeanFactory.getBeansOfType(Class<T> type)
ListableBeanFactory.getBeanDefinitionNames()

// Configuration
ConfigurableListableBeanFactory.getBeanDefinition(String name)
ConfigurableListableBeanFactory.preInstantiateSingletons()

// Context operations
ApplicationContext extends ListableBeanFactory, ResourceLoader, ApplicationEventPublisher
ConfigurableApplicationContext.refresh()
ConfigurableApplicationContext.close()
```

### Lifecycle Interfaces

```java
// Initialization
InitializingBean.afterPropertiesSet()

// Cleanup
DisposableBean.destroy()

// Bean processing
BeanPostProcessor.postProcessBeforeInitialization(Object bean, String name)
BeanPostProcessor.postProcessAfterInitialization(Object bean, String name)
```

### Awareness Interfaces

```java
BeanNameAware.setBeanName(String name)
BeanFactoryAware.setBeanFactory(BeanFactory beanFactory)
ApplicationContextAware.setApplicationContext(ApplicationContext context)
BeanClassLoaderAware.setBeanClassLoader(ClassLoader classLoader)
```

---

## Testing Quick Setup

### Test Class Template

```java
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.assertj.core.api.Assertions.assertThat;

public class MyServiceTest {
    private ApplicationContext context;
    private MyService myService;
    
    @Before
    public void setUp() {
        context = new ClassPathXmlApplicationContext("test-context.xml");
        myService = context.getBean("myService", MyService.class);
    }
    
    @After
    public void tearDown() {
        ((ConfigurableApplicationContext) context).close();
    }
    
    @Test
    public void testSomething() {
        // Test implementation
        assertThat(myService).isNotNull();
    }
}
```

### Test Configuration

```xml
<!-- test-context.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context">
    
    <context:component-scan base-package="com.example"/>
    
    <!-- Test-specific overrides -->
    <bean id="testDataSource" class="com.example.TestDataSource"/>
    
</beans>
```

---

## Property Files Examples

### application.properties

```properties
# Database
database.url=jdbc:mysql://localhost:3306/myapp
database.username=admin
database.password=secret
database.pool.size=10

# Email
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.from=noreply@myapp.com

# Application
app.name=My Application
app.version=1.0.0
app.debug=true

# Cache
cache.enabled=true
cache.ttl=3600
cache.maxSize=1000
```

---

## Annotation Quick Reference

### @Component

```java
@Component                    // Default name: myService
@Component("customName")      // Custom name: customName
public class MyService { }
```

### @Scope

```java
@Scope("singleton")    // Default - one instance
@Scope("prototype")    // New instance each time
```

---

## Event System Quick Reference

### Create Event

```java
public class MyEvent extends ApplicationEvent {
    private final String data;
    
    public MyEvent(Object source, String data) {
        super(source);
        this.data = data;
    }
    
    public String getData() { return data; }
}
```

### Create Listener

```java
@Component
public class MyListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        System.out.println("Received: " + event.getData());
    }
}
```

### Publish Event

```java
@Component
public class MyPublisher implements ApplicationContextAware {
    private ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }
    
    public void doSomething() {
        context.publishEvent(new MyEvent(this, "Hello"));
    }
}
```

---

## Performance Tips

### 1. Startup Optimization

```xml
<!-- Use lazy initialization for non-critical beans -->
<bean id="expensiveBean" class="com.example.ExpensiveBean" lazy-init="true"/>

<!-- Or globally -->
<beans default-lazy-init="true">
    <!-- All beans lazy by default -->
</beans>
```

### 2. Memory Optimization

```java
@Component
public class CacheService implements DisposableBean {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    @Override
    public void destroy() throws Exception {
        cache.clear(); // Clean up on shutdown
    }
}
```

### 3. AOP Performance

```java
// Specific pointcuts are faster
"execution(* com.example.UserService.save*(..))"  // Good

// Avoid overly broad pointcuts
"execution(* *.*(..))"  // Avoid - too broad
```

---

## Common Mistakes to Avoid

### 1. Circular Dependencies

```java
// BAD - Constructor injection causing circular dependency
@Component
public class ServiceA {
    public ServiceA(ServiceB serviceB) { } // BAD
}

@Component
public class ServiceB {
    public ServiceB(ServiceA serviceA) { } // BAD
}

// GOOD - Setter injection
@Component
public class ServiceA {
    private ServiceB serviceB;
    public void setServiceB(ServiceB serviceB) { this.serviceB = serviceB; }
}
```

### 2. Missing Setter Methods

```java
// BAD - No setter for property
public class MyService {
    private String host; // No setter
}

// GOOD - Proper setter
public class MyService {
    private String host;
    public void setHost(String host) { this.host = host; } // Setter exists
}
```

### 3. Incorrect Property Names

```xml
<!-- BAD - Property name doesn't match setter -->
<property name="hostName" value="localhost"/> <!-- No setHostName method -->

<!-- GOOD - Matches setter method -->
<property name="host" value="localhost"/> <!-- Has setHost method -->
```

### 4. Forgetting Resource Cleanup

```java
// BAD - No cleanup
@Component
public class FileService {
    private FileInputStream stream;
}

// GOOD - Proper cleanup
@Component
public class FileService implements DisposableBean {
    private FileInputStream stream;
    
    @Override
    public void destroy() throws Exception {
        if (stream != null) {
            stream.close();
        }
    }
}
```

---

## Useful Utilities

### Bean Existence Check

```java
public boolean beanExists(ApplicationContext context, String beanName) {
    try {
        context.getBean(beanName);
        return true;
    } catch (BeansException e) {
        return false;
    }
}
```

### Type-Safe Bean Retrieval

```java
public <T> Optional<T> getOptionalBean(ApplicationContext context, String name, Class<T> type) {
    try {
        return Optional.of(context.getBean(name, type));
    } catch (BeansException e) {
        return Optional.empty();
    }
}
```

### Context Health Check

```java
public boolean isContextHealthy(ApplicationContext context) {
    try {
        if (context instanceof ListableBeanFactory) {
            String[] beanNames = ((ListableBeanFactory) context).getBeanDefinitionNames();
            return beanNames.length > 0;
        }
        return false;
    } catch (Exception e) {
        return false;
    }
}
```

---

## File Structure Template

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── Application.java
│   │       ├── config/
│   │       │   └── AppConfig.java
│   │       ├── service/
│   │       │   ├── UserService.java
│   │       │   └── EmailService.java
│   │       ├── repository/
│   │       │   └── UserRepository.java
│   │       ├── model/
│   │       │   └── User.java
│   │       └── aop/
│   │           └── LoggingAdvice.java
│   └── resources/
│       ├── applicationContext.xml
│       ├── application.properties
│       └── logback.xml
└── test/
    ├── java/
    │   └── com/example/
    │       └── service/
    │           └── UserServiceTest.java
    └── resources/
        └── test-context.xml
```

---

## Quick Troubleshooting Checklist

### Context Won't Start

- [ ] Check XML syntax
- [ ] Verify all referenced classes exist
- [ ] Check classpath for configuration files
- [ ] Verify all property placeholders have values
- [ ] Check for circular dependencies

### Bean Not Found

- [ ] Check bean name spelling
- [ ] Verify component scanning package
- [ ] Check @Component annotation
- [ ] List all beans to verify registration

### Properties Not Injected

- [ ] Verify PropertyPlaceholderConfigurer is configured
- [ ] Check property file exists in classpath
- [ ] Verify setter method exists and matches property name
- [ ] Check property syntax: `${property.name}`

### AOP Not Working

- [ ] Check DefaultAdvisorAutoProxyCreator is registered
- [ ] Verify pointcut expression syntax
- [ ] Check advice implementation
- [ ] Ensure target class is not final (for CGLIB)

---

## Version Compatibility

| Small Spring Version | Java Version | Dependencies |
|---------------------|--------------|--------------|
| step-13 | Java 8+ | cglib 3.3.0, aspectjweaver 1.9.7, dom4j 2.1.3 |
| step-12 | Java 8+ | cglib 3.3.0, aspectjweaver 1.9.7 |
| step-11 | Java 8+ | cglib 3.3.0, aspectjweaver 1.9.7 |
| step-01-10 | Java 8+ | Basic dependencies only |

---

## Next Steps

1. **Start Simple**: Begin with XML configuration and basic beans
2. **Add Components**: Use @Component for automatic discovery
3. **Configure Properties**: Add PropertyPlaceholderConfigurer for external configuration
4. **Implement Lifecycle**: Add InitializingBean and DisposableBean as needed
5. **Add AOP**: Implement cross-cutting concerns with aspects
6. **Handle Events**: Use application events for loose coupling
7. **Optimize**: Add performance monitoring and lazy initialization

For complete examples and detailed explanations, see:
- [API Documentation](API_DOCUMENTATION.md)
- [Component Documentation](COMPONENT_DOCUMENTATION.md)
- [Usage Guide](USAGE_GUIDE.md)