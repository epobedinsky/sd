package org.ogai.core;

import org.ogai.util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collection of all service objects of application. Can be accessed by service name.
 * Exists only one instance for all application
 *
 * @author Побединский Евгений
 *         23.03.14 19:33
 */
public class ServicesRegistry {
	private static ServicesRegistry instance;

	private Map<String, Object> map;

	/**
	 *
	 * @return Newly created instance.
	 * NOTICE! Should be called only once from syncronized code
	 */
	public static  ServicesRegistry createInstance() {
		if (instance != null) {
			throw new IllegalStateException("ServicesRegistry.createInstance should be called only once!!!");
		}
		instance = new ServicesRegistry();
		return instance;
	}

	/**
	 *
	 * @return Existing registry instance
	 */
	public static ServicesRegistry getInstance() {
		if (instance == null) {
			throw new IllegalStateException("ServicesRegistry instance should be initialized at this point!!");
		}
		return instance;
	}

	/**
	 * Register new service object
	 * @param name Not empty name, which was not registred yet
	 * @param newService Not null command object
	 */
	public void register(String name, Object newService) {
		if (Util.isEmpty(name)){
			throw new IllegalArgumentException("ServicesRegistry.register:Name can't be empty");
		}
		if (null == newService){
			throw new IllegalArgumentException("ServicesRegistry.register:Can't register null service");
		}
		if (map.containsKey(name)){
			throw new IllegalArgumentException("ServicesRegistry.register:Service with such name was already registred");
		}

		map.put(name, newService);
	}

	/**
	 *
	 * @param name
	 * @return Service object registred for name. If no service object is founded, exception will
	 * be thrown
	 */
	public Object get(String name) {
		if (!map.containsKey(name)){
			throw new IllegalArgumentException("ServicesRegistry.get:Can't find service with such name");
		}

		return map.get(name);
	}

	/**
	 *
	 * @param exceptedServiceNames Имена сервисов которые не нужно закрывать
	 */
	public void closeAllExcept(String... exceptedServiceNames) {
		Set<String> exceptSet = new HashSet<String>();
		for (String exceptedServiceName : exceptedServiceNames) {
			exceptSet.add(exceptedServiceName);
		}

		for (String serviceName : exceptedServiceNames) {
			if (!exceptSet.contains(serviceName) &&
					map.get(serviceName) instanceof Closeable) {
				((Closeable) map.get(serviceName)).close();
			}
		}
	}

	/**
	 *
	 * @param name
	 * @return true - если в хранилище сервисов есть сервис с таким именем
	 */
	public boolean has(String name) {
		return map.containsKey(name);
	}

	private ServicesRegistry() {
		this.map = new HashMap<String, Object>();
	}
}
