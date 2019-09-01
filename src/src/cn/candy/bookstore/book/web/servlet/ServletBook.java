package src.cn.candy.bookstore.book.web.servlet;

import cn.candy.servletUtils.BaseServlet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.book.service.ServiceBook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 点击list上的分类名称，调用bookServlet的findAll方法，然后转发到body.jsp中
 */
@WebServlet(name = "ServletBook", urlPatterns = "/Book")
public class ServletBook extends BaseServlet {

    ServiceBook serviceBook = new ServiceBook();

    public void queryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Book> bookList = serviceBook.queryAll();
        response.getWriter().print(JSONArray.fromObject(bookList).toString());
    }

    public void queryByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cid = request.getParameter("cid");
        List<Book> bookList = serviceBook.queryByCategoty(cid);
        response.getWriter().print(JSONArray.fromObject(bookList).toString());
    }

    public void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String bid = request.getParameter("bid");
        Book book = serviceBook.queryByBid(bid);
        response.getWriter().print(JSONArray.fromObject(book).toString());
    }
}
