package com.simplesocialnetwork.controller;

import com.simplesocialnetwork.entity.User;
import com.simplesocialnetwork.repo.MessageRepo;
import com.simplesocialnetwork.repo.UserDetailsRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author Ivan Gordeev 22.05.2023
 */

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageRepo messageRepo;
    private final UserDetailsRepo userDetailsRepo;

    public MainController(MessageRepo messageRepo, UserDetailsRepo userDetailsRepo) {
        this.messageRepo = messageRepo;
        this.userDetailsRepo = userDetailsRepo;
    }

    @GetMapping
    public String main(Model model, Authentication authentication) {
        HashMap<Object, Object> data = new HashMap<>();
        User user = null;
        if (authentication != null) {
            user = getUser(authentication);
        }
        data.put("profile", user);
        data.put("messages", messageRepo.findAll());

        model.addAttribute("frontendData", data);
        return "index";
    }

    private User getUser(Authentication authentication) {
        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        String id = principal.getAttribute("sub");
        User user = userDetailsRepo.findById(id).orElseGet(() -> {
            User newUser = new User();

            newUser.setId(id);
            newUser.setName(principal.getAttribute("name"));
            newUser.setEmail(principal.getAttribute("email"));
            newUser.setGender(principal.getAttribute("gender"));
            newUser.setLocale(principal.getAttribute("locale"));
            newUser.setUserpic(principal.getAttribute("picture"));
            return newUser;
        });
        user.setLastVisit(LocalDateTime.now());
        userDetailsRepo.save(user);
        return user;
    }
}
