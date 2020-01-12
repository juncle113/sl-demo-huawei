package sl.demo.huawei.huaweiredis;

import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class DistributedLock {
    private final String host = "192.168.0.220";
    private final int port = 6379;
    private static final String SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String EXPIRE_TIME = "PX";

    public DistributedLock() {
    }    /*     * @param lockName      锁名     * @param timeout       获取锁的超时时间     * @param lockTimeout   锁的有效时间     * @return              锁的标识     */

    public String getLockWithTimeout(String lockName, long timeout, long lockTimeout) {
        String ret = null;
        Jedis jedisClient = new Jedis(host, port);
        try {
            String authMsg = jedisClient.auth("******");
            if (!SUCCESS.equals(authMsg)) {
                System.out.println("AUTH FAILED: " + authMsg);
            }
            String identifier = UUID.randomUUID().toString();
            String lockKey = "DLock:" + lockName;
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {
//                String result = jedisClient.set(lockKey, identifier, SET_IF_NOT_EXIST, EXPIRE_TIME, lockTimeout);
                String result = jedisClient.set(lockKey, identifier, SetParams.setParams().nx().px(lockTimeout));
                if (SUCCESS.equals(result)) {
                    ret = identifier;
                    break;
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisClient.quit();
            jedisClient.close();
        }
        return ret;
    }    /*     * @param lockName        锁名     * @param identifier    锁的标识     */

    public void releaseLock(String lockName, String identifier) {
        Jedis jedisClient = new Jedis(host, port);
        try {
            String authMsg = jedisClient.auth("******");
            if (!SUCCESS.equals(authMsg)) {
                System.out.println("AUTH FAILED: " + authMsg);
            }
            String lockKey = "DLock:" + lockName;
            if (identifier.equals(jedisClient.get(lockKey))) {
                jedisClient.del(lockKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisClient.quit();
            jedisClient.close();
        }
    }
}
