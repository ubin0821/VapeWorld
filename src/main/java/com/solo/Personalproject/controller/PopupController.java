package com.solo.Personalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PopupController {
    @GetMapping("/popup")
    public String pop() {
        return "event/popup";
    }
}
