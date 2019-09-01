package src.cn.candy.bookstore.order.web.servlet;

import cn.candy.commonUtils.CommonUtil;
import cn.candy.servletUtils.BaseServlet;
import src.cn.candy.bookstore.cart.domain.Cart;
import src.cn.candy.bookstore.cart.domain.CartItem;
import src.cn.candy.bookstore.order.domain.Order;
import src.cn.candy.bookstore.order.domain.OrderItem;
import src.cn.candy.bookstore.order.service.ServiceOrder;
import src.cn.candy.bookstore.user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@WebServlet(name = "ServletOrder", urlPatterns = "/Order")
public class ServletOrder extends BaseServlet {

    ServiceOrder serviceOrder = new ServiceOrder();

    /**
     * 从session中得到购物车cart
     * 创建order OrderItem
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        /*
        创建order
         */
        Order order = new Order();
        order.setOid(CommonUtil.uuid());
        //获取下单时间——获取系统当前时间new Date()
        order.setOrdertime(new Date());
        order.setState(1);
        User user = (User) request.getSession().getAttribute("session_user");
        order.setOwner(user);
        //订单中的总额其实就是购物车中的总额
        order.setTotal(cart.getTotal());
        /*
        创建订单条目
        需要将每一个条目添加在list集合当中
         */
        List<OrderItem> orderItemList = new ArrayList<>();
        //购物车当中的每一个条目就是订单中的条目
        //但是购物车中没有将所有的条目添加在一个list集合中
        //遍历购物车中的条目添加在orderItemList中
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setIid(CommonUtil.uuid());
            orderItem.setCount(cartItem.getCount());
            orderItem.setBook(cartItem.getBook());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItem.setOrder(order);

            orderItemList.add(orderItem);
        }
        order.setOrderItemList(orderItemList);
        //每次提交订单，购物车都会清空
        cart.clear();
        serviceOrder.add(order);
        request.setAttribute("order", order);
        return "f:/jsps/order/desc.jsp";
    }

    /**
     * 我的订单
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        通过session得到uid
        String uid = request.getParameter("uid");
        */
        User user = (User) request.getSession().getAttribute("session_user");
        String uid = user.getUid();
        List<Order> orderList = serviceOrder.queryByUid(uid);
        request.setAttribute("orderList", orderList);
        return "f:/jsps/order/list.jsp";
    }

    /**
     * 确认收货
     * 点击确认收货后：订单的状态要发生改变
     * |——需要得到订单：通过oid查询返回订单
     * |——查看订单状态
     * |——只有订单状态为3时才能修改订单状态，否则抛出异常
     * 操作过程中可能会出现异常，需要处理
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String conOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String oid = request.getParameter("oid");
        try {
            serviceOrder.conOrder(oid);
            request.setAttribute("msg", "订单确认收货成功");
        } catch (Exception e) {
            request.setAttribute("msg", e.getMessage());
        }
        return "f:jsps/msg.jsp";
    }

    /**
     * 加载加载当前订单
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String loadOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String oid = request.getParameter("oid");
        Order order = serviceOrder.queryByOid(oid);
        request.setAttribute("order", order);
        return "f:/jsps/order/desc.jsp";
    }

    /**
     * 调用易宝
     * 重定向到易宝的网站(支付网关)
     * 并添加13+1个参数
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public void pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties properties = new Properties();
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
        properties.load(resourceAsStream);
        /*
        设置13个参数
         * @param p0_Cmd 业务类型
	 * @param p1_MerId 商户编号
	 * @param p2_Order 商户订单号
	 * @param p3_Amt 支付金额
	 * @param p4_Cur 交易币种
	 * @param p5_Pid 商品名称
	 * @param p6_Pcat 商品种类
	 * @param p7_Pdesc 商品描述
	 * @param p8_Url 商户接收支付成功数据的地址
	 * @param p9_SAF 送货地址
	 * @param pa_MP 商户扩展信息
	 * @param pd_FrpId 银行编码
	 * @param pr_NeedResponse 应答机制
	 * @param keyValue 商户密钥
         */
        String p0_Cmd = "Buy";
        String p1_MerId = properties.getProperty("p1_MerId");
        String p2_Order = request.getParameter("oid");
        String p3_Amt = "0.01";
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        String p8_Url = properties.getProperty("p8_Url");
        String p9_SAF = "";
        String pa_MP = "";
        String pd_FrpId = request.getParameter("pd_FrpId");
        String pr_NeedResponse = "1";
        String keyValue = properties.getProperty("keyValue");

         /*
         给易宝传递的是13+1个参数，hmac是通过13个参数得到的
          */
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

        StringBuilder url = new StringBuilder(properties.getProperty("url"));
        url.append("?p0_Cmd=").append(p0_Cmd);
        url.append("&p1_MerId=").append(p1_MerId);
        url.append("&p2_Order=").append(p2_Order);
        url.append("&p3_Amt=").append(p3_Amt);
        url.append("&p3_Amt=").append(p3_Amt);
        url.append("&p4_Cur=").append(p4_Cur);
        url.append("&p5_Pid=").append(p5_Pid);
        url.append("&p6_Pcat=").append(p6_Pcat);
        url.append("&p7_Pdesc=").append(p7_Pdesc);
        url.append("&p8_Url=").append(p8_Url);
        url.append("&p9_SAF=").append(p9_SAF);
        url.append("&pa_MP=").append(pa_MP);
        url.append("&pd_FrpId=").append(pd_FrpId);
        url.append("&pr_NeedResponse=").append(pr_NeedResponse);
        url.append("&hmac=").append(hmac);

         /*
         StringBuilder？？？？？
         需要转换为String
          */
        response.sendRedirect(url.toString());

    }

    /**
     * 易宝回调方法
     * 首先判断访问者是否是易宝，防止被他人截获
     * 然后获取订单状态，如果订单状态为2修改订单状态，否则不访问数据库
     * 判断当前回调方式，如果为点对点，需要反馈以success开头的字符串
     * 保存信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        获得到11+1个参数
         */
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");

        String hmac = request.getParameter("hmac");

        /*
        判断hmac是否正确
         */
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
        properties.load(inputStream);
        String keyValue = properties.getProperty("keyValue");

        boolean callback = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if (callback == false) {
            request.setAttribute("msg", "支付失败");
            return "f:/jsps/order/desc.jsp";
        }

        serviceOrder.pay(r6_Order);

        /*
        ?????
         */
        if (r9_BType.equals("2")) {
            response.getWriter().print("success");
        }

        request.setAttribute("msg", "支付成功！等待卖家发货！");
        return "f:/jsps/msg.jsp";
    }
}