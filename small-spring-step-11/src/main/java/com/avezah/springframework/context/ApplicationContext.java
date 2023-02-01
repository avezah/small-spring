package com.avezah.springframework.context;

import com.avezah.springframework.beans.factory.HierarchicalBeanFactory;
import com.avezah.springframework.beans.factory.ListableBeanFactory;
import com.avezah.springframework.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
