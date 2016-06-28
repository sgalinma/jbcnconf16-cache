package org.jsoftbiz.service;

import org.jsoftbiz.repository.SomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import javax.cache.spi.CachingProvider;
import java.util.Map;

/**
 * Example service : Cache through
 *
 * Please implement TODO lines
 *
 */

@Service
public class Ex4Service implements SomeService {

  private static final Logger LOGGER = LoggerFactory.getLogger("org.jsoftbiz.Demo");

  private SomeRepository repository = new SomeRepository();
  private Cache<String, String> cache;

  public Ex4Service() {
    // TODO : get Ehcache as caching provider ("org.ehcache.jsr107.EhcacheCachingProvider")
    CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
    CacheManager cacheManager = cachingProvider.getCacheManager(); // TODO : Get javax.cache.CacheManager from caching provider

    MutableConfiguration<String, String> configuration = new MutableConfiguration<>(); // TODO : create Mutableconfiguration and set types : String, String
    configuration.setTypes(String.class, String.class);
    // TODO : Add a Cache Loader to the configuration (custom class implementing CacheLoader<String, String>)


    CacheLoader<String,String> cacheLoader = new CacheLoader<String, String>() {
      @Override
      public String load(String key) throws CacheLoaderException {
        String val = repository.readFromDb(key);
        return val;
      }

      @Override
      public Map<String, String> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
        Map<String, String> vals = null;
        for(String k : keys){
          vals.put(k, repository.readFromDb(k));
        }
        return vals;
      }
    };
    //new FactoryBuilder.ClassFactory<>("name own class")
    configuration.setCacheLoaderFactory(() -> cacheLoader);

    configuration.setReadThrough(true); //flag
    cache = cacheManager.createCache("cache2",configuration);

  }

  @Override
  public String someLogic(final String id) {
    LOGGER.debug("---> Call to service 4");

    // TODO : Get the value from the cache directly instead
    return cache.get(id);
  }
}
