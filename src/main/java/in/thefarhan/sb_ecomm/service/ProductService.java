package in.thefarhan.sb_ecomm.service;


import in.thefarhan.sb_ecomm.model.Product;
import in.thefarhan.sb_ecomm.payload.ProductDTO;
import in.thefarhan.sb_ecomm.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(Long categoryId);

    ProductResponse getProductsByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);
}
