package src.cn.candy.bookstore.category.service;

import src.cn.candy.bookstore.book.dao.DaoBook;
import src.cn.candy.bookstore.category.dao.DaoCategory;
import src.cn.candy.bookstore.category.domain.Category;

import java.util.List;

public class ServiceCategory {

    DaoCategory daoCategory = new DaoCategory();
    DaoBook daoBook = new DaoBook();

    /**
     * 查询所有的分类，都是要将检查出来的结果存储到List中
     * @return
     */
    public List<Category> listAll() {

        return daoCategory.listAll();
    }

    /**
     * 添加分类
     * 查询是否存在，存在则抛出异常
     * @param category
     */
    public void addCategory(Category category) throws CategoryException {
        String cname = category.getCname();

        Category result = daoCategory.queryByCname(cname);
        if (result == null) {
            daoCategory.addCategory(category);
        } else {
            throw new CategoryException("该类已存在");
        }
    }

    /**
     * 需要判断输入的分类是否存在
     * 并且需要判断，分类下是否有图书
     * @param cid
     */
    public void deleteCategory(String cid) throws CategoryException {

        Category result = daoCategory.queryById(cid);
        if (result.equals(null)) {
            throw new CategoryException("该类不存在");
        }

        int count = daoBook.getCountByCid(cid);
        if (count > 0) {
            throw new CategoryException("该分类下还存有参数");
        }

        daoCategory.deleteCategory(cid);
    }

    public void modifyCategory(Category category) {
        daoCategory.modifyCategory(category);
    }

    public Category loadCategory(String cid) {
        return daoCategory.queryById(cid);
    }
}
