# Small Spring Framework - Component Documentation

## Architecture Overview

The Small Spring Framework is organized into several key packages, each providing specific functionality:

```
com.avezah.springframework/
├── beans/           # Bean management and configuration
├── context/         # Application context and events
├── aop/            # Aspect-oriented programming
├── core/           # Core utilities and resources
├── stereotype/     # Annotations for component marking
└── util/           # General utilities
```

---

## Core Components

### 1. Bean Management Layer (`beans` package)

#### DefaultListableBeanFactory

The main implementation of the bean factory that manages bean definitions and instances.

**Key Responsibilities:**
- Bean definition registration and retrieval
- Bean instantiation and lifecycle management
- Dependency injection
- Singleton and prototype scope management

**Public Methods:**
```java
public class DefaultListableBeanFactory {
    // Bean definition management
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
    String[] getBeanDefinitionNames();
    
    // Bean retrieval
    Object getBean(String beanName);
    <T> T getBean(String name, Class<T> requiredType);
    <T> Map<String, T> getBeansOfType(Class<T> type);
    
    // Lifecycle management
    void preInstantiateSingletons();
}
```

**Usage Example:**
```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

// Register bean definition
BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
beanFactory.registerBeanDefinition("userService", beanDefinition);

// Get bean instance
UserService service = beanFactory.getBean("userService", UserService.class);
```

#### BeanDefinition

Metadata container for bean configuration.

**Key Properties:**
- Bean class type
- Property values for dependency injection
- Scope (singleton/prototype)
- Lifecycle method names (init/destroy)

**Configuration Example:**
```java
BeanDefinition definition = new BeanDefinition(DatabaseService.class);
definition.setScope(BeanDefinition.SCOPE_SINGLETON);
definition.setInitMethodName("connect");
definition.setDestroyMethodName("disconnect");

PropertyValues properties = new PropertyValues();
properties.addPropertyValue(new PropertyValue("host", "localhost"));
properties.addPropertyValue(new PropertyValue("port", 5432));
definition.setPropertyValues(properties);
```

#### Bean Post Processors

Components that intercept bean creation process for customization.

**Available Processors:**
- `ApplicationContextAwareProcessor`: Injects ApplicationContext into aware beans
- `InstantiationAwareBeanPostProcessor`: Handles property population
- Custom processors for logging, validation, etc.

---

### 2. Application Context Layer (`context` package)

#### ClassPathXmlApplicationContext

The primary application context implementation for XML-based configuration.

**Key Features:**
- XML configuration file parsing
- Bean factory integration
- Event publishing capabilities
- Resource loading
- Lifecycle management

**Initialization Process:**
1. Parse XML configuration files
2. Create bean definitions
3. Register bean post processors
4. Instantiate singleton beans
5. Publish ContextRefreshedEvent

**Usage Pattern:**
```java
// Initialize context
ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");

// Use beans
MyService service = context.getBean("myService", MyService.class);
service.performOperation();

// Shutdown gracefully
((ConfigurableApplicationContext) context).registerShutdownHook();
```

#### Event System Components

**ApplicationEventMulticaster**
- Manages event listeners
- Multicasts events to registered listeners
- Supports typed event listening

**Built-in Events:**
- `ContextRefreshedEvent`: Context initialization complete
- `ContextClosedEvent`: Context shutdown initiated

**Event Flow:**
```
Publisher → ApplicationContext → EventMulticaster → Listeners
```

---

### 3. AOP Layer (`aop` package)

#### Proxy Creation Components

**ProxyFactory**
- Central factory for creating AOP proxies
- Chooses between JDK dynamic proxies and CGLIB proxies
- Integrates with AdvisedSupport for configuration

**Proxy Selection Logic:**
```java
private AopProxy createAopProxy() {
    if (advisedSupport.isProxyTargetClass()) {
        return new Cglib2AopProxy(advisedSupport);  // CGLIB for classes
    }
    return new JdkDynamicAopProxy(advisedSupport);   // JDK for interfaces
}
```

