package src.cn.candy.bookstore.book.web.servlet.admin;

import cn.candy.commonUtils.CommonUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import src.cn.candy.bookstore.book.domain.Book;
import src.cn.candy.bookstore.book.service.ServiceBook;
import src.cn.candy.bookstore.category.domain.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ServletAdminAddBook", urlPatterns = "/AdminAddBook")
public class ServletAdminAddBook extends HttpServlet {

    ServiceBook serviceBook = new ServiceBook();

    /**
     * 添加图书：
     * 在添加图书时，需要上传文件——表单变化
     * 上传对request的限制——request.getParametere("xxx");这个方法在表单为enctype="multipart/form-data"时，它作废了。它永远都返回null
     * 需要使用工厂方法DiskFileItemFactory
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        /*
        编码
         */
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //创建工厂，设置缓存大小和临时目录
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(200 * 1024, new File("/home/candy/Pictures"));
        //创建解析器
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        servletFileUpload.setFileSizeMax(200 * 1024);
        //使用解析器解析request
        try {
            //当上传的文件大于设定值时，会抛出异常
            List<FileItem> fileItemList = servletFileUpload.parseRequest(request);

            /*
            将fileItemList中的数据封装到Book对象当中
            首先将所有的普通表单字段数据封装到Map中
            再通过JavaBean将数据封装到Book中
            方法：
            boolean isFormField()：是否为普通表单项！返回true为普通表单项，如果为false即文件表单项！
            String getFieldName()：返回当前表单项的名称；
            String getString(String charset)：返回表单项的值；
             */
            Map<String, String> map = new HashMap<>();
            for (FileItem fileItem : fileItemList) {
                if (fileItem.isFormField()) {
                    map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
                }
            }
            Book book = CommonUtil.toBean(map, Book.class);
            /*
            为book对象补齐剩余数据
             */
            book.setBid(CommonUtil.uuid());
            /*
            需要将map中的cid赋值给Category，然后赋给book
             */
            Category category = CommonUtil.toBean(map, Category.class);
            book.setCategory(category);

            /*
            将上传的文件封装到book对象中
            |——首先需要将文件保存到指定存放图书封面的位置
            |——设置Book的图书封面
             */

            /*
            保存图书封面
            获得保存到的目录、设置保存到的文件名称
            使用目录和文件名称创建目标文件对象——使用IO流保存文件！！
            校验文件扩展名,文件扩展名应该为.jpg
            保存上传的文件到目标文件地址
             */
            String savepath = this.getServletContext().getRealPath("/book_img");
            String filename = CommonUtil.uuid() + "_" + fileItemList.get(1).getName();
            System.out.println(filename);
            if (!filename.toLowerCase().endsWith("jpg")) {
                request.setAttribute("msg", "上传文件格式错误");
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
                return;
            }
            //使用目录和文件名称得到目标文件，目标文件不存在
            File file = new File(savepath, filename);
            //保存上传文件到目标文件位置
            fileItemList.get(1).write(file);

            /*
            校验图片的尺寸
             */
            Image image = new ImageIcon(file.getAbsolutePath()).getImage();
            if(image.getWidth(null) > 200 || image.getHeight(null) > 200) {
                file.delete();
                request.setAttribute("msg", "上传的图片尺寸超出了200 * 200");
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
                return;
            }
            /*
            设置book对象的image——将图片的路径位置赋给Book
            这个路径就是url了
             */
            book.setImage("book_img/" + filename);

            /*
            使用service实现添加book
             */
            serviceBook.addBook(book);
            request.setAttribute("msg", "添加成功");
            request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
        } catch (Exception e) {
            if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
                request.setAttribute("msg", "上传的文件超过了15KB");
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
