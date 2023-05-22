package com.simplesocialnetwork.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.simplesocialnetwork.entity.Message;
import com.simplesocialnetwork.entity.User;
import com.simplesocialnetwork.entity.Views;
import com.simplesocialnetwork.repo.MessageRepo;
import com.simplesocialnetwork.repo.UserDetailsRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ivan Gordeev 20.05.2023
 */

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepo messageRepo;
    private final UserDetailsRepo userDetailsRepo;

    public MessageController(MessageRepo messageRepo, UserDetailsRepo userDetailsRepo) {
        this.messageRepo = messageRepo;
        this.userDetailsRepo = userDetailsRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list(Authentication authentication) {
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

        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @PutMapping("{id}")
    public Message update(@RequestBody Message message,
                          @PathVariable("id") Message messageFormDb) {
        BeanUtils.copyProperties(message, messageFormDb, "id");
        return messageRepo.save(messageFormDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
    }
}