#### Pointcut and Advice Components

**AspectJExpressionPointcut**
- Parses AspectJ pointcut expressions
- Matches classes and methods for advice application
- Supports execution pointcuts

**Advice Types:**
- `MethodBeforeAdvice`: Executes before method calls
- `MethodInterceptor`: Wraps method execution
- `BeforeAdvice`: Marker interface for before advice

#### Auto-Proxy Creation

**DefaultAdvisorAutoProxyCreator**
- Automatically creates proxies for beans that match advisors
- Integrates with bean post processor lifecycle
- Enables declarative AOP configuration

---

### 4. Resource Management (`core.io` package)

#### Resource Abstraction

**Resource Interface Hierarchy:**
```
Resource (interface)
├── ClassPathResource    # Classpath-based resources
├── FileSystemResource   # File system resources
└── UrlResource         # URL-based resources
```

**ResourceLoader Implementation:**
```java
public class DefaultResourceLoader implements ResourceLoader {
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else if (location.startsWith("file:")) {
            return new FileSystemResource(location.substring(5));
        } else {
            return new UrlResource(location);
        }
    }
}
```

---

### 5. Configuration Processing

#### XML Bean Definition Reading

**XmlBeanDefinitionReader**
- Parses XML configuration files
- Creates BeanDefinition objects
- Handles property references and values
- Supports component scanning configuration

#### Property Placeholder Resolution

**PropertyPlaceholderConfigurer**
- Processes ${property} placeholders in bean definitions
- Loads properties from files or resources
- Supports multiple property sources

**Processing Flow:**
```
XML Config → Property Placeholder → Resolved Values → Bean Creation
```

---

## Component Relationships

### Dependency Graph

```
ApplicationContext
├── BeanFactory (composition)
│   ├── BeanDefinitionRegistry
│   ├── SingletonBeanRegistry
│   └── AutowireCapableBeanFactory
├── ResourceLoader
├── ApplicationEventPublisher
│   └── ApplicationEventMulticaster
└── BeanFactoryPostProcessor[]
    └── PropertyPlaceholderConfigurer
```

### Lifecycle Integration

```
Context Startup:
1. Parse Configuration → XmlBeanDefinitionReader
2. Process Properties → PropertyPlaceholderConfigurer
3. Scan Components → ClassPathBeanDefinitionScanner
4. Create Bean Definitions → BeanDefinitionRegistry
5. Process Bean Definitions → BeanFactoryPostProcessor
6. Instantiate Beans → InstantiationStrategy
7. Inject Dependencies → Property Population
8. Apply BeanPostProcessors → Initialization
9. Create AOP Proxies → ProxyFactory
10. Publish ContextRefreshedEvent
```

---

## Extension Points

### 1. Custom Bean Post Processors

Implement `BeanPostProcessor` to customize bean creation:

```java
@Component
public class ValidationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // Validate bean before initialization
        if (bean instanceof Validatable) {
            ((Validatable) bean).validate();
        }
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // Post-initialization processing
        return bean;
    }
}
```

### 2. Custom Factory Beans

Create complex beans with special instantiation logic:

```java
@Component
public class ThreadPoolFactoryBean implements FactoryBean<ExecutorService> {
    private int corePoolSize = 5;
    private int maxPoolSize = 10;
    
    @Override
    public ExecutorService getObject() throws Exception {
        return new ThreadPoolExecutor(
            corePoolSize, maxPoolSize, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
        );
    }
    
    @Override
    public Class<?> getObjectType() {
        return ExecutorService.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

### 3. Custom Advice

Implement cross-cutting concerns:

```java
public class PerformanceMonitoringAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            // Method execution happens here
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Method " + method.getName() + 
                             " took " + (endTime - startTime) + "ms");
        }
    }
}
```

### 4. Custom Event Listeners

Handle application events:

```java
@Component
public class AuditEventListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            System.out.println("Application started at: " + new Date());
        } else if (event instanceof ContextClosedEvent) {
            System.out.println("Application stopped at: " + new Date());
        }
    }
}
```

---

## Integration Patterns

### 1. Service Layer Pattern

```java
@Component
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    // Constructor injection (if supported) or setter injection
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    public void registerUser(User user) {
        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail());
    }
}
```

### 2. Repository Pattern

```java
@Component
public class UserRepository {
    private DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void save(User user) {
        // Database operations
    }
    
