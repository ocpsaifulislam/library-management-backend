package dev.shoaibsuad.library_management.auth.service;

import dev.shoaibsuad.library_management.auth.dto.response.InItemTreeResponse;

import java.util.List;

public interface InItemService {
    List<InItemTreeResponse> getItemTree();
    List<InItemTreeResponse> getItemTreeCaches();
}
