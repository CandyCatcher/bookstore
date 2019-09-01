package src.cn.candy.bookstore.book.dao;

import cn.candy.commonUtils.CommonUtil;
import cn.candy.jdbcUtils.ReQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.category.domain.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DaoBook {

    QueryRunner queryRunner = new ReQueryRunner();

    public List<Book> queryAll() {
        String sql = "select * from book where del = false";
        try {
            return queryRunner.query(sql, new BeanListHandler<>(Book.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> queryByCategory(String cid) {

        String sql = "select * from book where cid = ?";
        try {
            return queryRunner.query(sql, new BeanListHandler<>(Book.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 需要将查询出来的结果存在map中
     * 然后将map映射出两个对象
     * 并不能使用reques的getParameterMap方法。。。。。是查询，不是输入
     * @param bid
     * @return
     */
    public Book load(String bid) {

        String sql = "select * from book where bid = ?";
        try {
            Map map = queryRunner.query(sql, new MapHandler(), bid);
            Category category = CommonUtil.toBean(map, Category.class);
            Book book = CommonUtil.toBean(map, Book.class);
            book.setCategory(category);
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * query得到的是对象吗？
     * @param cid
     * @return
     */
    public int getCountByCid(String cid) {

        String sql = "select count(*) from book where cid = ? where del = false";
        try {
            Number number = (Number) queryRunner.query(sql, new ScalarHandler(), cid);
            return number.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBook(Book book) {
        String sql = "insert into book values(?, ?, ?, ?, ?, ?)";
        Object[] params = {book.getBid(), book.getBname(), book.getPrice(), book.getAuthor(), book.getImage(), book.getCategory().getCid()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delBook(String bid) {
        String sql = "update book set del = true where bid = ?";
        try {
            queryRunner.update(sql, bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editBook(Book book) {
        String sql = "update book set bname = ?, price = ?, author = ?, image = ?, cid = ? where bid = ?";
        Object[] params = {book.getBname(), book.getPrice(), book.getAuthor(), book.getImage(), book.getCategory().getCid(), book.getBid()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book queryByBid(String bid) {
        String sql = "select * from book where bid = ? and del = ?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(Book.class), bid, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
