package onlineShop.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.entity.Cart;
import onlineShop.entity.CartItem;

@Repository
public class CartItemDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void addCartItem(CartItem cartItem) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(cartItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();//如果try里面的步骤抛出异常，回退到之前没出错的那一步
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void removeCartItem(int cartItemId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            //<-----------这部分是删除cache里面的数据,不删除的话，cache里的数据会自动把数据库里再加回来
            //这部分是cache数据和db的自动匹配的内部实现？？
            CartItem cartItem = session.get(CartItem.class, cartItemId);
            Cart cart = cartItem.getCart();
            List<CartItem> cartItems = cart.getCartItem();
            cartItems.remove(cartItem);
            //这部分---------------->
            session.beginTransaction();
            session.delete(cartItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void removeAllCartItems(Cart cart) {
        List<CartItem> cartItems = cart.getCartItem();
        for (CartItem cartItem : cartItems) {
            removeCartItem(cartItem.getId());
        }
    }
}
