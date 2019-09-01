package src.cn.candy.bookstore.book.web.servlet.admin;

import cn.candy.commonUtils.CommonUtil;
import cn.candy.servletUtils.BaseServlet;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.book.service.ServiceBook;
import src.cn.candy.bookstore.category.domain.Category;
import src.cn.candy.bookstore.category.service.ServiceCategory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletAdminBook", urlPatterns = "/AdminBook")
public class ServletAdminBook extends BaseServlet {
    ServiceBook serviceBook = new ServiceBook();
    ServiceCategory serviceCategory = new ServiceCategory();

    public String listAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> bookList = serviceBook.queryAll();
        request.setAttribute("bookList", bookList);
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     * 加载图书
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid = request.getParameter("bid");
        Book book = serviceBook.load(bid);
        List<Category> categoryList = serviceCategory.listAll();
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("book", book);
        return "f:/adminjsps/admin/book/desc.jsp";
    }

    /**
     * 得到Category分类
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String preAddBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categoryList = serviceCategory.listAll();
        request.setAttribute("categoryList", categoryList);
        return "f:/adminjsps/admin/book/add.jsp";
    }

    public String delBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid = request.getParameter("bid");
        serviceBook.delBook(bid);
        request.setAttribute("msg", "删除成功");
        return "f:/adminjsps/admin/book/del.jsp";
    }

    /**
     * 如果调用的方法有返回值，那么本方法也要有返回值？？
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String editBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = CommonUtil.toBean(request.getParameterMap(), Book.class);
        Category category = CommonUtil.toBean(request.getParameterMap(), Category.class);
        book.setCategory(category);
        serviceBook.editBook(book);
        request.setAttribute("msg", "修改成功");
        return listAll(request, response);
    }
}
