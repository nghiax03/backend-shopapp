package com.project.shopapp.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.github.javafaker.Faker;
import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.utils.MessageKeys;

import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final LocalizationUtils localizationUtils;
	private final IProductService productService;
	
	@PostMapping("")
	//http://localhost:8088/v1/api/products
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, //
			// @RequestPart("file") MultipartFile file, //
			BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessage);
			}
			Product newProduct = productService.createProduct(productDTO);

			return ResponseEntity.ok(newProduct);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	//http://localhost:8088/v1/api/products/uploads/3
	public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files,//
			@PathVariable("id") long productId){
		
	    try {
	    	Product existingProduct = productService.getProductById(productId);
			files = files == null ? new ArrayList<MultipartFile>() : files;
			if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
				return ResponseEntity.badRequest().body(
						localizationUtils.getLocalizationUtils(MessageKeys.UPLOAD_IMAGES_MAX_5));
			}
			List<ProductImage> productImages = new ArrayList<>();
			for (MultipartFile file : files) {
				if(file.getSize() == 0) {
					continue;
				}
				
				// kiem tra kich thuoc file va dinh dang
				// file > 10mb, 1m = 1024kb ,1kb = 1024b
				if (file.getSize() > 10 * 1024 * 1024) {
					return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
							.body(localizationUtils
									.getLocalizationUtils(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
				}
				String contentType = file.getContentType();
				if (contentType == null || !contentType.startsWith("image/")) {
					return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
							.body(localizationUtils
									.getLocalizationUtils(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
				}
				String fileName = storeFile(file);
				// thay the ham nay vs code cua ban de save file
				// save object vao csdl java
				
			ProductImage productImage =	productService.createProductImage(
					    existingProduct.getId(),//
						ProductImageDTO.builder()
						.imageUrl(fileName).build());
			
			productImages.add(productImage);
			}
			return ResponseEntity.ok().body(productImages);

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@GetMapping("")
	public ResponseEntity<ProductListResponse> getProducts(@RequestParam("page") int page,
			@RequestParam("limit") int limit) {
		PageRequest pageRequest = PageRequest.of(page, limit,
				Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
        		.products(products).totalpage(totalPages).build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
		try {
			Product existingProduct = productService.getProductById(productId);
			return ResponseEntity.ok(ProductResponse.formProduct(existingProduct));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	
	
	
	

	private String storeFile(MultipartFile file) throws IOException {
		
		if(!isImageFile(file) || file.getOriginalFilename()== null) {
			throw new IOException("Invalid image format");
		}
		// ten file goc
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

		String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
		java.nio.file.Path uploadDir = Paths.get("uploads");
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}
		// duong dan den file dich
		java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

		try (BufferedWriter writer = Files.newBufferedWriter(destination, StandardCharsets.UTF_8)) {
			Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
		}

		return uniqueFileName;
	}
	
	private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType!=null && contentType.startsWith("image/");
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,//
			@RequestBody ProductDTO productDTO){
		try {
			Product updateProduct = productService.updateProduct(id, productDTO);
			return ResponseEntity.ok(updateProduct);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
		try {
		     productService.deleteProduct(id);
			return ResponseEntity
					.ok(String.format("Product with id = %d deleted successfully", id));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//@PostMapping("/generateFakeProducts")
	//dung de test cac ban ghi (fake.maven)
	private ResponseEntity<String> gerenateFakeProducts(){
		Faker faker = new Faker();
		for(int i=0;i<1_000_000;i++) {
			String productName = faker.commerce().productName();
			if(productService.existByName(productName)) {
				continue;
			}
			ProductDTO productDTO = ProductDTO.builder()
					.name(productName)
					.price((float)faker.number().numberBetween(10, 90_000_000))
					.description(faker.lorem().sentence())
					.thumbnail("")
					.categoryId((long)faker.number().numberBetween(2,5))
					.build();
			try {
				productService.createProduct(productDTO);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.ok("Fake Product created successfully");
	}
}
