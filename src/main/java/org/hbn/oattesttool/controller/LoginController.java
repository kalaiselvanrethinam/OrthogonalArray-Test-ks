package org.hbn.oattesttool.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> payload) {
        return Map.of(
                "message", "Login simulated",
                "browser", payload.get("browser"),
                "os", payload.get("os"),
                "region", payload.get("region")
        );
    }
}
