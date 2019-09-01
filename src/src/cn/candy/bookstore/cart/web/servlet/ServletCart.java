package src.cn.candy.bookstore.cart.web.servlet;

import cn.candy.servletUtils.BaseServlet;
import net.sf.json.JSONArray;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.book.service.ServiceBook;
import src.cn.candy.bookstore.cart.domain.Cart;
import src.cn.candy.bookstore.cart.domain.CartItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "ServletCart", urlPatterns = "/Cart")
public class ServletCart extends BaseServlet {
    /**
     * 添加一个条目
     * 首先获得购物车
     * 得到条目的商品和数量
     * 设置条目的商品和数量
     * 添加到购物车中
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        String bid = request.getParameter("bid");
        //cartItem中的book是Book类型
        Book book = new ServiceBook().load(bid);

        //request中得到的count是String类型，需要转换为int
        int count = Integer.parseInt(request.getParameter("count"));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCount(count);
        cart.add(cartItem);

        response.getWriter().print(JSONArray.fromObject(cart).toString());
    }

    /**
     * 清空条目
     * 得到购物车
     * 清空购物车
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cart cart = (Cart) request.getSession().getAttribute("cart");
        cart.clear();
    }

    /**
     * 删除特定条目
     * 得到购物车
     * 得到要删除的商品bid
     * 调用方法
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cart cart = (Cart) request.getSession().getAttribute("cart");
        String bid = request.getParameter("bid");
        cart.remove(bid);
        return "/cart.html";
    }

    public void getCart(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {

        Cart cart = (Cart) request.getSession().getAttribute("cart");
        Collection<CartItem> cartItems = cart.getCartItems();
        response.getWriter().print(JSONArray.fromObject(cartItems).toString());
    }
}