    public User findById(Long id) {
        // Database operations
        return null;
    }
}
```

### 3. Configuration Pattern

```java
@Component
public class ApplicationConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public void configureApplication() {
        // Access other beans for configuration
        DatabaseService dbService = applicationContext.getBean("databaseService", DatabaseService.class);
        dbService.initialize();
    }
}
```

---

## Testing Components

### Test Support Classes

The framework provides test utilities in the test package:

**Test Bean Example:**
```java
@Component
public class Car {
    private String brand;
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getBrand() {
        return brand;
    }
}
```

**Test Configuration:**
```xml
<!-- package-scan.xml -->
<context:component-scan base-package="com.avezah.springframework.test.bean"/>

<!-- property-placeholder-configurer.xml -->
<bean class="com.avezah.springframework.beans.factory.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:car.properties"/>
</bean>

<bean id="car" class="com.avezah.springframework.test.bean.Car">
    <property name="brand" value="${brand}"/>
</bean>
```

---

## Advanced Features

### 1. Instantiation Strategies

The framework supports multiple instantiation strategies:

- **SimpleInstantiationStrategy**: Direct constructor invocation
- **CglibSubclassingInstantiationStrategy**: CGLIB-based instantiation for enhanced features

### 2. Proxy Mechanisms

- **JDK Dynamic Proxies**: For interface-based proxying
- **CGLIB Proxies**: For class-based proxying

### 3. Bean Scopes

- **Singleton**: Default scope, one instance per container
- **Prototype**: New instance for each request

### 4. Aware Injection

Automatic injection of container infrastructure:
- BeanName, BeanFactory, ApplicationContext
- ClassLoader for advanced class manipulation

---

## Security Considerations

### 1. Class Loading

- Uses secure class loading mechanisms
- Validates class types during bean creation
- Prevents unauthorized class instantiation

### 2. Resource Access

- Validates resource paths
- Prevents directory traversal attacks
- Secure property file loading

### 3. Reflection Usage

- Controlled reflection for bean instantiation
- Validates method access for AOP
- Secure property injection

---

## Monitoring and Debugging

### 1. Bean Creation Monitoring

```java
@Component
public class BeanCreationMonitor implements BeanPostProcessor {
    private final Map<String, Long> creationTimes = new HashMap<>();
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        creationTimes.put(beanName, System.currentTimeMillis());
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        long creationTime = System.currentTimeMillis() - creationTimes.get(beanName);
        System.out.println("Bean " + beanName + " created in " + creationTime + "ms");
        return bean;
    }
}
```

### 2. Context State Inspection

```java
public void inspectContext(ApplicationContext context) {
    if (context instanceof ListableBeanFactory) {
        ListableBeanFactory factory = (ListableBeanFactory) context;
        
        System.out.println("Registered beans:");
        for (String beanName : factory.getBeanDefinitionNames()) {
            System.out.println("- " + beanName);
        }
        
        System.out.println("\nBeans by type:");
        Map<String, Object> allBeans = factory.getBeansOfType(Object.class);
        allBeans.forEach((name, bean) -> 
            System.out.println("- " + name + ": " + bean.getClass().getSimpleName())
        );
    }
}
```

---

## Performance Optimization

### 1. Lazy Initialization

Configure beans for lazy initialization to improve startup time:

```xml
<bean id="expensiveService" class="com.example.ExpensiveService" lazy-init="true">
    <!-- Bean configuration -->
