package src.cn.candy.bookstore.order.domain;

import src.cn.candy.bookstore.book.domain.Book;

public class OrderItem {

    //条目的id
    private String iid;
    //每一个商品的数目
    private int count;
    //每一个条目小计
    private double subtotal;
    //订单的id----关联所属订单
    private Order order;
    //条目中商品的id----关联商品
    private Book book;

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
