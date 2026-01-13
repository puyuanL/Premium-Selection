package test.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.ScanResult;
import java.util.*;

public class RedisClusterQuery {

    public static void main(String[] args) {
        // 配置Redis集群节点
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("172.18.91.89", 7000));
        nodes.add(new HostAndPort("172.18.91.89", 7001));
        nodes.add(new HostAndPort("172.18.91.89", 7002));
        nodes.add(new HostAndPort("172.18.91.89", 7003));
        nodes.add(new HostAndPort("172.18.91.89", 7004));
        nodes.add(new HostAndPort("172.18.91.89", 7005));

        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        poolConfig.setTestOnBorrow(true);

        JedisCluster jedisCluster = null;

        try {
            // 创建Redis集群连接
            jedisCluster = new JedisCluster(nodes);

            System.out.println("成功连接到Redis集群");
            System.out.println("========================================");

            // 获取所有master节点的所有key和value
            Map<String, String> allData = getAllKeysAndValuesFromCluster(nodes, poolConfig);

            // 输出结果
            System.out.println("总共找到 " + allData.size() + " 个key");
            System.out.println("========================================");

            if (allData.isEmpty()) {
                System.out.println("未找到任何key");
            } else {
                for (Map.Entry<String, String> entry : allData.entrySet()) {
                    System.out.println("Key: " + entry.getKey());
                    System.out.println("Value: " + entry.getValue());
                    System.out.println("----------------------------------------");
                }
            }

            // 使用jedisCluster进行其他操作(如果需要)
            // String value = jedisCluster.get("somekey");

        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (jedisCluster != null) {
                try {
                    jedisCluster.close();
                    System.out.println("已关闭Redis集群连接");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从Redis集群的所有节点获取key和value
     * 在集群模式下,需要分别连接每个节点进行扫描
     */
    private static Map<String, String> getAllKeysAndValuesFromCluster(
            Set<HostAndPort> nodes, JedisPoolConfig poolConfig) {

        Map<String, String> allData = new HashMap<>();
        Set<String> masterNodes = new HashSet<>();

        // 遍历所有节点,找出master节点并扫描
        for (HostAndPort node : nodes) {
            JedisPool pool = null;
            Jedis jedis = null;

            try {
                // 创建单独的连接池连接到该节点
                pool = new JedisPool(poolConfig, node.getHost(), node.getPort(), 3000);
                jedis = pool.getResource();

                // 获取节点信息
                String clusterInfo = jedis.clusterNodes();
                String nodeId = getNodeId(clusterInfo, node);

                // 检查是否为master节点,避免重复扫描(slave节点的数据与master相同)
                if (isMasterNode(clusterInfo, node) && !masterNodes.contains(nodeId)) {
                    masterNodes.add(nodeId);
                    System.out.println("正在扫描节点: " + node.getHost() + ":" + node.getPort() + " (Master)");

                    // 扫描该节点的所有key
                    Map<String, String> nodeData = scanNodeKeys(jedis);
                    allData.putAll(nodeData);

                    System.out.println("  -> 找到 " + nodeData.size() + " 个key");
                }

            } catch (Exception e) {
                System.err.println("扫描节点 " + node + " 失败: " + e.getMessage());
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
                if (pool != null) {
                    pool.close();
                }
            }
        }

        return allData;
    }

    /**
     * 扫描单个节点的所有key
     */
    private static Map<String, String> scanNodeKeys(Jedis jedis) {
        Map<String, String> data = new HashMap<>();
        String cursor = "0";

        try {
            do {
                // 使用SCAN命令遍历key,COUNT参数可以调整每次返回的数量
                ScanResult<String> scanResult = jedis.scan(cursor,
                        new redis.clients.jedis.params.ScanParams().count(100));

                List<String> keys = scanResult.getResult();
                cursor = scanResult.getCursor();

                // 获取每个key的值
                for (String key : keys) {
                    try {
                        String type = jedis.type(key);
                        String value = getValueByType(jedis, key, type);
                        data.put(key, value);
                    } catch (Exception e) {
                        System.err.println("  获取key [" + key + "] 失败: " + e.getMessage());
                    }
                }

            } while (!cursor.equals("0"));

        } catch (Exception e) {
            System.err.println("扫描失败: " + e.getMessage());
        }

        return data;
    }

    /**
     * 根据不同的数据类型获取value
     */
    private static String getValueByType(Jedis jedis, String key, String type) {
        try {
            switch (type.toLowerCase()) {
                case "string":
                    return jedis.get(key);

                case "list":
                    List<String> list = jedis.lrange(key, 0, -1);
                    return list.toString();

                case "set":
                    Set<String> set = jedis.smembers(key);
                    return set.toString();

                case "zset":
                    List<String> zset = jedis.zrange(key, 0, -1);
                    return zset.toString();

                case "hash":
                    Map<String, String> hash = jedis.hgetAll(key);
                    return hash.toString();

                default:
                    return "[未知类型: " + type + "]";
            }
        } catch (Exception e) {
            return "[获取值失败: " + e.getMessage() + "]";
        }
    }

    /**
     * 判断节点是否为master节点
     */
    private static boolean isMasterNode(String clusterInfo, HostAndPort node) {
        String[] lines = clusterInfo.split("\n");
        for (String line : lines) {
            if (line.contains("host.docker.internal:" + node.getPort()) &&
                    line.contains("master")) {
                System.out.println("Scan master node: " + line);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取节点ID
     */
    private static String getNodeId(String clusterInfo, HostAndPort node) {
        String[] lines = clusterInfo.split("\n");
        for (String line : lines) {
            if (line.contains(node.getHost() + ":" + node.getPort())) {
                String[] parts = line.split(" ");
                if (parts.length > 0) {
                    return parts[0];
                }
            }
        }
        return node.toString();
    }
}