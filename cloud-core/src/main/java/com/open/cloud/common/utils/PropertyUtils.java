package com.open.cloud.common.utils;

import com.open.cloud.common.base.Callable;
import com.open.cloud.common.utils.spring.SpringApplicationContext;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Leijian
 */
public class PropertyUtils {
	public static String NULL = "<?NULL?>";

	public static void eachProperty(Callable.Action3<String, String, Object> call) {
		for (val key : System.getProperties().stringPropertyNames()) {
			call.invoke("properties", key, System.getProperty(key));
		}
		for (val kv : System.getenv().entrySet()) {
			call.invoke("env", kv.getKey(), kv.getValue());
		}
	}

	private static <T> T getProperty(String key, T defaultvalue) {
		String value = System.getProperty(key);
		if (value == null) {
			value = System.getenv(key);
		}
		if (value == null && SpringApplicationContext.getApplicationContext() != null) {
			value = SpringApplicationContext.getApplicationContext().getEnvironment().getProperty(key);
		}
		if (value == null) {
			return defaultvalue;
		}
		return (T) ConvertUtils.convert(value, defaultvalue.getClass());
	}

	public static Object getProperty(String key) {
		String value = System.getProperty(key);
		if (value == null) {
			value = System.getenv(key);
		}
		if (value == null && SpringApplicationContext.getApplicationContext() != null) {
			value = SpringApplicationContext.getApplicationContext().getEnvironment().getProperty(key);
		}
		return value;
	}

	public static <T> T getEnvProperty(String key, T defaultvalue) {
		String value = System.getenv(key);
		if (value == null) {
			return defaultvalue;
		} else {
			return (T) ConvertUtils.convert(value, defaultvalue.getClass());
		}
	}


	public static <T> T getSystemProperty(String key, T defaultvalue) {
		String value = System.getProperty(key);
		if (value == null) {
			return defaultvalue;
		} else {
			return (T) ConvertUtils.convert(value, defaultvalue.getClass());
		}
	}

	public static void setDefaultInitProperty(Class cls, String module, String key, String propertyValue) {
		setDefaultInitProperty(cls, module, key, propertyValue, "");
	}

	public static void setDefaultInitProperty(Class cls, String module, String key, String propertyValue, String message) {
		if (StringUtils.isEmpty(PropertyUtils.getPropertyCache(key, ""))) {
			if (!StringUtils.isEmpty(propertyValue)) {
				System.setProperty(key, propertyValue);
				//PropertyCache.Default.tryUpdateCache(key, propertyValue);
				LogUtils.info(cls, module, "设置" + key + "=" + propertyValue + " " + message);
			}
		}
	}

	public static <T> T getPropertyCache(String key, T defaultvalue) {
		//return PropertyCache.Default.get(key, defaultvalue);
		return null;
	}

}