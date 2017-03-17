package org.ogai.core;

import org.ogai.command.Executable;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Collection of all Executables of application. Can be accessed by Executable name.
 * Exists only one instance for all application
 *
 * @author Побединский Евгений
 *         23.03.14 18:00
 */
public class CommandsRegistry {

	/**
	 * Команда с таким именем не найдена в CommandsRegistry
	 *
	 */
	public static class UnknownCommandException extends OgaiException {
		/**
		 *
		 * @param ExecutableName имя не найденной команды
		 */
		public UnknownCommandException(String ExecutableName) {
			super("Can't find action with name:" + ExecutableName);
		}
	}

	private static CommandsRegistry instance;

	private Map<String, Executable> executablesMap;

	/**
	 *
	 * @return Newly created instance.
	 * NOTICE! Should be called only once from syncronized code
	 */
	public static  CommandsRegistry createInstance() {
		if (instance != null) {
			throw new IllegalStateException("CommandsRegistry.createInstance should be called only once!!!");
		}
		instance = new CommandsRegistry();
		return instance;
	}

	/**
	 *
	 * @return Existing registry instance
	 */
	public static CommandsRegistry getInstance() {
		if (instance == null) {
			throw new IllegalStateException("CommandsRegistry instance should be initialized at this point!!");
		}
		return instance;
	}

	/**
	 * Register new Executable
	 * @param name Not empty name, wich was not registred yet
	 * @param newExecutable Not null Executable object
	 */
	public void register(String name, Executable newExecutable) {
		if (Util.isEmpty(name)){
			throw new IllegalArgumentException("CommandsRegistry.register:Name can't be empty");
		}
		if (null == newExecutable){
			throw new IllegalArgumentException("CommandsRegistry.register:Can't register null Executable");
		}
		if (executablesMap.containsKey(name)){
			throw new IllegalArgumentException("CommandsRegistry.register:Executable with such name was already registred");
		}

		executablesMap.put(name, newExecutable);
	}

	/**
	 *
	 * @param name
	 * @return Executable registred for name. If no Executable will be found, exception will
	 * be thrown
	 */
	public Executable get(String name) throws UnknownCommandException {
		if (!executablesMap.containsKey(name)){
			throw new UnknownCommandException(name);
		}

		return executablesMap.get(name);
	}

	private CommandsRegistry() {
		this.executablesMap = new HashMap<String, Executable>();
	}
}
