package com.esliceu.forumapirest.Controllers;

import com.esliceu.forumapirest.DTOs.LoginDTO;
import com.esliceu.forumapirest.DTOs.TopicDTO;
import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Models.Reply;
import com.esliceu.forumapirest.Models.Topic;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

@RestController
public class TopicController {

    @Autowired
    TopicService topicService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;
    @Autowired
    ReplyService replyService;

    @GetMapping("/categories/{slug}/topics")
    @CrossOrigin
    public Map<String, Object> topics(@PathVariable String slug, @RequestHeader("Authorization") String token) {

        Category category = categoryService.getCategoryBySlug(slug);
        List<Topic> topics = topicService.getTopicsByCategoryId(category.getId());

        Map<String, Object> map = new HashMap<>();

           for (Topic t: topics) {
                Map <String, Object> topic = new HashMap<>();
               Map<String, Object> user = new HashMap<>();
               String email = t.getUser().getEmail();
               User u = userService.getUserByEmail(email);
                topic.put("views",0);
                topic.put("_id",t.getId());
                topic.put("title",t.getTitle());
                topic.put("content",t.getContent());
                topic.put("category",category.getId());

               LoginDTO ldto = new LoginDTO();
               ldto.setRole(u.getRole());
               ldto.set_id((int) u.getId());
               ldto.setEmail(u.getEmail());
               ldto.setName(u.getName());
               ldto.set__v(0);
               ldto.setAvatarUrl("");
               ldto.setId((int)u.getId());
                topic.put("user",ldto);
                topic.put("createdAt",t.getCreatedAt());
                topic.put("updatedAt",t.getUpdatedAt());
                topic.put("__v",0);
                topic.put("replies",null);
                topic.put("numberOfReplies",0);
                topic.put("id",t.getId());

                map.put(t.getId()+"",topic);
            }

        return map;
    }

    @GetMapping("/topics/{id}")
    @CrossOrigin
    public Map<String, Object> topic(@PathVariable String id, @RequestHeader("Authorization") String token) {

        Topic topic = topicService.getTopicById(id);
        Map<String, Object> map = new HashMap<>();
Map<String, Object> category = new HashMap<>();
        Category categoryobj = topic.getCategory();
        //* Set category *//
        category.put("moderators",new String[]{});
        category.put("_id",categoryobj.getId());
        category.put("title",categoryobj.getTitle());
        category.put("description",categoryobj.getDescription());
        category.put("slug",categoryobj.getSlug());
        category.put("color",categoryobj.getColor());
        category.put("__v",0);

        //* Set user *//
        Map<String, Object> user = new HashMap<>();
        String email = topic.getUser().getEmail();
        User u = userService.getUserByEmail(email);
        user.put("role",u.getRole());
        user.put("_id",u.getId());
        user.put("email",u.getEmail());
        user.put("name",u.getName());
        user.put("__v",0);
        user.put("avatarUrl","");
        user.put("id",u.getId());

        //* Set Replies *//

        List<Reply> repliesList = replyService.getRepliesByTopicId(topic.getId());

        for (Reply r: repliesList) {

            User userReply = replyService.getUserByReplyId(r.getId());
           Map<String, Object> reply = new HashMap<>();
           Map<String, Object> userReplyMap = new HashMap<>();
           userReplyMap.put("role",userReply.getRole());
              userReplyMap.put("_id",userReply.getId());
                userReplyMap.put("email",userReply.getEmail());
                userReplyMap.put("name",userReply.getName());
                userReplyMap.put("__v",0);
                userReplyMap.put("avatarUrl","");
                userReplyMap.put("id",userReply.getId());

              reply.put("_id",r.getId());
                reply.put("content",r.getContent());
                reply.put("topic",topic.getId());
                reply.put("user",userReplyMap);
                reply.put("createdAt",r.getCreatedAt());
                reply.put("updatedAt",r.getUpdatedAt());

        }
        //* Set final map/
        map.put("_id",topic.getId());
        map.put("title",topic.getTitle());
        map.put("content",topic.getContent());
        map.put("category",category);
        map.put("user",user);
        map.put("createdAt",topic.getCreatedAt());
        map.put("updatedAt",topic.getUpdatedAt());
        map.put("__v",0);
        map.put("replies",repliesList);
        map.put("numberOfReplies",repliesList.size());
        map.put("id",topic.getId());




        return map;
    }

    @PostMapping("/topics")
    @CrossOrigin
    public Map<String, Object> createTopic(@RequestBody TopicDTO topicDTO, @RequestHeader("Authorization") String token) {
        Map <String, Object> map = new HashMap<>();
        Topic topic = topicService.createTopic(topicDTO.getCategory(),topicDTO.getTitle(),topicDTO.getContent(), token.replace("Bearer ",""));
        map.put("views",0);
        map.put("_id",topic.getId());
        map.put("title",topicDTO.getTitle());
        map.put("content",topicDTO.getContent());
        map.put("category",categoryService.getCategoryBySlug(topicDTO.getCategory()).getId());
        int userId = (int) userService.getUserByEmail(tokenService.getEmailFromToken(token.replace("Bearer ",""))).getId();
        map.put("user",userId);
        map.put("createdAt", topic.getCreatedAt());
        map.put("updatedAt",topic.getUpdatedAt());
        map.put("__v",0);
        map.put("replies",null);
        map.put("numberOfReplies",0);
        map.put("id",topic.getId());

        return map;
    }
}
