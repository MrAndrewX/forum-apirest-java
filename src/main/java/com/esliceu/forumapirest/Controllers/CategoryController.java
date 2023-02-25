package com.esliceu.forumapirest.Controllers;

import com.esliceu.forumapirest.DTOs.CategoryDTO;
import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Models.Reply;
import com.esliceu.forumapirest.Models.Topic;
import com.esliceu.forumapirest.Services.CategoryService;
import com.esliceu.forumapirest.Services.ReplyService;
import com.esliceu.forumapirest.Services.TopicService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    TopicService topicService;
    @Autowired
    ReplyService replyService;

    @GetMapping("/categories")
    @CrossOrigin
    public List<Category> categories(HttpServletResponse response) throws IOException {

        Map<Object, Object> map = new HashMap<>();
        List<Category> categories = categoryService.getAllCategories();
        List<Category> categoryList = new ArrayList<>();
        for (Category c : categories) {
            Category cat = new Category();
            cat.setId(c.getId());
            cat.setColor(c.getColor());
            cat.setDescription(c.getDescription());
            cat.setTitle(c.getTitle());
            cat.setSlug(c.getSlug());


            /*Now not using a map, using array*/
            categoryList.add(cat);


        }

        return categoryList;
    }

    @PostMapping("/categories")
    @CrossOrigin
    public Map<String, Object> createCategory(HttpServletResponse response, @RequestBody CategoryDTO cp) throws IOException {

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> category = new HashMap<>();
        int _id = categoryService.getAllCategories().size() + 1;
        String slug = cp.getTitle().replace(" ", "-");
        String color = "hsl(11, 50%, 50%)";
        category.put("_id", _id);
        category.put("title", cp.getTitle());
        category.put("description", cp.getDescription());
        category.put("slug", slug);
        category.put("color", color);
        category.put("__v", 0);


        map.put("moderators", new String[]{});
        map.put("_id", _id);
        map.put("title", cp.getTitle());
        map.put("description", cp.getDescription());
        map.put("slug", slug);
        map.put("color", color);
        map.put("__v", 0);

        if(categoryService.exists(slug)){
            //Crear categoria pero con la id de la ultima categoria +1
            System.out.println("La categoria ya existe");
            int lastId = categoryService.getAllCategories().size() + 1;
            categoryService.addCategory(slug+"-"+lastId, cp.getTitle(), cp.getDescription(), color);

            return map;

        }
        System.out.println("La categoria no existe");
            categoryService.addCategory(slug, cp.getTitle(), cp.getDescription(), color);

        /*Redirect*/


        return map;
    }


    @GetMapping("/categories/{slug}")
    @CrossOrigin
    public Map<String, Object> categoryfullurl(HttpServletResponse response, @PathVariable String slug) {


        Category categoryObj = categoryService.getCategoryBySlug(slug);

        if (categoryObj == null) {

            System.out.println("/categories/"+ slug + " no existe ");
            return null;
        }
        System.out.println(categoryObj);
        Map<String, Object> category = new HashMap<>();
        category.put("moderators", new String[]{});
        category.put("_id", categoryObj.getId());
        category.put("title", categoryObj.getTitle());
        category.put("description", categoryObj.getDescription());
        category.put("slug", categoryObj.getSlug());

        category.put("color", categoryObj.getColor());
        category.put("__v", 0);


        return category;
    }

    @PutMapping("/categories/{slug}")
    @CrossOrigin
    public Map<String, Object> updateCategory(HttpServletResponse response, @PathVariable String slug, @RequestBody CategoryDTO cp) throws IOException {
        Map map = new HashMap();
        categoryService.updateCategory(slug, cp.getTitle(), cp.getDescription());
        Category c = categoryService.getCategoryBySlug(slug);

        map.put("moderators", new String[]{});
        map.put("_id", c.getId());
        map.put("title", c.getTitle());
        map.put("description", c.getDescription());
        map.put("slug", c.getSlug());
        map.put("color", c.getColor());
        map.put("__v", 0);


        return map;
    }
    @DeleteMapping("/categories/{slug}")
    @CrossOrigin
    public boolean deleteCategory(HttpServletResponse response, @PathVariable String slug) throws IOException {
        Long id = categoryService.getCategoryBySlug(slug).getId();
        List<Topic> topics = topicService.getTopicsByCategoryId(id);

        for (Topic t : topics) {
            List<Reply> replies = replyService.getRepliesByTopicId(t.getId());
            for (Reply r : replies) {
                replyService.deleteReply(r.getId());
            }
            topicService.deleteTopic(t.getId()+"");
        }
        categoryService.deleteCategory(slug);

        return true;
    }

}
