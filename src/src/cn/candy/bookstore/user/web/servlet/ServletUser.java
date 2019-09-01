package src.cn.candy.bookstore.user.web.servlet;

import cn.candy.commonUtils.CommonUtil;
import cn.candy.mailUtils.Mail;
import cn.candy.mailUtils.MailUtils;
import cn.candy.servletUtils.BaseServlet;
import net.sf.json.JSONObject;
import src.cn.candy.bookstore.cart.domain.Cart;
import src.cn.candy.bookstore.user.domain.User;
import src.cn.candy.bookstore.user.service.UserException;
import src.cn.candy.bookstore.user.service.UserService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * servlet中的功能
 * 注册功能，激活功能
 */
@WebServlet(name = "ServletUser", urlPatterns = "/userServlet")
public class ServletUser extends BaseServlet {
    /*
    输出编码
     */
    private UserService userService = new UserService();

    public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        注册功能
        1.封装表单数据到form对象
        2.补全uid， code
        3.输入校验(不调用数据库)
            保存错误信息、form搭配request域，转发到regist.jsp
        4.调用service方法完成注册(调用数据库)
            保存错误信息、form到request域，转发到regist.jsp
        5.发邮件
        6.保存错误信息到msg.jsp
         */

        /*
        封装表单数据到form对象
         */
        User form = CommonUtil.toBean(request.getParameterMap(), User.class);
        /*
        补全uid，code
         */
        String uid = CommonUtil.uuid();
        String code = CommonUtil.uuid() + CommonUtil.uuid();
        form.setUid(uid);
        form.setCode(code);

        /*
        输入校验
        1.创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
        2.获取form中的username、password、email进行校验
        3.判断是否存在错误信息，如果存在的话，转发到jsps/user/regist.jsp
        4.如果没有错误的话，调用service中的方法，对接数据库
         */
        Map<String, String> errors = new HashMap<>();
        errors = verify(errors, form);
        errors = verifyEmail(errors, form);

        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
            request.setAttribute("form", form);
            return "f:/jsps/user/regist.jsp";
        }

        try {
            userService.regist(form);
        } catch (UserException e) {
            /*
            获取userService中的错误信息到request中
            保存form
            转发到jsps/user/regist.jsp
             */
            request.setAttribute("errors", e.getMessage());
            request.setAttribute("form", form);
            return "f:/jsps/user/regist.jsp";
        }

        /*
        发送激活邮件
        配置文件：
            服务器主机、用户名、密码、
            发件人、收件人、主题、邮件内容
        创建邮件对象
        发送邮件
         */
        //properties应该在src文件下
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
        String host = properties.getProperty("host");
        String usernamem = properties.getProperty("username");
        String passwordm = properties.getProperty("password");
        String subject = properties.getProperty("subject");
        String content = properties.getProperty("content");
        String from = properties.getProperty("from");
        String to = form.getEmail();
        content = MessageFormat.format(content, form.getCode());

        Session session = MailUtils.createSession(host, usernamem, passwordm);
        Mail mail = new Mail(from, to, subject, content);
        try {
            MailUtils.send(session, mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        /*
        保存成功信息
        转发到msg.jsp
         */
        request.setAttribute("msg", "注册成功，请马上到邮箱激活");
        return "f:/jsps/msg.jsp";
    }

    private Map verify(Map errors, User form) {

        String username = form.getUsername();
        String password = form.getPassword();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空！");
        }
//        else if (username.length() < 3 || username.length() > 10) {
//            errors.put("username", "用户名长度必须在3~10之间！");
//        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码不能为空！");
        }
//        else if (password.length() < 3 || password.length() > 10) {
//            errors.put("password", "密码长度必须在3~10之间！");
//        }

        return errors;
    }

    private Map verifyEmail(Map errors, User form) {

        String email = form.getEmail();

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "邮箱不能为空！");
        } else if (!email.matches("\\w+@\\w+\\.\\w+")) {
            errors.put("email", "邮箱格式错误！");
        }

        return errors;
    }

    /*
    激活功能：
    获取参数激活码
    调用service方法完成激活
    |——保存异常信息到request域，转发到msg.jsp
    保存成功信息到request域，转发到msg.jsp
    */
    public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        try {
            userService.active(code);
            request.setAttribute("msg", "激活成功");
        } catch (UserException e) {
            request.setAttribute("msg", e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }

    /*
    登录功能：
    在login.jsp输入用户名和密码
    获取用户名和密码
    首先格式校验
    然后进行数据库校验
    不存在的话抛出异常
    正常的话，转到index.jsp
    将用户存到session中
     */
        /*
    登录功能
    封装表单数据到form中
    调用servlet中的校验
    调用service中的方法，进行数据库的校验
    |——捕获异常，发送到login.jsp
    保存到用户信息到session中，然后重定向到index.jsp
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User form = CommonUtil.toBean(request.getParameterMap(), User.class);
        Map<String, String> errors = new HashMap<>();
        errors = verify(errors, form);

        if (errors.size() > 0) {
//            request.setAttribute("errors", errors);
//            request.setAttribute("form", form);
            response.getWriter().print(JSONObject.fromObject(errors));

        }
        try {
            User user = userService.login(form);
            request.getSession().setAttribute("session_user", user);
            //给User添加购物车cart
            //虽然设置的是对象，但是还是要转换
            request.getSession().setAttribute("cart", new Cart());
            response.getWriter().print("null");
        } catch (UserException e) {
//            request.setAttribute("msg", e.getMessage());
//            request.setAttribute("form", form);
            response.getWriter().print(e.getMessage());
        }
    }

    /*
    退出功能，清空session即可
     */
    public void quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect("/index.html");
    }

    public void isLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            response.getWriter().print("null");
        } else {
            response.getWriter().print(user.getUsername());
        }
    }
}
