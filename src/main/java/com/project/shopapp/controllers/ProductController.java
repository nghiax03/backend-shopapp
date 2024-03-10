package com.project.shopapp.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.project.shopapp.dtos.ProductDTO;

import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.Getter;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
	@GetMapping("")
	public ResponseEntity<String> getProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return ResponseEntity.ok("getProducts here");
	}

	@GetMapping("/{id}")
	public ResponseEntity<String> getProductById(@PathVariable("id") String productId) {
		return ResponseEntity.ok("Get product with id = " + productId);
	}

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDTO productDTO, //
			// @RequestPart("file") MultipartFile file, //
			BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessage);
			}
			List<MultipartFile> files = productDTO.getFiles();
			files = files == null ? new ArrayList<MultipartFile>() : files;
			for (MultipartFile file : files) {
				if(file.getSize() == 0) {
					continue;
				}
				// kiem tra kich thuoc file va dinh dang
				// file > 10mb, 1m = 1024kb ,1kb = 1024b
				if (file.getSize() > 10 * 1024 * 1024) {
					return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
							.body(" File is too large! Maximum size is 10MB ");
				}
				String contentType = file.getContentType();
				if (contentType == null || !contentType.startsWith("image/")) {
					return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
				}
				String fileName = storeFile(file);// thay the ham nay vs code cua ban de save file
				// save object vao csdl java

			}
			return ResponseEntity.ok("Product created sucessfully");
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private String storeFile(MultipartFile file) throws IOException {
		// ten file goc
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

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

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted sucessfully! with id = " + id);
	}
}
