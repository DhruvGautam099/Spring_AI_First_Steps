package com.Dhruv.SpringAIMK1.Controllers;

import com.Dhruv.SpringAIMK1.Services.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/describe")
    public ResponseEntity<String> describe(@RequestParam("file") MultipartFile file,
                                           @RequestParam("question") String question) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload an image");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image files are allowed");
        }
        if (question.isBlank()) {
            return ResponseEntity.badRequest().body("Please ask a question about the image");
        }
        return ResponseEntity.ok(imageService.describeImage(file, question));
    }
}