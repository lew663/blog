package com.lew663.blog;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public ResponseEntity<String> home() {
    return ResponseEntity.ok("Home page data");
  }
}