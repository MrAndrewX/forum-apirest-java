package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Repos.CategoryRepo;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    public void addCategory(String slug, String title, String description, String color) {
        Category category = new Category();
        category.setSlug(slug);
        category.setTitle(title);
        category.setDescription(description);
        category.setColor(color);
        categoryRepo.save(category);

    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }


    public Category getCategoryById(int categoryid) {
        return categoryRepo.findById((long) categoryid).get();
    }

    public Category getCategoryBySlug(String slug) {
        return categoryRepo.findBySlug(slug);
    }


    public boolean exists(String slug) {
        return categoryRepo.existsBySlug(slug);
    }

    public void updateCategory(String slug, String title, String description) {

        categoryRepo.updateCategory(slug, title, description);

    }
}


