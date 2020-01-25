package sl.demo.huawei.huaweiredis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProductSalesRankDemo {
    static final int PRODUCT_KINDS = 30;

    public static void main(String[] args) {
        // 实例连接地址，从控制台获取
        String host = "192.168.9.81";
        // Redis端口
        int port = 6379;
        Jedis jedisClient = new Jedis(host, port);
        try {
            // 实例密码
            String authMsg = jedisClient.auth("hw@815GST");
            if (!authMsg.equals("OK")) {
                System.out.println("AUTH FAILED: " + authMsg);
            }
            // 键分布式缓存服务最佳实践2 使用Redis实现排行榜功能2019-03-206

            String key = "商品热销排行榜";
            jedisClient.del(key);
            // 随机生成产品数据
            List<String> productList = new ArrayList<>();
            for (int i = 0; i < PRODUCT_KINDS; i++) {
                productList.add("product-" + UUID.randomUUID().toString());
            }

            // 随机生成销量
            for (String product : productList) {
                int sales = (int) (Math.random() * 20000);
                // 插入Redis的SortedSet中
                jedisClient.zadd(key, sales, product);
            }

            System.out.println(key);
            // 获取所有列表并按销量顺序输出
            Set<Tuple> sortedProductList = jedisClient.zrevrangeWithScores(key, 0, -1);
            for (Tuple product : sortedProductList) {
                System.out.println("产品ID： " + product.getElement() + ", 销量： " + Double.valueOf(product.getScore()).intValue());
            }
            System.out.println();
            System.out.println(key);
            System.out.println("前五大热销产品");
            // 获取销量前五列表并输出
            Set<Tuple> sortedTopList = jedisClient.zrevrangeWithScores(key, 0, 4);
            for (Tuple product : sortedTopList) {
                System.out.println("产品ID： " + product.getElement() + ", 销量： " + Double.valueOf(product.getScore()).intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisClient.quit();
            jedisClient.close();
        }
    }
}

/*
商品热销排行榜
产品ID： product-62605664-1d9d-40ac-bf78-09a18545e07d, 销量： 19131
产品ID： product-99fef7a9-bbde-4138-bcb0-c17bea01c26f, 销量： 18894
产品ID： product-afa2b961-b111-479a-9d5e-4f38940e9ca2, 销量： 18461
产品ID： product-014b7368-52d5-419c-b524-890a9ff93b4b, 销量： 17375
产品ID： product-43e894d9-8b86-493f-bd5e-5c011b0019f2, 销量： 16362
产品ID： product-76506b28-61f6-415e-80f0-cdf90218de97, 销量： 15924
产品ID： product-bd4c91c2-2bcc-4b4c-938a-6563b7d2e06d, 销量： 14824
产品ID： product-0cec9d29-17fb-438c-8a80-6720d2fc7df6, 销量： 14516
产品ID： product-155eb630-1450-49c1-bf1a-a40662a88bc1, 销量： 12652
产品ID： product-4ea801d8-4f06-4d33-9d9f-baeb2f6789e2, 销量： 10236
产品ID： product-858eb3ea-1444-4869-b58b-669e985268d8, 销量： 10142
产品ID： product-b42ee409-bf20-49f8-abd0-d963c1b37951, 销量： 9193
产品ID： product-9d7035fd-d45c-4e64-bc6e-7b765733328a, 销量： 9069
产品ID： product-2ed1427e-d2df-4630-99a2-ecefe4a98862, 销量： 8263
产品ID： product-ef62aea1-6fc9-45f7-aa26-743663e035ed, 销量： 7710
产品ID： product-3ae4643a-72e6-454b-8c1c-541a794dc5f1, 销量： 7440
产品ID： product-82678bea-ae80-4512-abfb-3fa1e29ed2e2, 销量： 6961
产品ID： product-31d2d183-f12f-49ab-83ea-1dcff7cac8fa, 销量： 6882
产品ID： product-7e1c096b-f5d8-4839-965c-b3d5db861434, 销量： 6586
产品ID： product-ccf37a1c-bca3-4742-9fd4-1cf81fd1d7b5, 销量： 6258
产品ID： product-4b403258-68a2-43c8-affc-e43c56e35716, 销量： 6007
产品ID： product-eeafb311-181e-4b43-a635-ea554c23f19a, 销量： 5425
产品ID： product-847c8350-05c5-40f7-bb14-acbffb46b95c, 销量： 4655
产品ID： product-926fd18c-d2da-407f-b5cf-3e76136e2d8f, 销量： 4477
产品ID： product-fc98e2d9-f608-420f-918e-06b73cff7f92, 销量： 3536
产品ID： product-4ebf602a-d3f1-40a3-8657-c428b35764f5, 销量： 2831
产品ID： product-6482a167-3820-41ff-b1d7-6f14801e4a6f, 销量： 2423
产品ID： product-997d0923-b001-49e4-bd5c-e9fcdbf95ba1, 销量： 2181
产品ID： product-754863e0-1f88-4238-9738-6e6da8334587, 销量： 2071
产品ID： product-5da91003-32ea-441f-bb45-f3f02dd3ef11, 销量： 412

商品热销排行榜
前五大热销产品
产品ID： product-62605664-1d9d-40ac-bf78-09a18545e07d, 销量： 19131
产品ID： product-99fef7a9-bbde-4138-bcb0-c17bea01c26f, 销量： 18894
产品ID： product-afa2b961-b111-479a-9d5e-4f38940e9ca2, 销量： 18461
产品ID： product-014b7368-52d5-419c-b524-890a9ff93b4b, 销量： 17375
产品ID： product-43e894d9-8b86-493f-bd5e-5c011b0019f2, 销量： 16362

 */