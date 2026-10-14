package com.preppulse.controller;
import com.preppulse.model.StudySettings; import com.preppulse.service.StudySettingsService;
import org.springframework.stereotype.Controller; import org.springframework.web.bind.annotation.*; import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller public class SettingsController {
 private final StudySettingsService service; public SettingsController(StudySettingsService s){service=s;}
 @PostMapping("/settings") public String update(@ModelAttribute StudySettings settings,RedirectAttributes redirect){service.update(settings);redirect.addFlashAttribute("successMessage","Study preferences saved.");return "redirect:/#settings";}
}
