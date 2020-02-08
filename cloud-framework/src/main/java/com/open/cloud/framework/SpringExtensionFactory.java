/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.open.cloud.framework;

import com.open.cloud.common.spi.dubbo.ExtensionFactory;
import com.open.cloud.common.spi.dubbo.SPI;
import com.open.cloud.common.utils.ConcurrentHashSet;
import com.open.cloud.common.utils.spring.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;

/**
 * @author Leijian
 */
public class SpringExtensionFactory implements ExtensionFactory {
	private static final Logger logger = LoggerFactory.getLogger(SpringExtensionFactory.class);

	private static final Set<ApplicationContext> CONTEXTS = new ConcurrentHashSet<ApplicationContext>();

	public static void addApplicationContext(ApplicationContext context) {
		CONTEXTS.add(context);
		if (context instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) context).registerShutdownHook();
		}
	}

	public static void removeApplicationContext(ApplicationContext context) {
		CONTEXTS.remove(context);
	}

	public static Set<ApplicationContext> getContexts() {
		return CONTEXTS;
	}

	// currently for test purpose
	public static void clearContexts() {
		CONTEXTS.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getExtension(Class<T> type, String name) {

		//SPI should be get from SpiExtensionFactory
		if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
			return null;
		}

		for (ApplicationContext context : CONTEXTS) {
			T bean = BeanFactoryUtils.getOptionalBean(context, name, type);
			if (bean != null) {
				return bean;
			}
		}

		logger.warn("No spring extension (bean) named:" + name + ", try to find an extension (bean) of type " + type.getName());

		return null;
	}
}
