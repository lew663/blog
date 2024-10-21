package com.lew663.blog;

import com.lew663.blog.global.config.LayoutConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final LayoutConfig layoutConfig;

  @GetMapping("/")
  public String home(Model model) {
    layoutConfig.AddLayoutTo(model);
    return "index";
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }
}