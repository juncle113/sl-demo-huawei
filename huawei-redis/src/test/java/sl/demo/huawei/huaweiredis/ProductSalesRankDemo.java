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
        //实例连接地址，从控制台获取
        String host = "192.168.9.81";
        //Redis端口
        int port = 6379;
        Jedis jedisClient = new Jedis(host, port);
        try {
            //实例密码
            String authMsg = jedisClient.auth("hw@815GST");
            if (!authMsg.equals("OK")) {
                System.out.println("AUTH FAILED: " + authMsg);
            }
            //键分布式缓存服务最佳实践2 使用Redis实现排行榜功能2019-03-206

            String key = "商品热销排行榜";
            jedisClient.del(key);
            //随机生成产品数据
            List<String> productList = new ArrayList<>();
            for (int i = 0; i < PRODUCT_KINDS; i++) {
                productList.add("product-" + UUID.randomUUID().toString());
            }

            //随机生成销量
            for (int i = 0; i < productList.size(); i++) {
                int sales = (int) (Math.random() * 20000);

                String product = productList.get(i);
                //插入Redis的SortedSet中
                jedisClient.zadd(key, sales, product);
            }
            System.out.println();
            System.out.println("                   " + key);
            //获取所有列表并按销量顺序输出
            Set<Tuple> sortedProductList = jedisClient.zrevrangeWithScores(key, 0, -1);
            for (
                    Tuple product : sortedProductList) {
                System.out.println("产品ID： " + product.getElement() + ", 销量： " + Double.valueOf(product.getScore()).intValue());
            }
            System.out.println();
            System.out.println("                   " + key);
            System.out.println("                   前五大热销产品");
            //获取销量前五列表并输出
            Set<Tuple> sortedTopList = jedisClient.zrevrangeWithScores(key, 0, 4);
            for (
                    Tuple product : sortedTopList) {
                System.out.println("产品ID： " + product.getElement() + ", 销量： " + Double.valueOf(product.getScore()).intValue());
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            jedisClient.quit();
            jedisClient.close();
        }
    }
}
