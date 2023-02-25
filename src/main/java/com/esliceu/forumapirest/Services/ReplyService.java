package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.Models.Reply;
import com.esliceu.forumapirest.Models.Topic;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Repos.ReplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ReplyService {
    @Autowired
    ReplyRepo replyRepo;
    @Autowired
    TopicService topicService;

    public List<Reply> getRepliesByTopicId(long id) {
        return replyRepo.findAllReplyByTopicId(id);
    }

    public User getUserByReplyId(long id) {
        return replyRepo.findById(id).get().getUser();
    }

    public Reply createReply(String content, User u, long topicId) {
        Reply r = new Reply();
        r.setContent(content);
        Topic t = topicService.getTopicById(String.valueOf(topicId));
        r.setTopic(t);
        r.setUser(u);
        r.setCreatedAt(Instant.now());
        r.setUpdatedAt(Instant.now());

        return replyRepo.save(r);
    }

    public void deleteReply(long replyId) {
    replyRepo.deleteById(replyId);

    }
}
