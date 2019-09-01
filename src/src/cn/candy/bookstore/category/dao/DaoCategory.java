package src.cn.candy.bookstore.category.dao;

import cn.candy.jdbcUtils.ReQueryRunner;
import com.sun.nio.sctp.HandlerResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import src.cn.candy.bookstore.category.domain.Category;

import java.sql.SQLException;
import java.util.List;

public class DaoCategory {

    QueryRunner queryRunner = new ReQueryRunner();

    public List<Category> listAll() {
        String sql = "select * from category";
        try {
            return queryRunner.query(sql, new BeanListHandler<>(Category.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category queryByCname(String cname) {
        String sql = "select * from category where cname = ?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(Category.class), cname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在数据库中添加分类
     * @param category
     */
    public void addCategory(Category category) {
        String cname = category.getCname();
        String cid = category.getCid();
        String sql = "insert into category values(?, ?)";
        Object[] params = {cid, cname};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在数据库中删除指定的分类
     * @param cid
     */
    public void deleteCategory(String cid) {
        String sql = "delete from category where cid = ?";
        try {
            queryRunner.update(sql, cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category queryById(String cid) {
        String sql = "select * from category where cid = ?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(Category.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void modifyCategory(Category category) {
        String cid = category.getCid();
        String cname = category.getCname();
        String sql = "update category set cname = ? where cid = ?";
        Object[] params = {cname, cid};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
