package in.thefarhan.sb_ecomm.service;

import in.thefarhan.sb_ecomm.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}
