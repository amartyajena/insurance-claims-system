package com.insurance.document.service;

import com.insurance.document.dto.DocumentResponse;
import com.insurance.document.entity.Document;
import com.insurance.document.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final Path uploadPath;

    public DocumentService(DocumentRepository documentRepository,
                           @Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.documentRepository = documentRepository;
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadPath);
    }

    public DocumentResponse uploadDocument(Long claimId, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String originalFileName = file.getOriginalFilename();
            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String storedFileName = UUID.randomUUID() + extension;
            Path targetLocation = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Document document = Document.builder()
                    .claimId(claimId)
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .filePath(targetLocation.toString())
                    .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .fileSize(file.getSize())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            Document saved = documentRepository.save(document);
            return mapToResponse(saved);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to upload file");
        }
    }

    public DocumentResponse getDocumentMetadata(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

        return mapToResponse(document);
    }

    public List<DocumentResponse> getDocumentsByClaimId(Long claimId) {
        return documentRepository.findByClaimId(claimId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Resource downloadDocument(Long id) {
        try {
            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

            Path filePath = Paths.get(document.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found on disk");
            }

            return resource;
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Failed to download file");
        }
    }

    public String getContentType(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        return document.getContentType();
    }

    public String getOriginalFileName(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        return document.getOriginalFileName();
    }

    public String deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

        try {
            Files.deleteIfExists(Paths.get(document.getFilePath()));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete file from disk");
        }

        documentRepository.delete(document);
        return "Document deleted successfully";
    }

    private DocumentResponse mapToResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .claimId(document.getClaimId())
                .originalFileName(document.getOriginalFileName())
                .contentType(document.getContentType())
                .fileSize(document.getFileSize())
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}