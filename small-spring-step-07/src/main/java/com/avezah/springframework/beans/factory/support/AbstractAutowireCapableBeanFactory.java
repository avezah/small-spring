package com.avezah.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.PropertyValue;
import com.avezah.springframework.beans.PropertyValues;
import com.avezah.springframework.beans.factory.DisposableBean;
import com.avezah.springframework.beans.factory.InitializingBean;
import com.avezah.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.config.BeanPostProcessor;
import com.avezah.springframework.beans.factory.config.BeanReference;
import com.sun.xml.internal.ws.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

	private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean = null;
		try {
			bean = createBeanInstance(beanName, beanDefinition, args);
			applyPropertyValue(beanName, bean, beanDefinition);
			bean = initializeBean(beanName, bean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}
		registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
		super.addSingleton(beanName, bean);
		return bean;
	}

	protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Constructor<?> constructorToUse = null;
		Class<?> beanClass = beanDefinition.getBeanClass();
		Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
		for (Constructor<?> ctor: declaredConstructors) {
			// 这里仅验证了参数列表长度，实际上 Spring 还验证了参数类型是否一致
			if (null != args && ctor.getParameterTypes().length == args.length) {
				constructorToUse = ctor;
				break;
			}
		}
		return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
	}

	protected void applyPropertyValue(String beanName, Object bean, BeanDefinition beanDefinition) {
		try {
			PropertyValues propertyValues = beanDefinition.getPropertyValues();
			for (PropertyValue propertyValue: propertyValues.getPropertyValues()) {

				String name = propertyValue.getName();
				Object value = propertyValue.getValue();
				// A 依赖 B，获取 B 的实例化
				if (value instanceof BeanReference) {
					BeanReference beanReference = (BeanReference) value;
					// 这里会递归调用一步步实例化依赖
					value = getBean(beanReference.getBeanName());
				}
				// 属性填充
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception e) {
			throw new BeansException("Error setting property values: " + beanName);
		}
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return this.instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}

	private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
		try {
			invokeInitMethods(beanName, wrappedBean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
		}

		wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		return wrappedBean;
	}

	private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception{
		// 1. 处理实现了InitializingBean接口的bean
		if (bean instanceof InitializingBean) {
			((InitializingBean) bean).afterPropertiesSet();
		}

		// 2. 处理定义在xml中的init-method
		String initMethodName = beanDefinition.getInitMethodName();
		if (StrUtil.isNotEmpty(initMethodName)) {
			Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
			if (null == initMethod) {
				throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
			}
			initMethod.invoke(bean);
		}
	}

	protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
		if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
			registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
		}
 	}

	@Override
	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
		Object result = existingBean;
		for (BeanPostProcessor postProcessor: getBeanPostProcessors()) {
			Object current = postProcessor.postProcessBeforeInitialization(existingBean, beanName);
			if (current == null) return result;
			result = current;
		}
		return result;
	}

	@Override
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
		Object result = existingBean;
		for (BeanPostProcessor postProcessor: getBeanPostProcessors()) {
			Object current = postProcessor.postProcessAfterInitialization(existingBean, beanName);
			if (current == null) return result;
			result = current;
		}
		return result;
	}
}
