package com.example.blog.mapper;

import com.example.blog.dto.CategoryRequest;
import com.example.blog.dto.CategoryResponse;
import com.example.blog.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);
    CategoryResponse toResponse(Category  category);
}
