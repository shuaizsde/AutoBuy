package onlineShop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.entity.Authorities;
import onlineShop.entity.Customer;
import onlineShop.entity.User;

@Repository //本质上就是component，与数据库repository交互，起这个名字更直观
public class CustomerDao { //data access object

    @Autowired
    private SessionFactory sessionFactory;//hibernate提供的接口，用来连接数据库，通过spring创建出来的

    public void addCustomer(Customer customer) {
        //这里自己new了一个authorities，而不是用spring创建然后injection，是因为spring创建的不会被gc，当用户注册量特别大的时候，容易吃掉内存
        //自己new的话，会被存进database里，更好一点的设计
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");//普通用户ROLE_USER，管理员ROLE_ADMIN
        authorities.setEmailId(customer.getUser().getEmailId());
        Session session = null;//这里的session不同于项目1的（network相关），我们这里的session是database相关的，提供数据库的访问

        try {
            //session是hibernate提供的接口
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);//session帮助存进database里
            session.save(customer);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();//中间插入如果出现任何异常，回滚，避免出现dirty data
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //这里的String userName是个用户输入的email
    public Customer getCustomerByUserName(String userName) {
        User user = null;
        //try with resource写法会默认帮你session.close();
        try (Session session = sessionFactory.openSession()) {//因为get语句不需要回滚，所以可以定义在if里面

            Criteria criteria = session.createCriteria(User.class);//Criteria相当于一个搜索
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null)
            return user.getCustomer();
        return null;
    }
}
