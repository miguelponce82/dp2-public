package org.springframework.samples.petclinic.configuration;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CacheLogger implements CacheEventListener<Object, Object>{

	private final Logger log = LoggerFactory.getLogger(CacheLogger.class);	
	@Override
	public void onEvent(CacheEvent<? , ?> cacheEvent) {
		log.info("Key: {} | EventType: {} | Old value: {} | New value: {} ", cacheEvent.getKey(), cacheEvent.getType(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
		
	}
	

}
