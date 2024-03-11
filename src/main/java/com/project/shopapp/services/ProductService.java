package com.project.shopapp.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepostitory;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
	
	private final ProductRepository productRepository;
	
	private final CategoryRepostitory categoryRepostitory;
	
	private final ProductImageRepository productImageRepository;

	@Override
	public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
		Category existingCategory = categoryRepostitory
				     .findById(productDTO.getCategoryId())
		             .orElseThrow(() -> 
		             new DataNotFoundException("Cannot find Category with id = " + productDTO.getCategoryId()));
		Product newProduct = Product.builder()
				                 .name(productDTO.getName())
				                 .price(productDTO.getPrice())      
				                 .thumbnail(productDTO.getThumbnail())
				                 .description(productDTO.getDescription())
				                 .categoryId(existingCategory)
				                 .build();
		return productRepository.save(newProduct);
				              
	}

	@Override
	public Product getProductById(Long productId) throws Exception {
		return productRepository.findById(productId)
				.orElseThrow(() -> new DataNotFoundException("Cannot find product with id =" + productId));
	}

	@Override
	public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
		//lay ra danh sach san pham  theo trang and gh(limit)
		return productRepository.findAll(pageRequest)
				  .map(ProductResponse::formProduct);
	}

	@Override
	public Product updateProduct(Long id, ProductDTO productDTO) 
			throws Exception {
		Product existingProduct = getProductById(id);
		Category existingCategory = categoryRepostitory.findById(productDTO.getCategoryId())
	      .orElseThrow(() -> new DataNotFoundException("Cannot find category with id = " + id));
		existingProduct.setName(productDTO.getName());
		existingProduct.setCategoryId(existingCategory);
		existingProduct.setPrice(productDTO.getPrice());
		existingProduct.setDescription(productDTO.getDescription());
		existingProduct.setThumbnail(productDTO.getThumbnail());
		
		return productRepository.save(existingProduct);
		
	}

	@Override
	public void deleteProduct(Long id) {
		// TODO Auto-generated method stub
		Optional<Product> optionalProduct = productRepository.findById(id);
		optionalProduct.ifPresent(productRepository::delete);
	}

	@Override
	public boolean existByName(String name) {
		// TODO Auto-generated method stub
		return productRepository.existsByName(name);
	}
	
	@Override
	public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
			throws Exception{
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(
	   () -> new DataNotFoundException("Cannot find product with id: "
				+ productImageDTO.getProductId()));
		ProductImage productImage = ProductImage.builder().product(existingProduct)
				.imageUrl(productImageDTO.getImageUrl()).build();
		//k insert 5 anh qua 1sp
		int size = productImageRepository.findByProductId(productId).size();
		
		if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
			throw new InvalidParamException("Number of images must be <= 5");
		}
		
		return productImageRepository.save(productImage);
	}

}
