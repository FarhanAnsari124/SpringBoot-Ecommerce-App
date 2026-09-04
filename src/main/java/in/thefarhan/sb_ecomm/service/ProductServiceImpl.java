package in.thefarhan.sb_ecomm.service;

import in.thefarhan.sb_ecomm.exceptions.APIException;
import in.thefarhan.sb_ecomm.exceptions.ResourceNotFoundException;
import in.thefarhan.sb_ecomm.model.Category;
import in.thefarhan.sb_ecomm.model.Product;
import in.thefarhan.sb_ecomm.payload.ProductDTO;
import in.thefarhan.sb_ecomm.payload.ProductResponse;
import in.thefarhan.sb_ecomm.repositories.CategoryRepository;
import in.thefarhan.sb_ecomm.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId,ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO,Product.class);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product existingProduct : products) {
            if (existingProduct.getProductName().equals(product.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent){
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice()-((product.getDiscount()*.01)* product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct,ProductDTO.class);
        }else{
            throw new APIException("Product Already Exists!!!");
        }

    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map((e)-> modelMapper.map(e,ProductDTO.class))
                .toList();
        if(products.isEmpty()){
            throw new APIException("No Product Exists!!!");
        }
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

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        String fileName = fileService.uploadImage(path,image);
        product.setImage(fileName);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }


}
