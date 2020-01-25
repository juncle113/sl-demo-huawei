package sl.demo.huawei.huaweiredis;

import java.util.UUID;

public class DistributedLockTest {
    public static void main(String[] args) {
        ServiceOrder service = new ServiceOrder();
        for (int i = 0; i < 20; i++) {
            ThreadBuy client = new ThreadBuy(service);
            client.start();
        }
    }
}

class ServiceOrder {
    private final int MAX = 10;
    DistributedLock DLock = new DistributedLock();
    int n = 0;

    public void handleOder() {
        String userName = UUID.randomUUID().toString().substring(0, 8) + Thread.currentThread().getName();
        String identifier = DLock.getLockWithTimeout("Mate 10", 10000, 2000);
        System.out.println("正在为用户：" + userName + " 处理订单");
        if (n < MAX) {
            n++;
            System.out.println("用户：" + userName + "购买第" + n + "台，剩余" + (MAX - n) + "台");

        } else {
            System.out.println("用户：" + userName + "无法购买，已售罄！");
        }
        DLock.releaseLock("Mate 10", identifier);
    }
}

class ThreadBuy extends Thread {
    private ServiceOrder service;

    public ThreadBuy(ServiceOrder service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.handleOder();
    }
}

/*
正在为用户：4eb5194dThread-15 处理订单
用户：4eb5194dThread-15购买第1台，剩余9台
正在为用户：f50bc9e3Thread-16 处理订单
用户：f50bc9e3Thread-16购买第2台，剩余8台
正在为用户：bb46c26cThread-13 处理订单
用户：bb46c26cThread-13购买第3台，剩余7台
正在为用户：78bd818eThread-17 处理订单
用户：78bd818eThread-17购买第4台，剩余6台
正在为用户：acdc6067Thread-1 处理订单
用户：acdc6067Thread-1购买第5台，剩余5台
正在为用户：325dc70fThread-7 处理订单
用户：325dc70fThread-7购买第6台，剩余4台
正在为用户：1ddfea19Thread-4 处理订单
用户：1ddfea19Thread-4购买第7台，剩余3台
正在为用户：d1d5249aThread-19 处理订单
用户：d1d5249aThread-19购买第8台，剩余2台
正在为用户：d21e43b2Thread-9 处理订单
用户：d21e43b2Thread-9购买第9台，剩余1台
正在为用户：1faf69ebThread-18 处理订单
用户：1faf69ebThread-18购买第10台，剩余0台
正在为用户：189e96e8Thread-14 处理订单
用户：189e96e8Thread-14购买第11台，剩余-1台
正在为用户：5a6e944aThread-5 处理订单
用户：5a6e944aThread-5无法购买，已售罄！
正在为用户：852a3a5eThread-12 处理订单
用户：852a3a5eThread-12无法购买，已售罄！
正在为用户：133b94b9Thread-2 处理订单
用户：133b94b9Thread-2无法购买，已售罄！
正在为用户：a8affa3dThread-10 处理订单
用户：a8affa3dThread-10无法购买，已售罄！
正在为用户：0895fecfThread-6 处理订单
用户：0895fecfThread-6无法购买，已售罄！
正在为用户：c4332063Thread-0 处理订单
用户：c4332063Thread-0无法购买，已售罄！
正在为用户：c87e009cThread-3 处理订单
用户：c87e009cThread-3无法购买，已售罄！
正在为用户：f62932a8Thread-8 处理订单
用户：f62932a8Thread-8无法购买，已售罄！
正在为用户：34ea9beeThread-11 处理订单
用户：34ea9beeThread-11无法购买，已售罄！

 */
