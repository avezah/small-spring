package com.avezah.springframework.context.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.avezah.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.avezah.springframework.beans.factory.config.BeanPostProcessor;
import com.avezah.springframework.beans.factory.support.ApplicationContextAwareProcessor;
import com.avezah.springframework.context.ApplicationEvent;
import com.avezah.springframework.context.ApplicationListener;
import com.avezah.springframework.context.ConfigurableApplicationContext;
import com.avezah.springframework.context.event.ApplicationEventMulticaster;
import com.avezah.springframework.context.event.ContextClosedEvent;
import com.avezah.springframework.context.event.ContextRefreshedEvent;
import com.avezah.springframework.context.event.SimpleApplicationEventMulticaster;
import com.avezah.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICAST_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        // 1. 初始化BeanFactory，并加载BeanDefinitions
        refreshBeanFactory();

        // 2. 获取beanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. 感知ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4. Bean实例化之前执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessor(beanFactory);

        // 5，BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);

        // 6. 将Bean加载到单例容器中，实际是遍历BeanDefinition调用getBean方法
        beanFactory.preInstantiateSingletons();

        // * 事件相关初始化及发布

        // 初始化发布者
        initApplicationEventMulticaster();

        // 注册事件监听器
        registerListeners();

        // 发布容器刷新事件
        finishRefresh();

    }

    private void invokeBeanFactoryPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor: beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        // 执行单例Bean的destroy方法
        getBeanFactory().destroySingletons();
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICAST_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) throws BeansException {
        return getBeanFactory().getBean(beanName, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
