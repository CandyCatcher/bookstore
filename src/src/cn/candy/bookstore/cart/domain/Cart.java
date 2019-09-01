package src.cn.candy.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车中存放的是一个一个的条目
 * 每一个条目包含商品名称和数量
 * 另外购物差还需要包含对这些条目进行操作的方法
 */
public class Cart {

    private Map<String, CartItem> map = new LinkedHashMap<>();

    /*
    添加条目到购物车中
    需要先判断车中是否有该条目(商品)
    如果有的话，将该条目的数量+1
    没有的话，创建该条目
     */
    public void add(CartItem cartItem) {
        if (map.containsKey(cartItem.getBook().getBid())) {
            //需要获取到老条目，因为是在老条目的数量上+1
            CartItem _cartItem = map.get(cartItem.getBook().getBid());
            _cartItem.setCount(_cartItem.getCount() +  cartItem.getCount());
            map.put(cartItem.getBook().getBid(), _cartItem);
        } else {
            map.put(cartItem.getBook().getBid(), cartItem);
        }
    }

    public void clear() {
        map.clear();
    }

    public void remove(String bid) {
        map.remove(bid);
    }

    /*
    合计=所有小计之和
     */
    public double getTotal() {
        BigDecimal total = new BigDecimal("0");
        //map进行遍历的方法
        for (CartItem cartItem : map.values()) {
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(cartItem.getSubtotal()));
            total = total.add(bigDecimal);
        }
        //转换为double
        return total.doubleValue();
    }

    public Collection<CartItem> getCartItems() {
        return map.values();
    }
}
