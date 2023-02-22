package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findBySlug(String slug);



}
