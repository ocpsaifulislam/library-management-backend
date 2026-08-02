package dev.shoaibsuad.library_management.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record InItemTreeResponse(
        @JsonProperty("ITEM_NO")
        Long id,
        @JsonProperty("ITEM_NAME")
        String name,
        @JsonProperty("SUBITEM_OF")
        Long subitemOf,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<InItemTreeResponse> children
) {
    // Compact constructor ensuring 'children' is never null
    public InItemTreeResponse {
        if (children == null) {
            children = new ArrayList<>();
        }
    }

    // Secondary convenience constructor
    public InItemTreeResponse(Long id, String name, Long subitemOf) {
        this(id, name, subitemOf, new ArrayList<>());
    }
}