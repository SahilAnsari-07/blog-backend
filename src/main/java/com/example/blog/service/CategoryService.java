package com.example.blog.service;

import com.example.blog.dto.CategoryRequest;
import com.example.blog.dto.CategoryResponse;
import com.example.blog.exception.DuplicateResourceException;
import com.example.blog.mapper.CategoryMapper;
import com.example.blog.model.Category;
import com.example.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryResponse saveCategory(CategoryRequest categoryRequest){
        if (categoryRepository.existsByName(categoryRequest.getName())){
            Map<String, String > error = new HashMap<>();
            error.put("Category", categoryRequest.getName()+" Category is Already exists ");
            throw new DuplicateResourceException(error);
        }
        Category category = categoryMapper.toEntity(categoryRequest);
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

}
