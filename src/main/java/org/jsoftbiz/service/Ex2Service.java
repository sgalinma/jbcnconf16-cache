package org.jsoftbiz.service;

import org.jsoftbiz.repository.SomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * Example service : Cache aside
 *
 * Please implement TODO lines
 *
 */

@Service
public class Ex2Service implements SomeService {

  private static final Logger LOGGER = LoggerFactory.getLogger("org.jsoftbiz.Demo");

  private SomeRepository repository = new SomeRepository();
  private Cache<String, String> cache;

  public Ex2Service() {
    // TODO : get Ehcache as caching provider ("org.ehcache.jsr107.EhcacheCachingProvider")
    CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
    CacheManager cacheManager = cachingProvider.getCacheManager(); // TODO : Get javax.cache.CacheManager from caching provider
    MutableConfiguration<String, String> configuration = new MutableConfiguration<>(); // TODO : create Mutableconfiguration and set types : String, String
    configuration.setTypes(String.class, String.class);

    // TODO Create Cache
    cache = cacheManager.createCache("cache", configuration);
  }

  @Override
  public String someLogic(final String id) {
    LOGGER.debug("---> Call to service 2");

    String val = cache.get(id);

    // TODO implements Cache Aside pattern to cache the call to the repository
    if( val == null){
      LOGGER.debug("---> Call DB");
      val = repository.readFromDb(id);
      cache.putIfAbsent(id,val);
    }else{
      LOGGER.debug("---> Call CACHE");

    }
    return val;
  }
}
