package in.thefarhan.sb_ecomm.service;

import in.thefarhan.sb_ecomm.exceptions.ResourceNotFoundException;
import in.thefarhan.sb_ecomm.model.Category;
import in.thefarhan.sb_ecomm.model.Product;
import in.thefarhan.sb_ecomm.payload.ProductDTO;
import in.thefarhan.sb_ecomm.payload.ProductResponse;
import in.thefarhan.sb_ecomm.repositories.CategoryRepository;
import in.thefarhan.sb_ecomm.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId,ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO,Product.class);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice()-((product.getDiscount()*.01)* product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map((e)-> modelMapper.map(e,ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( ()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream()
                .map((e)-> modelMapper.map(e,ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%");
        List<ProductDTO> productDTOS = products.stream()
                .map((e)-> modelMapper.map(e,ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product newProduct = modelMapper.map(productDTO,Product.class);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        existingProduct.setProductName(newProduct.getProductName());
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setQuantity(newProduct.getQuantity());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDiscount(newProduct.getDiscount());

        double specialPrice = newProduct.getPrice()
                - (newProduct.getPrice() * newProduct.getDiscount() / 100);
        existingProduct.setSpecialPrice(specialPrice);
        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product deletedproduct = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        productRepository.delete(deletedproduct);
        return modelMapper.map(deletedproduct,ProductDTO.class);
    }
}
