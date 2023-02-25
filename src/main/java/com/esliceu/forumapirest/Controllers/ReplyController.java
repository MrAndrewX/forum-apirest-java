package com.esliceu.forumapirest.Controllers;

import com.esliceu.forumapirest.DTOs.ReplyDTO;
import com.esliceu.forumapirest.Models.Reply;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Services.ReplyService;
import com.esliceu.forumapirest.Services.TokenService;
import com.esliceu.forumapirest.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReplyController {
    @Autowired
    ReplyService replyService;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;









    @PostMapping("/topics/{topicId}/replies")
    @CrossOrigin
    public Map<String,Object> createReply(@PathVariable("topicId") long topicId, @RequestBody ReplyDTO replyDTO, @RequestHeader("Authorization") String token){
        Map<String,Object> map = new HashMap<>();
        User user = userService.getUserByEmail(tokenService.getEmailFromToken(token.replace("Bearer ","")));
        Reply r = replyService.createReply(replyDTO.getContent(),user,topicId);
        map.put("content",replyDTO.getContent());
        map.put("createdAt", r.getCreatedAt());
        map.put("updatedAt", r.getUpdatedAt());
        map.put("topic",topicId);
        Map <String,Object> userMap = new HashMap<>();
        userMap.put("avatarUrl","");
        userMap.put("email",user.getEmail());
        userMap.put("id",user.getId());
        userMap.put("name",user.getName());
        userMap.put("role",user.getRole());
        userMap.put("__v",0);
        userMap.put("_id",user.getId());

        map.put("user",userMap);
        map.put("__v",0);
        map.put("_id",r.getId());

        return map;

    }

    @DeleteMapping("/topics/{topicId}/replies/{replyId}")
    @CrossOrigin
    public boolean deleteReply(@PathVariable("topicId") long topicId, @PathVariable("replyId") long replyId, @RequestHeader("Authorization") String token){

        replyService.deleteReply(replyId);


        return true;

    }

}
