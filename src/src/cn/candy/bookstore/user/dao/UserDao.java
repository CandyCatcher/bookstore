package src.cn.candy.bookstore.user.dao;

import cn.candy.jdbcUtils.ReQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import src.cn.candy.bookstore.user.domain.User;

import java.sql.SQLException;

/*
这个页面爆出sql异常一般是打错了字母
 */
public class UserDao {
    private QueryRunner queryRunner = new ReQueryRunner();

    public User findByUsername(String username) {
        String sql = "select * from tb_user where username = ?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(User.class), username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByEmail(String email) {
        String sql = "select * from tb_user where email = ?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(User.class), email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(User user) {

        String sql = "insert into tb_user values(?, ?, ?, ?, ?, ?)";
        Object[] params = {user.getUid(), user.getUsername(), user.getPassword(), user.getEmail(), user.getCode(), user.isState()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByCode(String code) {

        String sql = "select * from tb_user where code = ?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(User.class), code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateState(String uid, boolean b) {
        String sql = "update tb_user set state = ? where uid = ?";
        Object[] params = {b, uid};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
