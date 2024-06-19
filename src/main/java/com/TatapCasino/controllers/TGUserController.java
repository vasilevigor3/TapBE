package com.TatapCasino.controllers;

import com.TatapCasino.model.TGUserModel;
import com.TatapCasino.service.TGUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TGUserController {

    @Autowired
    private TGUserService TGUserService;

    @GetMapping("/TGUsers")
    public List<TGUserModel> getAllTGUsers() {
        return TGUserService.getAllTGUsers();
    }

    @PostMapping("/getOrCreateTGUser")
    public TGUserModel getOrCreateTGUser(@RequestBody final Map<String, String> requestData) {
        final long id = Long.parseLong(requestData.get("id"));
        final String userName = requestData.get("name");

        return TGUserService.getOrCreateTGUser(id, userName);
    }
}
