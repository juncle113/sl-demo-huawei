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
    int n = 10;

    public void handleOder() {
        String userName = UUID.randomUUID().toString().substring(0, 8) + Thread.currentThread().getName();
        String identifier = DLock.getLockWithTimeout("Mate 10", 10000, 2000);
        System.out.println("正在为用户：" + userName + " 处理订单");
        if (n > 0) {
            int num = MAX - n + 1;
            System.out.println("用户：" + userName + "购买第" + num + "台，剩余" + (--n) + "台");
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
