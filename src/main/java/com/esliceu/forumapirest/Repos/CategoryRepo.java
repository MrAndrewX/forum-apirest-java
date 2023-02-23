package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.Category;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Transactional
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Category findBySlug(String slug);


    boolean existsBySlug(String slug);
    @Modifying
    @Query("update Category c set c.title = :title, c.description = :description where c.slug = :slug")
    void updateCategory(@Param("slug") String slug, @Param("title") String title, @Param("description") String description);


//Save




}
