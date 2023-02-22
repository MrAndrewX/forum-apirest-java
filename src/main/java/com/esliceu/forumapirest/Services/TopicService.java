package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.Models.Topic;
import com.esliceu.forumapirest.Repos.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    TopicRepo topicRepo;
    public List<Topic> getTopicsByCategoryId(long id) {
        return topicRepo.findAllTopicsUsingCategoryTopicID(id);
    }

    public Topic getTopicById(String id) {
        return topicRepo.findById(Long.parseLong(id)).get();
    }
}
