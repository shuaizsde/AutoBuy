package onlineShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller //创建一个表单（key， handlerMapping）
public class HomePageController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String sayIndex() {
        return "index";
    }//返回的是个view name

    //这里不写method=RequestMethod.GET之类的，是说get，post...都可以操作
    @RequestMapping("/login")
    //login?error，"required = false"是说不写这部分也可以。
    //login？logout，"required = false"是说不写这部分也可以。
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView();//存着data + view name
        modelAndView.setViewName("login");

        if (error != null) {
            modelAndView.addObject("error", "Invalid username and Password");
        }

        if (logout != null) {
            modelAndView.addObject("logout", "You have logged out successfully");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
    public String sayAbout() {
        return "aboutUs";
    }
}
