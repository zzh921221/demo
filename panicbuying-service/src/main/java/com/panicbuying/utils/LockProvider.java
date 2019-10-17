package com.panicbuying.utils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

/**
 * 分布式锁工具类
 * @author zhaozhonghui
 */
public class LockProvider {

    private static RedissonClient redissonClient;

    public LockProvider() {
    }

    /**
     * 注入redissonClient
     * @param redissonClient
     */
    public static void setRedissonClient(RedissonClient redissonClient) {
        LockProvider.redissonClient = redissonClient;
    }

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 获取加锁
     * @param objectName
     * @return
     */
    public static RLock getLock(String objectName) {
        return redissonClient.getLock(objectName);
    }

    /**
     * 获取公平锁 当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程
     * @param objectName
     * @return
     */
    public static RLock getFairLock(String objectName) {
        return redissonClient.getFairLock(objectName);
    }

    /**
     * 获取联锁 Redisson的RedissonMultiLock对象可以将多个RLock对象关联为一个联锁
     * @param lockKeys
     * @return
     */
    public static Lock getMultiLock(String... lockKeys) {
        RLock[] rLocks = new RLock[lockKeys.length];
        int index = 0;
        String[] var3 = lockKeys;
        int var4 = lockKeys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String lockKey = var3[var5];
            RLock lock = redissonClient.getLock(lockKey);
            rLocks[index++] = lock;
        }

        return new RedissonMultiLock(rLocks);
    }
    /**
     * 获取联锁 Redisson的RedissonMultiLock对象可以将多个RLock对象关联为一个联锁
     * @param lockKeys
     * @return
     */
    public static Lock getMultiLock(List<String> lockKeys) {
        RLock[] rLocks = new RLock[lockKeys.size()];
        int index = 0;

        RLock lock;
        for(Iterator var3 = lockKeys.iterator(); var3.hasNext(); rLocks[index++] = lock) {
            String lockKey = (String)var3.next();
            lock = redissonClient.getLock(lockKey);
        }

        return new RedissonMultiLock(rLocks);
    }

    /**
     * 获取读写锁 该对象允许同时有多个读取锁，但是最多只能有一个写入锁
     * @param objectName
     * @return
     */
    public static RReadWriteLock getReadWriteLock(String objectName) {
        return redissonClient.getReadWriteLock(objectName);
    }

    /**
     * 闭锁
     * @param objectName
     * @return
     */
    public static RCountDownLatch getCountDownLatch(String objectName) {
        return redissonClient.getCountDownLatch(objectName);
    }

    /**
     * 获取信号量
     * @param objectName
     * @return
     */
    public static RSemaphore getSemaphore(String objectName) {
        return redissonClient.getSemaphore(objectName);
    }

    /**
     * 获取可过期性信号量
     * @param objectName
     * @return
     */
    public static RPermitExpirableSemaphore getPermitExpirableSemaphore(String objectName) {
        return redissonClient.getPermitExpirableSemaphore(objectName);
    }
}
