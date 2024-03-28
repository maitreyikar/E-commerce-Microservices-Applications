package com.mymart.userservice.controller;

//import com.mymart.userservice.model.Product;
import com.mymart.userservice.model.User;
import com.mymart.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("error", "");
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user") User user, Model model) {

        List<User> userlist = userRepository.findByUsername(user.getUsername());

        if(userlist.size() > 0){
            model.addAttribute("user", new User());
            model.addAttribute("error", "Username already exists");
            return "signup";
        }

        userRepository.save(user);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("error", "");
        return "login";
    }

    @PostMapping("/login")
    public String getMethodName(@ModelAttribute User user, Model model, HttpServletRequest request) {
        
        List<User> userlist = userRepository.findByUsername(user.getUsername());

        if(userlist.size() == 1 && userlist.get(0).getPassword().equals(user.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", userlist.get(0));
            return "redirect:/user/products";
        }

        model.addAttribute("user", new User());
        model.addAttribute("error", "Invalid Username or Password");
        return "login";
    }
    

    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request, Model model) {
        
        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        
        if(currentuser == null){
            return "redirect:/user/login";
        }
        else{
            Optional<User> user = userRepository.findById(currentuser.getId());
            model.addAttribute("user", user.get());
            return "profile";
        }
    }

    @PostMapping("/profile")
    public String changePassword(@ModelAttribute User user, HttpServletRequest request) {
        String newPassword = user.getPassword();

        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        currentuser.setPassword(newPassword);
        request.getSession().setAttribute("loggedInUser", currentuser);

        User editedUser = userRepository.findByUsername(currentuser.getUsername()).get(0);
        editedUser.setPassword(newPassword);
        userRepository.save(editedUser);

        return "redirect:/user/products";
    }

    @GetMapping("/products")
    public String showProducts(){
        return "products";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/user/login";
    }

    @GetMapping("/delete")
    public String deleteUser(HttpServletRequest request, Model model) {

        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        userRepository.deleteById(currentuser.getId());

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        model.addAttribute("user", new User());
        model.addAttribute("error", "");
        return "signup";
    }

    
}
