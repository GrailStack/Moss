package org.xujin.moss.client.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Program: moss
 * @Description:
 * @Author: xujin
 * @Create: 2019/2/21 14:04
 **/
@Slf4j
@ConfigurationProperties(prefix = "endpoints.cacheManager")
public class CacheManagerEndpoint  extends AbstractNamedMvcEndpoint implements ApplicationContextAware {

    ApplicationContext applicationContext;
    @Autowired
    List<CacheManager> cacheManagerList;

    public CacheManagerEndpoint(){
        super("cacheManager", "/cacheManager", true);
    }

    @GetMapping()
    @ResponseBody
    public Object getAllCacheManager(){
        return applicationContext.getBeansOfType(CacheManager.class).entrySet().stream()
                .map((entry)->{
                    entry.getKey();
                    Map<String,Object> cm=new HashMap<>();
                    cm.put("name",entry.getKey());
                    cm.put("caches",entry.getValue().getCacheNames());
                    return cm;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{cacheManager}")
    public Object getAllCache(@PathVariable("cacheManager") String cacheManager){
        return applicationContext.getBeansOfType(CacheManager.class).entrySet().stream()
                .filter(en->en.getKey().equals(cacheManager))
                .map(en->en.getValue().getCacheNames())
                .findFirst().orElseGet(ArrayDeque::new);
    }

    @DeleteMapping("/{cacheManager}/{cache}")
    public Object clearCache(@PathVariable("cacheManager") String cacheManager,@PathVariable("cache") String cache){
        applicationContext.getBeansOfType(CacheManager.class).entrySet().stream()
                .filter(en->en.getKey().equals(cacheManager))
                .forEach(en->{
                    Cache c = en.getValue().getCache(cache);
                    if(c!=null){
                        c.clear();
                    }
                });
        return null;
    }

    @DeleteMapping("/{cacheManager}/{cache}/{key}")
    public Object evictCacheKey(@PathVariable("cacheManager") String cacheManager,@PathVariable("cache") String cache,@PathVariable("cache") String key){
        applicationContext.getBeansOfType(CacheManager.class).entrySet().stream()
                .filter(en->en.getKey().equals(cacheManager))
                .forEach(en->{
                    Cache c = en.getValue().getCache(cache);
                    if(c!=null){
                        c.evict(key);
                    }
                });
        return null;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
