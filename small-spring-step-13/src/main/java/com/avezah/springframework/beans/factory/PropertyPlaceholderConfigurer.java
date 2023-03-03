package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.PropertyValue;
import com.avezah.springframework.beans.PropertyValues;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.avezah.springframework.core.io.DefaultResourceLoader;
import com.avezah.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String DEFAULT_PLACE_HOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACE_HOLDER_SUFFIX = "}";

    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 加载属性配置文件
        Properties properties = loadProperties();

        // 属性值替换占位符
        processProperties(beanFactory, properties);
    }

    private Properties loadProperties() {
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            return properties;
        } catch (IOException e) {
            throw new BeansException("Could not load properties.", e);
        }
    }

    private void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        // 对于所有的BeanDefinition，出现占位符都需要替换
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            resolvePropertyValues(beanDefinition, properties);
        }
    }

    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof String) {
                String strVal = (String) value;
                StringBuffer buf = new StringBuffer(strVal);
                int startIndex = strVal.indexOf(DEFAULT_PLACE_HOLDER_PREFIX);
                int endIndex = strVal.indexOf(DEFAULT_PLACE_HOLDER_SUFFIX);
                if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                    String propKey = strVal.substring(startIndex + 2, endIndex);
                    String propVal = (String) properties.get(propKey);
                    // 属性值覆盖占位符所在的位置
                    buf.replace(startIndex, endIndex + 1, propVal);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), buf.toString()));
                }
            }
        }
    }
}
