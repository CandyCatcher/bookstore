package src.cn.candy.bookstore.user.service;

import src.cn.candy.bookstore.user.dao.UserDao;
import src.cn.candy.bookstore.user.domain.User;

public class UserService {

    private UserDao userDao = new UserDao();

    /*
    注册功能
    校验注册信息
    对接数据库
     */
    public void regist(User form) throws UserException {
        //校验用户名,如果用户已存在，抛出异常
        User user = userDao.findByUsername(form.getUsername());
        if (user != null) {
            throw new UserException("用户名已注册");
        }
        //校验email，如果emai注册的用户已存在，抛出异常
        user = userDao.findByEmail(form.getEmail());
        if (user != null) {
            throw new UserException("email已注册");
        }
        //加入用户到数据库
        userDao.add(form);
    }

    public User login(User form) throws UserException {
        User user = userDao.findByUsername(form.getUsername());
        if (user == null) {
            throw new UserException("用户名未注册");
        }
        if (!form.getPassword().equals(user.getPassword())) {
            throw new UserException("密码错误");
        }
        if (!user.isState()) {
            throw new UserException("没有激活");
        }
        return user;
    }

    /*
    激活功能
    使用code查询数据库
    |——如果user不存在，说明激活码错误
    |——校验状态是否为未激活状态，如果已激活，抛出异常
     */
    public void active(String code) throws UserException {

        User user = userDao.findByCode(code);
        if (user == null) {
            throw new UserException("激活码无效");
        }
        if (user.isState()) {
            throw new UserException("已经激活过了");
        }

        userDao.updateState(user.getUid(), true);
    }
}
