package src.cn.candy.bookstore.cart.domain;

import src.cn.candy.bookstore.book.domain.Book;

import java.math.BigDecimal;

public class CartItem {
    private Book book;
    private int count;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /*
    小计
    二进制运算误差问题？
     */
    public double getSubtotal() {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(book.getPrice()));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(count));
        return decimal1.multiply(decimal2).doubleValue();
    }

}
