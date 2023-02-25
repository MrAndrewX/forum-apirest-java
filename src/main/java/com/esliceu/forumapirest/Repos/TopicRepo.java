package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.Topic;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

@Transactional
public interface TopicRepo extends JpaRepository<Topic, Long> {
    @Query( nativeQuery = true, value = "select topic.id,topic.content,topic.created_at,topic.title,topic.updated_at,topic.views,topic.category_id,topic.user_id from topic inner join category on topic.category_id = category.id where category.id = :id")
    List<Topic> findAllTopicsUsingCategoryTopicID(long id);

@Modifying
    @Query("update Topic t set t.title = :title, t.content = :content, t.category.id = :categoryid where t.id = :topicid")
    void updateTopic(@Param("title") String title, @Param("content")String content,@Param("categoryid") long categoryid,@Param("topicid") long topicid);
}
