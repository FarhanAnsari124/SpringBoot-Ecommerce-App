package in.thefarhan.sb_ecomm.service;


import in.thefarhan.sb_ecomm.model.Product;
import in.thefarhan.sb_ecomm.payload.ProductDTO;
import in.thefarhan.sb_ecomm.payload.ProductResponse;
import org.springframework.stereotype.Service;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts();
}
