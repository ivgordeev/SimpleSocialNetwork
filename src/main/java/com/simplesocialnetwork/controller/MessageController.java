package com.simplesocialnetwork.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.simplesocialnetwork.entity.Message;
import com.simplesocialnetwork.entity.Views;
import com.simplesocialnetwork.repo.MessageRepo;
import org.springframework.beans.BeanUtils;
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

    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
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

