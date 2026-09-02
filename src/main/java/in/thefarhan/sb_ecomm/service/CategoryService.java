package in.thefarhan.sb_ecomm.service;

import in.thefarhan.sb_ecomm.model.Category;
import in.thefarhan.sb_ecomm.payload.CategoryDTO;
import in.thefarhan.sb_ecomm.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}
