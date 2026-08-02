package dev.shoaibsuad.library_management.auth.controller;

import dev.shoaibsuad.library_management.auth.dto.response.InItemTreeResponse;
import dev.shoaibsuad.library_management.auth.service.InItemService;
import dev.shoaibsuad.library_management.common.constants.ApiEndpoints;
import dev.shoaibsuad.library_management.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.Auth.BASE_AUTH)
@RequiredArgsConstructor
public class InItemController {

    private final InItemService service;

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<InItemTreeResponse>>> getItemTree() {
        ApiResponse<List<InItemTreeResponse>> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Tree fetched successfully",
                service.getItemTree()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/itemTree")
    public ResponseEntity<ApiResponse<List<InItemTreeResponse>>> getItemTreeCaches() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Tree fetched successfully",
                        service.getItemTreeCaches()
                )
        );
    }
}