package src.cn.candy.bookstore.order.dao;

import cn.candy.commonUtils.CommonUtil;
import cn.candy.jdbcUtils.ReQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.order.domain.Order;
import src.cn.candy.bookstore.order.domain.OrderItem;

import javax.mail.Service;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DaoOrder {

    private QueryRunner queryRunner = new ReQueryRunner();

    /**
     * 在数据库中添加订单
     * |——添加order
     * |——添加订单条目
     * @param order
     */
    public void addOrder(Order order) {
        try {
            //需要将Date转换为SQL.Date
            Timestamp orderTime = new Timestamp(order.getOrdertime().getTime());

            String sql = "insert into orders values(?, ?, ?, ?, ?, ?)";
            Object[] params = {order.getOid(), orderTime, order.getTotal(), order.getState(), order.getOwner().getUid(), order.getAddress()};
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加订单条目---是一个数组
     * 由于订单条目可能很多，使用批处理
     * 要使用批处理，参数必须是二维数组
     * QueryRunner类的batch(String sql, Object[][] params)
     * 其中params是多个一维数组
     * 需要将orderItemList转换为二维数组
     * @param orderItemList
     */
    public void addOrderItemList(List<OrderItem> orderItemList) {

        try {
            String sql = "insert into orderitem values(?, ?, ?, ?, ?)";
            Object[][] params = new Object[orderItemList.size()][];
            for (int i = 0; i < orderItemList.size(); i++) {
                /*
                需要将orderItemList中的每一个对象添加到params中
                因为是list，所以需要使用get方法获取每一个对象
                往二维数组中添加的是一维数组，不是对象，所以需要将对象转换为一维数组
                */
                OrderItem orderItem = orderItemList.get(i);
                params[i] = new Object[]{orderItem.getIid(), orderItem.getCount(), orderItem.getSubtotal(), orderItem.getOrder().getOid(), orderItem.getBook().getBid()};
            }
            queryRunner.batch(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按照uid查询所有的订单
     * |——查询表order得到所有的order-----通过数据库查询出来的是表格，不是对象！！
     *    通过遍历每一个order得到其所有的orderItemList
     * 创建getOrderItemList方法，得到orderItemList
     * |——每一个orderItem中的内容从orderitem、book中查询
     *    (item表中的内容是iid、oid、bid、subtotal，但是订单条目中图书的信息是详细的)
     *    查询出来的条目中的信息和orderItem的成员变量不是一一对应的，所以需要进一步的处理
     * |——获得orderItem
     *    将每一条map分成两个对象
     * @param uid
     * @return
     */
    public List<Order> queryById(String uid) {

        String sql = "select * from orders where uid = ?";
        try {
            List<Order> orderList = queryRunner.query(sql, new BeanListHandler<>(Order.class), uid);
            for (Order order : orderList) {
                loadOrderItemList(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 直接在这个方法里面为order对象添加orderItemList
     * @param order
     */
    private void loadOrderItemList(Order order) {

        String sql = "select * from orderitem i, book b where i.bid = b.bid and oid = ?";
        try {
            //因为得到map的集合，所以不需要，因为也不是bean，不需要给出类名
            List<Map<String, Object>> mapList = queryRunner.query(sql, new MapListHandler(), order.getOid());
            //遍历每一个map，将每一个map转换为orderItem
            List<OrderItem> orderItemList = toOrderItemList(mapList);
            order.setOrderItemList(orderItemList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将每一个map转换为orderItem
     * 并返回orderItemList
     * @param mapList
     * @return
     */
    private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
        List<OrderItem> orderItemList = new ArrayList<>();
       for (Map<String, Object> map : mapList) {
           OrderItem orderItem = toOrderItem(map);
           orderItemList.add(orderItem);
       }
       return orderItemList;
    }

    private OrderItem toOrderItem(Map<String, Object> map) {

        OrderItem orderItem = CommonUtil.toBean(map, OrderItem.class);
        Book book = CommonUtil.toBean(map, Book.class);
        orderItem.setBook(book);
        return orderItem;
    }

    /**
     * 加载当前订单
     * @param oid
     * @return
     */
    public Order queryByOid(String oid) {

        String sql = "select * from orders where oid = ?";
        try {
            Order order = queryRunner.query(sql, new BeanHandler<>(Order.class), oid);
            loadOrderItemList(order);
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得订单的状态
     * 不需要获得Order对象，因为order表中有state条目
     * 回顾结果集？？？
     * @param oid
     * @return
     */
    public int queryOrderState(String oid) {

        String sql = "select state from orders where oid = ?";
        try {
            return (int) queryRunner.query(sql, new ScalarHandler(), oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改订单状态
     * 得到的是订单表
     * @param oid
     * @param state
     */
    public void updateOrderState(String oid, int state) {
        String sql = "update orders set state = ? where oid = ?";
        Object[] params = {state, oid};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
