package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepo extends JpaRepository<Reply, Long> {


    List<Reply> findAllReplyByTopicId(long id);
}
