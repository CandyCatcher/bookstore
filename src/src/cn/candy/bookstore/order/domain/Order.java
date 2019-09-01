package src.cn.candy.bookstore.order.domain;

import src.cn.candy.bookstore.user.domain.User;

import java.util.Date;
import java.util.List;

public class Order {
    //订单编号
    private String oid;
    //订单日期
    private Date ordertime;
    //订单总额
    private double total;
    //订单状态 未付款 已付款未发货 已付款已发货但未确认收货 确认收货
    private int state;
    //订单所有者------要使用的是User
    private User owner;
    //收货地址
    private String address;
    //当前订单下所有的条目
    private List<OrderItem> orderItemList;

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
