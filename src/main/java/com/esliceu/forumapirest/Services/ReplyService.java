package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.Models.Reply;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Repos.ReplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
@Autowired
ReplyRepo replyRepo;

    public List<Reply> getRepliesByTopicId(long id) {
        return replyRepo.findAllRepliesById(id);
    }

    public User getUserByReplyId(long id) {
        return replyRepo.findById(id).get().getUser();
    }
}
