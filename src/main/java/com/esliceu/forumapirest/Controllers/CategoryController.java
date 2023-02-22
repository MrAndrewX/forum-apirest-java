package com.esliceu.forumapirest.Controllers;

import com.esliceu.forumapirest.DTOs.CategoryDTO;
import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Services.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/categories")
    @CrossOrigin
    public Map<String, Object> categories(HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();


        List<Category> categories = categoryService.getAllCategories();



        for (Category c: categories) {
            Map <String, Object> category = new HashMap<>();
            category.put("_id",c.getId());
            category.put("title",c.getTitle());
            category.put("description",c.getDescription());
            category.put("slug",c.getSlug());
            category.put("color",c.getColor());
            category.put("__v",0);
            map.put(c.getId()+"",category);

        }




        return map;
    }

    @PostMapping("/categories")
    @CrossOrigin
    public Map<String, Object> createCategory(HttpServletResponse response, @RequestBody CategoryDTO cp) throws IOException {

        Map<String, Object> map = new HashMap<>();
        Map <String, Object> category = new HashMap<>();
        int _id = categoryService.getAllCategories().size()+1;
        String slug = cp.getTitle().replace(" ","-");
        String color = "hsl(11, 50%, 50%)";
        category.put("_id",_id);
        category.put("title",cp.getTitle());
        category.put("description",cp.getDescription());
        category.put("slug",slug);
        category.put("color",color);
        category.put("__v",0);


        categoryService.addCategory(slug, cp.getTitle(), cp.getDescription(), color);
        /*Redirect*/


        return map;
    }


    @GetMapping("/categories/{slug}")
    @CrossOrigin
    public Map<String, Object> categoryfullurl(HttpServletResponse response, @PathVariable String slug) {

        Category categoryObj = categoryService.getCategoryBySlug(slug);

        System.out.println(categoryObj);
        Map <String, Object> category = new HashMap<>();
        category.put("moderators", new String[]{});
        category.put("_id",categoryObj.getId());
        category.put("title",categoryObj.getTitle());
        category.put("description",categoryObj.getDescription());
        category.put("slug",categoryObj.getSlug());

        category.put("color",categoryObj.getColor());
        category.put("__v",0);



        return category;
    }

}
