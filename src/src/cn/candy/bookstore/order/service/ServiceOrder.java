package src.cn.candy.bookstore.order.service;

import cn.candy.jdbcUtils.JdbcUtil;
import src.cn.candy.bookstore.order.dao.DaoOrder;
import src.cn.candy.bookstore.order.domain.Order;

import java.sql.SQLException;
import java.util.List;

public class ServiceOrder {
    DaoOrder daoOrder = new DaoOrder();

    /**
     * 调用数据库的方法，往数据库中添加订单
     * 订单order的信息在一个表中
     * 订单order中的所有订单条目信息在另一个表中
     * 当在添加一个订单时，需要操作两个表
     * 因为订单条目隶属于订单order，两个类是相互关联的
     *
     * 因为在往数据库中添加两个表格时会存在异常情况而不能添加
     * 所以这两个动作应该是同一个事务
     * 需要添加事务
     * @param order
     */
    public void add(Order order) {
        try {
            //开启事务
            JdbcUtil.beginTransaction();
            daoOrder.addOrder(order);
            daoOrder.addOrderItemList(order.getOrderItemList());
            //提交事务
            JdbcUtil.commitTransaction();
        } catch (Exception e) {
            try {
                //回滚事务
                JdbcUtil.rollbackTransaction();
                throw new RuntimeException(e);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public List<Order> queryByUid(String uid) {
        List<Order> orderList = daoOrder.queryById(uid);
        return orderList;
    }

    /**
     * 业务的具体操作需要在service中完成！！！！
     * 确认订单
     * 得到订单目前状态
     * 修改订单的状态
     * ！！！不能使用order的setState！！！
     * @param oid
     */
    public void conOrder(String oid) {
        int state = daoOrder.queryOrderState(oid);
        if (state != 3) {
            throw new RuntimeException("订单状态不能确认收货");
        } else {
            daoOrder.updateOrderState(oid, state);
        }
    }

    public Order queryByOid(String oid) {

        return daoOrder.queryByOid(oid);
    }

    /**
     * 易宝返回后判断订单状态
     * @param r6_order
     */
    public void pay(String r6_order) {
        int state = daoOrder.queryOrderState(r6_order);
        if (state == 1) {
            daoOrder.updateOrderState(r6_order, 2);
        }
    }
}
