//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.asset.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
@Slf4j
@Component
public class RedisClusterClient {

    public static final int EXPIRE_DAY = 86400;
    public static final int EXPIRE_HELF_DAY = 43200;
    public static final int EXPIRE_HOUR = 3600;
    public static final int EXPIRE_HALF_HOUR = 1800;
    public static final int EXPIRE_TEN_MIN = 600;
    public static final int EXPIRE_ONE_MIN = 60;
    private static final String KEY_EMPTY = "key值为空";
    private static final String NO_VALUE = "参数值为空";
    @Resource
    private RedisTemplate redisTemplate;
    private Gson gson = (new GsonBuilder()).create();

    public RedisClusterClient() {
    }

    @Autowired(
            required = false
    )
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    public boolean set(String key, String value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForValue().set(key, value);
                return true;
            } catch (RuntimeException var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public void multi() {
        this.redisTemplate.multi();
    }

    public void exec() {
        this.redisTemplate.exec();
    }

    public boolean set(String key, String value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                if (time > 0L) {
                    this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                } else {
                    this.set(key, value);
                }

                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean setObject(String key, Object value) {
        if (null != key && !"".equals(key)) {
            try {
                return this.set(key, this.gson.toJson(value));
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean setObject(String key, Object value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                return this.set(key, this.gson.toJson(value), time);
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (null != key && !"".equals(key)) {
            try {
                String json = this.get(key);
                return this.gson.fromJson(json, clazz);
            } catch (JsonSyntaxException var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean setList(String key, List<?> objList) {
        if (null != key && !"".equals(key)) {
            try {
                String json = this.gson.toJson(objList);
                return this.set(key, json);
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean setList(String key, List<?> objList, long time) {
        if (null != key && !"".equals(key)) {
            try {
                String json = this.gson.toJson(objList);
                return this.set(key, json, time);
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public <T> List<T> getList(String key, Type type) {
        if (null != key && !"".equals(key)) {
            try {
                String json = this.get(key);
                return (List)this.gson.fromJson(json, type);
            } catch (JsonSyntaxException var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean delCacheByKey(String key) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForValue().getOperations().delete(key);
                return true;
            } catch (Exception var3) {
                log.error(var3.getMessage(), var3.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean delCacheByKey(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    this.redisTemplate.delete(key[0]);
                    return true;
                } else {
                    this.redisTemplate.delete(CollectionUtils.arrayToList(key));
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3.getCause());
            return false;
        }
    }

    public long getExpireTime(String key) {
        if (null != key && !"".equals(key)) {
            long time = this.redisTemplate.getExpire(key);
            return time;
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long getExpireTimeForSec(String key) {
        if (null != key && !"".equals(key)) {
            long time = this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return time;
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long getExpireTimeForMin(String key) {
        if (null != key && !"".equals(key)) {
            long time = this.redisTemplate.getExpire(key, TimeUnit.MINUTES);
            return time;
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long getExpireTimeForHour(String key) {
        if (null != key && !"".equals(key)) {
            long time = this.redisTemplate.getExpire(key, TimeUnit.HOURS);
            return time;
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long getExpireTimeForDay(String key) {
        if (null != key && !"".equals(key)) {
            long time = this.redisTemplate.getExpire(key, TimeUnit.DAYS);
            return time;
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5.getCause());
            return false;
        }
    }

    public boolean expireAt(String key, final long date) {
        try {
            if (date > 0L) {
                final byte[] rawKey = this.rawKey(key);
                this.redisTemplate.execute(new RedisCallback<Boolean>() {
                    public Boolean doInRedis(RedisConnection connection) {
                        return connection.expireAt(rawKey, (new Date(date)).getTime() / 1000L);
                    }
                }, true);
            }

            return true;
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5.getCause());
            return false;
        }
    }

    private byte[] rawKey(Object key) {
        return this.keySerializer() == null && key instanceof byte[] ? (byte[])((byte[])key) : this.keySerializer().serialize(key);
    }

    private RedisSerializer keySerializer() {
        return this.redisTemplate.getKeySerializer();
    }

    public boolean hasKey(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.hasKey(key);
            } catch (Exception var3) {
                log.error(var3.getMessage(), var3.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public String get(String key) {
        if (null != key && !"".equals(key)) {
            return (String)this.redisTemplate.opsForValue().get(key);
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, delta);
        }
    }

    public long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, -delta);
        }
    }

    public boolean setByMap(Map<String, String> map) {
        if (map.isEmpty()) {
            log.info("参数值为空");
            return false;
        } else {
            try {
                this.redisTemplate.opsForValue().multiSet(map);
                return true;
            } catch (Exception var3) {
                log.error(var3.getMessage(), var3.getCause());
                return false;
            }
        }
    }

    public Object setByMap(List<String> keys) {
        if (keys.isEmpty()) {
            log.info("参数值为空");
            return null;
        } else {
            try {
                return this.redisTemplate.opsForValue().multiGet(keys);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public Integer strAppend(String key, String value) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForValue().append(key, value);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public String get(String key, long start, long end) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForValue().get(key, start, end);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Object hget(String key, String item) {
        if (null != key && !"".equals(key)) {
            return this.redisTemplate.opsForHash().get(key, item);
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Map<Object, Object> hmget(String key) {
        if (null != key && !"".equals(key)) {
            return this.redisTemplate.opsForHash().entries(key);
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean hmset(String key, Map<String, String> map) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForHash().putAll(key, map);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean hmset(String key, Map<String, String> map, long time) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForHash().putAll(key, map);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean hset(String key, String item, String value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForHash().put(key, item, value);
                return true;
            } catch (Exception var5) {
                log.error(var5.getMessage(), var5.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean hset(String key, String item, String value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForHash().put(key, item, value);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return true;
            } catch (Exception var7) {
                log.error(var7.getMessage(), var7.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean hdel(String key, String... item) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForHash().delete(key, item);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean hHasKey(String key, String item) {
        if (null != key && !"".equals(key)) {
            return this.redisTemplate.opsForHash().hasKey(key, item);
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public List hmGet(String key, List keys) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForHash().multiGet(key, keys);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set hKeys(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForHash().keys(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long hSize(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForHash().size(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public List hValues(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForHash().values(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Cursor<Entry<Object, Object>> hScan(String key, ScanOptions scanOptions) {
        if (null != key && !"".equals(key)) {
            try {
                return null != scanOptions ? this.redisTemplate.opsForHash().scan(key, scanOptions) : this.redisTemplate.opsForHash().scan(key, ScanOptions.NONE);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set sGet(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().members(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean sHasKey(String key, String value) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().isMember(key, value);
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public long sSet(String key, String... values) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().add(key, values);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long intersectAndStore(String key1, String key2, String key3) {
        if (null != key1 && !"".equals(key1) && null != key2 && !"".equals(key2) && null != key3 && !"".equals(key3)) {
            try {
                return this.redisTemplate.opsForSet().intersectAndStore(key1, key2, key3);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Double zScore(String key, String member) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForZSet().score(key, member);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return 0.0D;
        }
    }

    public boolean zAdd(String key, String value, double score) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForZSet().add(key, value, score);
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public Set<String> zrange(String key, long start, long end) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zset = this.redisTemplate.opsForZSet();
                return zset.range(key, start, end);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set<String> zReverseRange(String key, long start, long end) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zset = this.redisTemplate.opsForZSet();
                return zset.reverseRange(key, start, end);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set<String> zReverseRangeByScore(String key, double score1, double score2) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zset = this.redisTemplate.opsForZSet();
                return zset.reverseRangeByScore(key, score1, score2);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set<String> zRangeByScore(String key, double score1, double score2) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zset = this.redisTemplate.opsForZSet();
                return zset.rangeByScore(key, score1, score2);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public <T> List<T> getSortDataDESC(int page, int pageSize, String order, Class<T> clz) {
        int start = page;
        int end = page + pageSize - 1;
        if (page == 1) {
            start = 0;
        }

        List<T> list = new ArrayList();
        Set<String> coinIdNames = this.zReverseRange(order, (long)start, (long)end);
        Iterator var9 = coinIdNames.iterator();

        while(var9.hasNext()) {
            String coinKey = (String)var9.next();
            T data = this.getObject(coinKey, clz);
            if (data != null) {
                list.add(data);
            }
        }

        return list;
    }

    public <T> List<T> getSortDataASC(int page, int pageSize, String order, Class<T> clz) {
        int start = page;
        int end = page + pageSize - 1;
        if (page == 1) {
            start = 0;
        }

        List<T> list = new ArrayList();
        Set<String> coinIdNames = this.zrange(order, (long)start, (long)end);
        Iterator var9 = coinIdNames.iterator();

        while(var9.hasNext()) {
            String coinKey = (String)var9.next();
            T data = this.getObject(coinKey, clz);
            list.add(data);
        }

        return list;
    }

    public Set<String> setMembers(String key) {
        if (null != key && !"".equals(key)) {
            try {
                SetOperations<String, String> set = this.redisTemplate.opsForSet();
                return set.members(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Long zCard(String key) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zSet = this.redisTemplate.opsForZSet();
                return zSet.zCard(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Long zRemove(String key, String member) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zSet = this.redisTemplate.opsForZSet();
                return zSet.remove(key, new String[]{member});
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Long zRank(String key, String member) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zSet = this.redisTemplate.opsForZSet();
                return zSet.rank(key, member);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Long zReverseRank(String key, String member) {
        if (null != key && !"".equals(key)) {
            try {
                ZSetOperations<String, String> zSet = this.redisTemplate.opsForZSet();
                return zSet.reverseRank(key, member);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long sSetAndTime(String key, long time, String... values) {
        if (null != key && !"".equals(key)) {
            try {
                Long count = this.redisTemplate.opsForSet().add(key, values);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return count;
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long sGetSetSize(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().size(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long setRemove(String key, String... values) {
        if (null != key && !"".equals(key)) {
            try {
                Long count = this.redisTemplate.opsForSet().remove(key, values);
                return count;
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Object sPop(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().pop(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean sMove(String key, String value, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().move(key, value, destKey);
            } catch (Exception var5) {
                log.error(var5.getMessage(), var5.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public Set sIntersect(String key, String otherKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().intersect(key, otherKey);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long sIntersectStore(String key, String otherKey, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Set sIntersect(String key, List<String> keys) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().intersect(key, keys);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long sIntersectStore(String key, List<String> keys, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().intersectAndStore(key, keys, destKey);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Set sUnion(String key, String otherKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().union(key, otherKey);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set sUnion(String key, List<String> otherKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().union(key, otherKey);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long sUnion(String key, String otherKey, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long sUnion(String key, List<String> otherKey, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Set sDiff(String key, String otherKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().difference(key, otherKey);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set sDiff(String key, List<String> otherKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().difference(key, otherKey);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long sDiff(String key, String otherKey, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public long sDiff(String key, List<String> otherKey, String destKey) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Object sRandom(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().randomMember(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public List random(String key, long count) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().randomMembers(key, count);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Set sDistinctRandom(String key, long count) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForSet().distinctRandomMembers(key, count);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Cursor sScan(String key, ScanOptions scanOptions) {
        if (null != key && !"".equals(key)) {
            try {
                return null != scanOptions ? this.redisTemplate.opsForSet().scan(key, scanOptions) : this.redisTemplate.opsForSet().scan(key, ScanOptions.NONE);
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean sIsMember(String key, String member) {
        if (null != key && !"".equals(key)) {
            try {
                return null != member ? this.redisTemplate.opsForSet().isMember(key, member) : false;
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public List lGet(String key, long start, long end) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().range(key, start, end);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public long lGetListSize(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().size(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public Object lGetIndex(String key, long index) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().index(key, index);
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean lSet(String key, String value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().rightPush(key, value);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSet(String key, String value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().rightPush(key, value);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSet(String key, List<String> value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().rightPushAll(key, value);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSet(String key, List<String> value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().rightPushAll(key, value);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSetFromLeft(String key, String value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().leftPush(key, value);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSetFromLeft(String key, String value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().leftPush(key, value);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSetFromLeft(String key, List<String> value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().leftPushAll(key, value);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lSetFromLeft(String key, List<String> value, long time) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().leftPushAll(key, value);
                if (time > 0L) {
                    this.expire(key, time);
                }

                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, String value) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().set(key, index, value);
                return true;
            } catch (Exception var6) {
                log.error(var6.getMessage(), var6.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public long lRemove(String key, long count, String value) {
        if (null != key && !"".equals(key)) {
            try {
                Long remove = this.redisTemplate.opsForList().remove(key, count, value);
                return remove;
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        } else {
            log.info("key值为空");
            return 0L;
        }
    }

    public boolean lTrim(String key, long start, long end) {
        if (null != key && !"".equals(key)) {
            try {
                this.redisTemplate.opsForList().trim(key, start, end);
                return true;
            } catch (Exception var7) {
                log.error(var7.getMessage(), var7.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public Object lLeftPop(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().leftPop(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Object lLeftPop(String key, long timeout, TimeUnit unit) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().leftPop(key, timeout, unit);
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Object lRightPop(String key) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().rightPop(key);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public Object lRightPop(String key, long timeout, TimeUnit unit) {
        if (null != key && !"".equals(key)) {
            try {
                return this.redisTemplate.opsForList().rightPop(key, timeout, unit);
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        } else {
            log.info("key值为空");
            return null;
        }
    }

    public boolean rightPopAndLeftPush(String sourceKey, String destinationKey) {
        if (null != sourceKey && !"".equals(sourceKey) && null != destinationKey && !"".equals(destinationKey)) {
            try {
                this.redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
                return true;
            } catch (Exception var4) {
                log.error(var4.getMessage(), var4.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }

    public boolean rightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        if (null != sourceKey && !"".equals(sourceKey) && null != destinationKey && !"".equals(destinationKey)) {
            try {
                this.redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
                return true;
            } catch (Exception var7) {
                log.error(var7.getMessage(), var7.getCause());
                return false;
            }
        } else {
            log.info("key值为空");
            return false;
        }
    }
}
