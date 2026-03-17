package com.insurance.document.controller;

import com.insurance.document.dto.DocumentResponse;
import com.insurance.document.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload/{claimId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(@PathVariable Long claimId,
                                                           @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(documentService.uploadDocument(claimId, file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentMetadata(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentMetadata(id));
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByClaimId(@PathVariable Long claimId) {
        return ResponseEntity.ok(documentService.getDocumentsByClaimId(claimId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Resource resource = documentService.downloadDocument(id);
        String contentType = documentService.getContentType(id);
        String fileName = documentService.getOriginalFileName(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.deleteDocument(id));
    }
}