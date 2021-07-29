package onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.entity.Customer;
import onlineShop.service.CustomerService;

@Controller
public class RegistrationController {

    @Autowired //inject到内部
    private CustomerService customerService;

    @RequestMapping(value = "/customer/registration", method = RequestMethod.GET)
    public ModelAndView getRegistrationForm() {
        Customer customer = new Customer();
        return new ModelAndView("register", "customer", customer);//括号里的内容是起到一个validation的作用，确认前端的表单正确map成后端的customer类型object
    }

    @RequestMapping(value = "/customer/registration", method = RequestMethod.POST)
    public ModelAndView registerCustomer(@ModelAttribute Customer customer,
                                         BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {//如果绑定结果出错，直接返回原本的register view
            modelAndView.setViewName("register");
            return modelAndView;
        }
        //如果绑定正常，传过来的数据是一个customer类型
        customerService.addCustomer(customer);
        modelAndView.setViewName("login");
        modelAndView.addObject("registrationSuccess", "Registered Successfully. Login using username and password");
        return modelAndView;
    }
}

