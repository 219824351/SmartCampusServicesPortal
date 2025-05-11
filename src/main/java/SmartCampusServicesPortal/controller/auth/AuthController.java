package SmartCampusServicesPortal.controller.auth;

import SmartCampusServicesPortal.model.AuthenticationResponse;
import SmartCampusServicesPortal.model.user.Role;
import SmartCampusServicesPortal.model.user.User;
import SmartCampusServicesPortal.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationService authService;


    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> registerApi(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerForm(@ModelAttribute User request, Model model) {

        request.setRole(Role.STUDENT);
        AuthenticationResponse response = authService.register(request);
        if (response.getToken() != null) {
            return "redirect:/login?registered";
        } else {
            model.addAttribute("error", response.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

}
