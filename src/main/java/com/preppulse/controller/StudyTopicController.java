package com.preppulse.controller;
import com.preppulse.model.StudyTopic; import com.preppulse.service.RevisionService; import jakarta.validation.Valid;
import org.springframework.stereotype.Controller; import org.springframework.validation.BindingResult; import org.springframework.web.bind.annotation.*; import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller public class StudyTopicController {
 private final RevisionService service; public StudyTopicController(RevisionService s){service=s;}
 @PostMapping("/topics") public String add(@Valid @ModelAttribute("newTopic") StudyTopic topic,BindingResult result,RedirectAttributes redirect){if(result.hasErrors()){redirect.addFlashAttribute("errorMessage","Please enter a topic name and subject.");return "redirect:/#revision";}service.add(topic);redirect.addFlashAttribute("successMessage","Revision topic added. Your first review is scheduled for tomorrow.");return "redirect:/#revision";}
 @PostMapping("/topics/{id}/reviewed") public String reviewed(@PathVariable Long id,RedirectAttributes redirect){StudyTopic t=service.markReviewed(id);redirect.addFlashAttribute("successMessage","Nice work! Next review for "+t.getTitle()+" is "+t.getNextReviewDate()+".");return "redirect:/#revision";}
 @PostMapping("/topics/{id}/archive") public String archive(@PathVariable Long id,RedirectAttributes redirect){service.archive(id);redirect.addFlashAttribute("successMessage","Topic archived from your revision list.");return "redirect:/#revision";}
}
