package com.preppulse.controller;
import com.preppulse.dto.FocusSessionRequest; import com.preppulse.model.FocusSession; import com.preppulse.service.FocusSessionService;
import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/focus-sessions") public class FocusSessionController {
 private final FocusSessionService service; public FocusSessionController(FocusSessionService s){service=s;}
 @PostMapping public FocusSession save(@Valid @RequestBody FocusSessionRequest request){return service.save(request);}
}
