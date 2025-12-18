package cn.bravedawn.collection.map.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : depers
 * @program : JavaTrain
 * @date : Created in 2022/9/28 20:42
 */
public class HashMapExample {

    /**
     * HashMap的基本操作
     */


    public static void main(String[] args) {
        // 声明一个HashMap实例
        Map<String, Product> productsByName = new HashMap<>();

        // 1.put方法
        Product eBike = new Product("E-Bike", "A bike with a battery");
        Product roadBike = new Product("Road bike", "A bike for competition");
        Product bike1 = productsByName.put(eBike.getName(), eBike);
        Product bike2 = productsByName.put(roadBike.getName(), roadBike);
        System.out.println(bike1); // null
        System.out.println(bike2); // null

        // 2.get方法
        Product nextPurchase = productsByName.get("E-Bike");
        System.out.println(nextPurchase); // Product{name='E-Bike', description='A bike with a battery', tags=null}

        // 如果我们尝试通过一个key从map里面获取一个不存在的value
        Product noExist = productsByName.get("Car");
        System.out.println(noExist); // null

        // 如果第二次插入一个相同key的一个值，put方法会返回之前已经存在的value值
        // 我们再次通过key获取到的这个value值为第二次插入的，也就是说第二次的插入会覆盖第一次的
        Product newEBike = new Product("E-Bike", "A bike with a better battery");
        Product newBike = productsByName.put(newEBike.getName(), newEBike);
        System.out.println(newBike); // Product{name='E-Bike', description='A bike with a battery', tags=null}
        System.out.println(productsByName.get("E-Bike")); // Product{name='E-Bike', description='A bike with a better battery', tags=null}

        // 3.HashMap允许我们使用null作为key
        Product defaultProduct = new Product("Chocolate", "At least buy chocolate");
        productsByName.put(null, defaultProduct);
        System.out.println(productsByName.get(null)); // Product{name='Chocolate', description='At least buy chocolate', tags=null}


        // 4.移除一个元素
        productsByName.remove("E-Bike");
        System.out.println(productsByName.get("E-Bike")); // null


        // 5.检查map中是否存在一个键或是值，值得注意的是：检查键是否存在的复杂度是O(1)，检查value是否存在的复杂度是O(n)，因为需要遍历map的所有元素
        // 5.1 检查一个key是否存在
        System.out.println(productsByName.containsKey("E-Bike"));  // false

        // 5.2 检查一个value是否存在
        System.out.println(productsByName.containsValue(roadBike)); // false


        // 创建一个 HashMap
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // 使用 replaceAll() 方法修改所有的值
        map.replaceAll((key, value) -> value + 10);

        // 打印修改后的 map
        System.out.println(map); // 输出：{a=11, b=12, c=13}
    }
}