</bean>
```

### 2. Prototype Scope Optimization

For frequently created prototype beans, consider object pooling:

```java
@Component
@Scope("prototype")
public class PooledService {
    // Implement object pooling if needed
}
```

### 3. Event Listener Optimization

Use typed event listeners to avoid unnecessary event processing:

```java
@Component
public class SpecificEventListener implements ApplicationListener<UserRegisteredEvent> {
    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        // Only processes UserRegisteredEvent
    }
}
```

---

## Extension Examples

### Custom Scope Implementation

```java
public class ThreadLocalScope implements Scope {
    private final ThreadLocal<Map<String, Object>> threadLocalScope = 
        ThreadLocal.withInitial(HashMap::new);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = threadLocalScope.get();
        Object object = scope.get(name);
        if (object == null) {
            object = objectFactory.getObject();
            scope.put(name, object);
        }
        return object;
    }
    
    @Override
    public Object remove(String name) {
        return threadLocalScope.get().remove(name);
    }
    
    // Additional scope methods...
}
```

### Custom Resource Implementation

```java
public class DatabaseResource implements Resource {
    private final String query;
    private final DataSource dataSource;
    
    public DatabaseResource(String query, DataSource dataSource) {
        this.query = query;
        this.dataSource = dataSource;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            // Execute query and return results as InputStream
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Convert ResultSet to InputStream
            return convertResultSetToInputStream(rs);
        } catch (SQLException e) {
            throw new IOException("Failed to load database resource", e);
        }
    }
}
```

---

## Integration with External Libraries

### 1. AspectJ Integration

The framework integrates with AspectJ for pointcut expressions:

```java
// Requires aspectjweaver dependency
AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
    "execution(* com.example.service.*.save*(..))"
);
```

### 2. CGLIB Integration

For class-based proxying:

```java
// Requires cglib dependency
Cglib2AopProxy cglibProxy = new Cglib2AopProxy(advisedSupport);
Object proxy = cglibProxy.getProxy();
```

### 3. DOM4J Integration

For XML parsing:

```java
// Used internally by XmlBeanDefinitionReader
// Requires dom4j dependency for XML configuration parsing
```

---

## Component Lifecycle

### Bean Lifecycle

```
1. Bean Definition Creation
2. Bean Instantiation (via InstantiationStrategy)
3. Property Population
4. Aware Interface Injection
5. BeanPostProcessor.postProcessBeforeInitialization()
6. InitializingBean.afterPropertiesSet() or init-method
7. BeanPostProcessor.postProcessAfterInitialization()
8. Bean Ready for Use
9. DisposableBean.destroy() or destroy-method (on shutdown)
```

### Context Lifecycle

```
1. Configuration Loading (XML/Annotation)
2. Bean Definition Registration
3. BeanFactoryPostProcessor Execution
4. Bean Instantiation and Initialization
5. ContextRefreshedEvent Publication
6. Application Runtime
7. Context Shutdown Hook
8. ContextClosedEvent Publication
9. Bean Destruction
10. Context Closed
```

---

## Thread Safety

### Singleton Beans
- Must be thread-safe as they're shared across threads
- Use synchronization or immutable objects when necessary

### Prototype Beans
- New instance per request, inherently thread-safe
- No shared state between instances

### Context Operations
- Bean factory operations are thread-safe
- Event publishing is thread-safe
- Resource loading is thread-safe

---

## Memory Management

### Bean Caching
- Singleton beans are cached in `DefaultSingletonBeanRegistry`
- Prototype beans are not cached
- Factory beans cache their products based on `isSingleton()`

### Cleanup Strategies
- Implement `DisposableBean` for resource cleanup
- Use shutdown hooks for graceful termination
- Clear ThreadLocal variables in custom scopes

---

## Compatibility Notes

### Java Version
- Requires Java 8 or higher
- Uses lambda expressions and streams where appropriate
- Compatible with modern JVM features

### Dependencies
- **Required**: cglib, dom4j, aspectjweaver, aopalliance
- **Test**: junit, assertj-core
- **Optional**: hutool-all for utilities

### Framework Compatibility
- Designed to be compatible with Spring Framework concepts
- Similar API design for easy migration
- Supports standard Spring configuration patterns