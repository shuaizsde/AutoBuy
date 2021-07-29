package onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.entity.Cart;
import onlineShop.entity.Customer;
import onlineShop.service.CartService;
import onlineShop.service.CustomerService;

@Controller
public class CartController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/cart/getCartById", method = RequestMethod.GET)
    public ModelAndView getCartId(){
        ModelAndView modelAndView = new ModelAndView("cart");
        //SecurityContextHolder存储登陆过的用户信息（在in-memory保存起来的）
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        //username是用户登录时的email address
        String username = loggedInUser.getName();
        //通过userName（email address）来从customerService调取customer obj
        Customer customer = customerService.getCustomerByUserName(username);
        modelAndView.addObject("cartId", customer.getCart().getId());
        return modelAndView;
    }

    @RequestMapping(value = "/cart/getCart/{cartId}", method = RequestMethod.GET)
    @ResponseBody
    public Cart getCartItems(@PathVariable(value="cartId")int cartId){
        return cartService.getCartById(cartId);
    }
}
