package src.cn.candy.bookstore.book.service;

import src.cn.candy.bookstore.book.dao.DaoBook;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.order.web.servlet.PaymentUtil;

import java.util.List;

public class ServiceBook {

    DaoBook daoBook = new DaoBook();

    public List<Book> queryAll() {
        return daoBook.queryAll();
    }

    public List<Book> queryByCategoty(String cid) {
        return daoBook.queryByCategory(cid);
    }

    public Book queryByBid(String bid) {
        return daoBook.queryByBid(bid);
    }

    public Book load(String bid) {
        return daoBook.load(bid);
    }

    public void addBook(Book book) {
        daoBook.addBook(book);
    }

    public void delBook(String bid) {
        daoBook.delBook(bid);
    }

    public void editBook(Book book) {
        daoBook.editBook(book);
    }
}
