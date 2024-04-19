package com.mymart.userservice.controller;

import com.mymart.userservice.model.Product;
import com.mymart.userservice.model.User;
import com.mymart.userservice.model.OrderDTO;
import com.mymart.userservice.model.Order;
import com.mymart.userservice.repository.UserRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;





@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${orderserviceHost}") 
    private String order_service_host;

    @Value("${productserviceHost}") 
    private String product_service_host;

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

            // addition
            Map<Long, Integer> cart = new HashMap<>();            
            session.setAttribute("cart", cart);
            // end of addition

            return "redirect:/user/home";
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

        return "redirect:/user/home";
    }

    @GetMapping("/home")
    public String showHome(HttpServletRequest request, Model model){
        
        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        if(currentuser == null){
            return "redirect:/user/login";
        }
        else{
            return "home";
        }
    }


    @GetMapping("/products")
    public String showProducts(HttpServletRequest request, Model model){
        
        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        if(currentuser == null){
            return "redirect:/user/login";
        }
        else{
            // String url = UriComponentsBuilder.fromHttpUrl("http://product-service:8080")
            //     .path("/products/list") // Endpoint in product-service controller
            //     .toUriString();

            String url = UriComponentsBuilder.fromHttpUrl("http://" + product_service_host + ":8080")
                .path("/products/list") // Endpoint in product-service controller
                .toUriString();

            @SuppressWarnings("unchecked")
            List<Product> productList = (List<Product>)restTemplate.getForObject(url, List.class);

            model.addAttribute("products", productList);

            return "products";
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity, HttpServletRequest request){

        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>)request.getSession().getAttribute("cart");

        if (cart.containsKey(productId)) {
            cart.put(productId, cart.get(productId) + quantity);
        } else {
            cart.put(productId, quantity);
        }       
        request.getSession().setAttribute("cart", cart);

        // for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
        //     System.out.println(entry.getKey() + ": " + entry.getValue());
        // }
        // System.out.println("");

        return "redirect:/user/products";
    }

    @GetMapping("/cart/view")
    public String viewCart(HttpServletRequest request, Model model) {
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>)request.getSession().getAttribute("cart");
        Map<Product, Integer> cartItems = new HashMap<>();
        double cartTotal = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            
            // String url = UriComponentsBuilder.fromHttpUrl("http://product-service:8080")
            //         .path("/products/fetch/" + String.valueOf(entry.getKey())) // Endpoint in product-service controller
            //         .toUriString();

            String url = UriComponentsBuilder.fromHttpUrl("http://" + product_service_host + ":8080")
                    .path("/products/fetch/" + String.valueOf(entry.getKey())) // Endpoint in product-service controller
                    .toUriString();
            
            Product product = (Product)restTemplate.getForObject(url, Product.class);
            cartItems.put(product, entry.getValue());
            cartTotal = cartTotal + entry.getValue() * product.getPrice();
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity, HttpServletRequest request, Model model) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>)request.getSession().getAttribute("cart");
        cart.put(productId, quantity);
        request.getSession().setAttribute("cart", cart);
        return "redirect:/user/cart/view";
    }
    
    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>)request.getSession().getAttribute("cart");
        cart.remove(productId);
        request.getSession().setAttribute("cart", cart);
        return "redirect:/user/cart/view";
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpServletRequest request, Model model) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>)request.getSession().getAttribute("cart");
        cart.clear();
        request.getSession().setAttribute("cart", cart);
        return "redirect:/user/cart/view";
    }

    @PostMapping("/cart/confirm")
    public String confirmOrder(@RequestParam("address") String address, HttpServletRequest request, Model model) {
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>)request.getSession().getAttribute("cart");

        OrderDTO cartDTO = new OrderDTO();
        cartDTO.setCartItems(cart);
        cartDTO.setAddress(address);
        cartDTO.setUser((User)request.getSession().getAttribute("loggedInUser"));

        //String orderServiceUrl = "http://order-service:8082/order/confirm"; // Assuming order service endpoint
        String orderServiceUrl = "http://" + order_service_host + ":8082/order/confirm";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDTO> orderRequest = new HttpEntity<>(cartDTO, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(orderServiceUrl, orderRequest, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("message", "Order confirmed successfully!");
        } else {
            model.addAttribute("message", "Failed to confirm order");
        }

        cart.clear();
        request.getSession().setAttribute("cart", cart);
        return "checkout";
    }

    @GetMapping("/orderhistory")
    public String viewOrderHistory(HttpServletRequest request, Model model) {
        Long userId = ((User)request.getSession().getAttribute("loggedInUser")).getId();
        // String url = UriComponentsBuilder.fromHttpUrl("http://order-service:8082")
        //         .path("/order/history/" + String.valueOf(userId))
        //         .toUriString();

        String url = UriComponentsBuilder.fromHttpUrl("http://" + order_service_host + ":8082")
                .path("/order/history/" + String.valueOf(userId))
                .toUriString();

        @SuppressWarnings("unchecked")
        List<Order> orders = (List<Order>)restTemplate.getForObject(url, List.class);
        model.addAttribute("orders", orders);
        return "order-history";
    }

    @GetMapping("/orderhistory/{orderId}")
    public String viewOrderHistory(Model model, @PathVariable Long orderId) {
        
        // String url = UriComponentsBuilder.fromHttpUrl("http://order-service:8082")
        //         .path("/order/history/" + String.valueOf(orderId) + "/items")
        //         .toUriString();

        String url = UriComponentsBuilder.fromHttpUrl("http://" + order_service_host + ":8082")
                .path("/order/history/" + String.valueOf(orderId) + "/items")
                .toUriString();

        @SuppressWarnings("unchecked")
        Map<Long, Integer> orderDetails = (Map<Long, Integer>)restTemplate.getForObject(url, Map.class);
        
        Map<Product, Integer> orderDetails2 = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : orderDetails.entrySet()) {
            
            // String url2 = UriComponentsBuilder.fromHttpUrl("http://product-service:8080")
            //         .path("/products/fetch/" + String.valueOf(entry.getKey())) // Endpoint in product-service controller
            //         .toUriString();

            String url2 = UriComponentsBuilder.fromHttpUrl("http://" + product_service_host + ":8080")
                    .path("/products/fetch/" + String.valueOf(entry.getKey())) // Endpoint in product-service controller
                    .toUriString();
            
            Product product = (Product)restTemplate.getForObject(url2, Product.class);

            orderDetails2.put(product, entry.getValue());
        }

        
        model.addAttribute("orderDetails", orderDetails2);
        return "order-details";
    }
    
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/user/login";
    }

    // @GetMapping("/delete")
    // public String deleteUser(HttpServletRequest request, Model model) {

    //     User currentuser = (User)request.getSession().getAttribute("loggedInUser");
    //     userRepository.deleteById(currentuser.getId());

    //     HttpSession session = request.getSession(false);
    //     if (session != null) {
    //         session.invalidate();
    //     }

    //     model.addAttribute("user", new User());
    //     model.addAttribute("error", "");
    //     return "signup";
    // }
    
    
}



// Map<String, Long> counts = list.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));