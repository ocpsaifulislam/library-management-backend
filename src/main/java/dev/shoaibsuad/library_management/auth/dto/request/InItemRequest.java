package dev.shoaibsuad.library_management.auth.dto.request;

public record InItemRequest(
        Long itemNo,
        String itemName,
        Long subitemOf
) {}