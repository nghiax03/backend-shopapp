package com.project.shopapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
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
				                 .category(existingCategory)
				                 .build();
		return productRepository.save(newProduct);
				              
	}

	@Override
	public Product getProductById(Long productId) throws Exception {
		Optional<Product> optionalProduct = productRepository.getDetailProduct(productId);
		if(optionalProduct.isPresent()) {
			return optionalProduct.get();
		}
		throw new DataNotFoundException("Cannot find product witd id = " + productId);
	}
	
	@Override
	public List<Product> findProductsByIds(List<Long> productIds){
		return productRepository.findProductsByIds(productIds);
	}

	@Override
	public Page<ProductResponse> getAllProducts(String keyword,Long categoryId,
			 PageRequest pageRequest) {
		//lay ra danh sach san pham  theo trang and gh(limit)
		Page<Product> productPages;
		productPages = productRepository.searchProducts(categoryId, keyword, pageRequest);
		return productPages.map(ProductResponse::formProduct);
	}

	@Override	
	@Transactional
	public Product updateProduct(Long id, ProductDTO productDTO) 
			throws Exception {
		Product existingProduct = getProductById(id);
		if(existingProduct != null) {
			Category existingCategory = categoryRepostitory.findById(productDTO.getCategoryId())
				      .orElseThrow(() -> new DataNotFoundException(
				    		  "Cannot find category with id = " + productDTO.getCategoryId()));
					existingProduct.setName(productDTO.getName());
					existingProduct.setCategory(existingCategory);
					existingProduct.setPrice(productDTO.getPrice());
					existingProduct.setDescription(productDTO.getDescription());
					existingProduct.setThumbnail(productDTO.getThumbnail());
					
					return productRepository.save(existingProduct);
		}
		return null;
		
	}

	@Override
	@Transactional
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
	@Transactional
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
