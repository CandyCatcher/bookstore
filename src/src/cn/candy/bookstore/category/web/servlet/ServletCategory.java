package src.cn.candy.bookstore.category.web.servlet;

import cn.candy.servletUtils.BaseServlet;
import net.sf.json.JSONArray;
import src.cn.candy.bookstore.category.domain.Category;
import src.cn.candy.bookstore.category.service.ServiceCategory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 查询所有分类
 * 在页面左侧动态显示所有分类名称
 * 通过servlet在数据库中查询所有分类的名称，并存到request当中
 */
@WebServlet(name = "ServletCategory", urlPatterns = "/Category")
public class ServletCategory extends BaseServlet {

    ServiceCategory serviceCategory = new ServiceCategory();

    public void listAll (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categoryList = serviceCategory.listAll();
        response.getWriter().print(JSONArray.fromObject(categoryList).toString());
    }

}
