package cn.bravedawn.shardingjdbcdemo;

import cn.bravedawn.shardingjdbcdemo.dao.AreaMapper;
import cn.bravedawn.shardingjdbcdemo.dao.OrderItemMapper;
import cn.bravedawn.shardingjdbcdemo.dao.OrdersMapper;
import cn.bravedawn.shardingjdbcdemo.model.*;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class ShardingJdbcDemoApplicationTests {

	@Autowired
	private OrdersMapper ordersMapper;

	@Autowired
    private AreaMapper areaMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void insert(){
		Orders orders = new Orders();

		orders.setOrderId(19L);
		orders.setUserId(2);
		orders.setOrderAmount(BigDecimal.TEN);
		orders.setOrderStatus(0);

		// 插入目标数据库：192.168.156.139，表：orders_2
		ordersMapper.insert(orders);
	}

	@Test
    public void testGlobal() {
        Area area = new Area();
        area.setId(4);
        area.setName("兰州");
        areaMapper.insert(area);
    }

    @Test
	public void testSelectGlobal() {
		AreaExample areaExample = new AreaExample();
		areaExample.createCriteria().andIdEqualTo(4);
		List<Area> areaList = areaMapper.selectByExample(areaExample);
		System.out.println("area.size=" + areaList.size());
	}


	@Test
	public void testOrderItem() {
		OrderItem orderItem = new OrderItem();
		orderItem.setId(4);
		orderItem.setOrderId(19);
		orderItem.setProductName("MacBook Air 笔记本");
		orderItem.setNum(1);
//		orderItem.setUserId(2);
		orderItemMapper.insertSelective(orderItem);
	}

	@Test
    public void testSelectOrderItem() {
		List<OrderItem> orderItems = orderItemMapper.selectByOrderIdAndUserId("19");
		orderItems.stream().forEach(o -> System.out.println(o));
    }

    @Test
    public void testMsOrder() {
		OrdersExample example = new OrdersExample();
		example.createCriteria().andUserIdEqualTo(2).andOrderIdEqualTo(19L);
		List<Orders> ordersList = ordersMapper.selectByExample(example);

		for (int i = 0; i < 10; i++) {
			ordersList.forEach(o -> {
				System.out.println("orderId=" + o.getOrderId());
				System.out.println("userId=" + o.getUserId());
				System.out.println("orderAmount=" + o.getOrderAmount());
			});
		}
	}


	@Test
	public void insertByCustomOrderIdInsert(){
		Orders orders = new Orders();
		orders.setUserId(2);
		orders.setOrderAmount(BigDecimal.TEN);
		orders.setOrderStatus(0);

		ordersMapper.insertSelective(orders);
	}


	@Test
	@Transactional
	public void testDistributedTransaction(){
		Orders orders = new Orders();
		orders.setUserId(1);
		orders.setOrderAmount(BigDecimal.TEN);
		orders.setOrderStatus(0);
		ordersMapper.insertSelective(orders);


		Orders orders2 = new Orders();
		orders2.setUserId(2);
		orders2.setOrderAmount(BigDecimal.TEN);
		orders2.setOrderStatus(0);
		ordersMapper.insertSelective(orders2);
	}

	@Test
	@Transactional
	@ShardingTransactionType(TransactionType.XA)
	public void testDistributedTransactionThrow(){
		Orders orders = new Orders();
		orders.setUserId(1);
		orders.setOrderAmount(BigDecimal.TEN);
		orders.setOrderStatus(0);
		ordersMapper.insertSelective(orders);

		int i = 1/0;

		Orders orders2 = new Orders();
		orders2.setUserId(2);
		orders2.setOrderAmount(BigDecimal.TEN);
		orders2.setOrderStatus(0);
		ordersMapper.insertSelective(orders2);

	}


}
