package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


    public interface TopicRepo extends JpaRepository<Topic, Long> {
@Query( nativeQuery = true, value = "select topic.id,topic.content,topic.created_at,topic.title,topic.updated_at,topic.views,topic.category_id,topic.user_id from topic inner join category on topic.category_id = category.id where category.id = :id")
        List<Topic> findAllTopicsUsingCategoryTopicID(long id);
}
