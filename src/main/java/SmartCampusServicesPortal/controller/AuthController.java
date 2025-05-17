package SmartCampusServicesPortal.controller;

import SmartCampusServicesPortal.model.AuthenticationResponse;
import SmartCampusServicesPortal.model.enums.Role;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationService authService;


    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    // Form submission endpoint
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerForm(@ModelAttribute User request, Model model, HttpServletRequest servletRequest) {
        request.setRole(Role.STUDENT);

        try {

            if (request.getFirstName() == null || request.getFirstName().isEmpty() ||
                    request.getLastName() == null || request.getLastName().isEmpty() ||
                    request.getUsername() == null || request.getUsername().isEmpty() ||
                    request.getEmail() == null || request.getEmail().isEmpty() ||
                    request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
                model.addAttribute("error", "All fields are required");
                model.addAttribute("user", request);
                return "register";
            }

            AuthenticationResponse response = authService.register(request);

            if (response.getToken() != null) {
                // Store success flag in session
                servletRequest.getSession().setAttribute("registrationSuccess", true);
                return "redirect:/register";
            } else {
                throw new RuntimeException(response.getMessage() != null ?
                        response.getMessage() : "Registration failed");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", request);
            return "register";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpServletRequest request){
        // Clear any previous error messages
        model.addAttribute("error", null);

        // Check for success flag in session
        Boolean success = (Boolean) request.getSession().getAttribute("registrationSuccess");
        if (Boolean.TRUE.equals(success)) {
            request.getSession().removeAttribute("registrationSuccess");
            model.addAttribute("registrationSuccess", true);
        }
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

}