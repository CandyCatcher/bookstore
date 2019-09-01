package src.cn.candy.bookstore.category.web.servlet.admin;

import cn.candy.commonUtils.CommonUtil;
import cn.candy.servletUtils.BaseServlet;
import src.cn.candy.bookstore.category.domain.Category;
import src.cn.candy.bookstore.category.service.CategoryException;
import src.cn.candy.bookstore.category.service.ServiceCategory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletAdminCategory", urlPatterns = "/AdminCategory")
public class ServletAdminCategory extends BaseServlet {
    ServiceCategory serviceCategory = new ServiceCategory();

    /**
     * 在后台查询所有分类
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String listAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categoryList = serviceCategory.listAll();
        request.setAttribute("categoryList", categoryList);
        return "f:/adminjsps/admin/category/list.jsp";
    }

    /**
     * 在后台添加分类
     * 点击添加分类-->addCategory方法-->cname、cid-->service
     * -->首先查询要添加的分类是否存在
     * -->调用dao update
     * <p>
     * 创建Category，将Category内的数据补充完整
     * 调用service添加Category
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public String addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category category = CommonUtil.toBean(request.getParameterMap(), Category.class);
        String cname = request.getParameter("cname");
        if (cname == null || cname.trim().isEmpty()) {
            request.setAttribute("msg", "请输入");
            return "f:/adminjsps/admin/category/add.jsp";
        }
        category.setCname(cname);
        String cid = CommonUtil.uuid();
        category.setCid(cid);
        try {
            serviceCategory.addCategory(category);
        } catch (CategoryException e) {
            request.setAttribute("msg", e.getMessage());
            return "f:/adminjsps/admin/category/add.jsp";
        }
        request.setAttribute("msg", "添加成功");
        return listAll(request, response);
    }

    /**
     * 删除指定的分类
     * 首先得到cid
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String deleteCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        if (cid == null || cid.trim().isEmpty()) {
            request.setAttribute("msg", "请输入");
            return "f:/adminjsps/admin/category/delete.jsp";
        }
        try {
            serviceCategory.deleteCategory(cid);
        } catch (CategoryException e) {
            request.setAttribute("msg", e.getMessage());
        }
        return listAll(request, response);
    }

    /**
     * 修改分类的名称
     * 在list页面中点击修改-->跳转到mod页面————————在这个过程中要得到需要修改的分类，并加载到mod页面————————在这个过程中需要将分类保存到request中
     * 首先需要得到需要修改的分类
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String modifyCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cname = request.getParameter("cname");
        System.out.println(cname);
        if (cname == null || cname.trim().isEmpty()) {
            request.setAttribute("msg", "请输入");
            return "f:/adminjsps/admin/category/mod.jsp";
        }

        Category category = CommonUtil.toBean(request.getParameterMap(), Category.class);
        serviceCategory.modifyCategory(category);
        request.setAttribute("msg", "修改成功");
        return listAll(request, response);
    }

    /**
     * 点击修改
     * 将cid传递过来
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String loadCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Category category = serviceCategory.loadCategory(cid);
        request.setAttribute("category", category);
        return "f:/adminjsps/admin/category/mod.jsp";
    }
}
