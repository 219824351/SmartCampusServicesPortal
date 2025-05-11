package SmartCampusServicesPortal.controller;

import SmartCampusServicesPortal.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

 @Controller
public class ViewController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

     // Role-specific dashboards
     @GetMapping("/student/dashboard")
     public String showStudentDashboard() {
         return "student-dashboard";
     }

     @GetMapping("/lecturer/dashboard")
     public String showLecturerDashboard() {
         return "lecturer-dashboard";
     }

     @GetMapping("/admin/dashboard")
     public String showAdminDashboard() {
         return "admin-dashboard";
     }

     @GetMapping("/dashboard")
     public String handleGenericDashboard() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null && authentication.getAuthorities() != null) {
             for (GrantedAuthority authority : authentication.getAuthorities()) {
                 if (authority.getAuthority().equals("STUDENT")) {
                     return "redirect:/student/dashboard";
                 } else if (authority.getAuthority().equals("LECTURER")) {
                     return "redirect:/lecturer/dashboard";
                 } else if (authority.getAuthority().equals("ADMIN")) {
                     return "redirect:/admin/dashboard";
                 }
             }
         }
         return "redirect:/login";
     }

 }
