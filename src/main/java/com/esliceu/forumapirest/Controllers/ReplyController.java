package com.esliceu.forumapirest.Controllers;

import com.esliceu.forumapirest.DTOs.ReplyDTO;
import com.esliceu.forumapirest.Models.Reply;
import com.esliceu.forumapirest.Models.Topic;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Services.ReplyService;
import com.esliceu.forumapirest.Services.TokenService;
import com.esliceu.forumapirest.Services.TopicService;
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
    @Autowired
    TopicService topicService;









    @PostMapping("/topics/{topicId}/replies")
    @CrossOrigin
    public Map<String,Object> createReply(@PathVariable("topicId") long topicId, @RequestBody ReplyDTO replyDTO, @RequestHeader("Authorization") String token){
        Map<String,Object> map = new HashMap<>();
        User user = userService.getUserByEmail(tokenService.getEmailFromToken(token.replace("Bearer ","")));
        Reply reply = replyService.createReply(replyDTO.getContent(),user,topicId);


        map.put("content",replyDTO.getContent());
        map.put("createdAt", Instant.now());

        map.put("topic",String.valueOf(topicId));
        map.put("updatedAt",Instant.now());
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("avatarUrl","");
        userMap.put("email",user.getEmail());
        userMap.put("id",String.valueOf(user.getId()));
        userMap.put("name",user.getName());
        userMap.put("role",user.getRole());
        userMap.put("__v",0);
        userMap.put("_id",String.valueOf(user.getId()));

        map.put("user",userMap);
        map.put("__v",0);
        map.put("_id",String.valueOf(reply.getId()));

        return map;

    }

    @DeleteMapping("/topics/{topicId}/replies/{replyId}")
    @CrossOrigin
    public boolean deleteReply(@PathVariable("topicId") long topicId, @PathVariable("replyId") long replyId, @RequestHeader("Authorization") String token){

        replyService.deleteReply(replyId);


        return true;

    }
    @PutMapping("/topics/{topicId}/replies/{replyId}")
    @CrossOrigin
    public Map<String,Object> updateReply(@PathVariable("topicId") long topicId, @PathVariable("replyId") long replyId, @RequestBody ReplyDTO replyDTO, @RequestHeader("Authorization") String token){
        Map<String,Object> map = new HashMap<>();
        User user = userService.getUserByEmail(tokenService.getEmailFromToken(token.replace("Bearer ","")));
        replyService.updateReply(replyId,replyDTO.getContent(),topicId);
        Topic topic = topicService.getTopicById(String.valueOf(topicId));
        map.put("content",replyDTO.getContent());
        map.put("createdAt", topic.getCreatedAt());
        map.put("topic",String.valueOf(topicId));
        map.put("updatedAt",Instant.now());
        map.put("user",String.valueOf(user.getId()));
        map.put("__v",0);
        map.put("_id",String.valueOf(replyId));


        return map;

    }
}
