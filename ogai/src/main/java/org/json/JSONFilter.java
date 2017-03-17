package org.json;

/**
 * @author anomys
 *	Фильтр при сериализации и десериализации
 */
public class JSONFilter {
	
	public boolean isKeyNeedSerialization(String key){
		return true;
	}

}
