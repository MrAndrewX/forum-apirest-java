package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.Models.Topic;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Repos.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepo topicRepo;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;
    public List<Topic> getTopicsByCategoryId(long id) {
        return topicRepo.findAllTopicsUsingCategoryTopicID(id);
    }

    public Topic getTopicById(String id) {
        return topicRepo.findById(Long.parseLong(id)).get();
    }

    public Topic createTopic(String category, String title, String content, String token) {
        Topic topic = new Topic();
        topic.setContent(content);
        topic.setCreatedAt(Instant.now());
        topic.setTitle(title);
        topic.setUpdatedAt(Instant.now());
        topic.setViews(0);
        topic.setCategory(categoryService.getCategoryBySlug(category));
        topic.setUser(userService.getUserByEmail(tokenService.getEmailFromToken(token)));

        return topicRepo.save(topic);


    }


    public void updateTopic(String topicid, String title, String content, long categoryid) {
       topicRepo.updateTopic(title,content,categoryid,Long.parseLong(topicid));
    }

    public void deleteTopic(String id) {
        topicRepo.deleteAllRepliesByTopicId(Long.parseLong(id));
        topicRepo.deleteById(Long.parseLong(id));
    }
}
