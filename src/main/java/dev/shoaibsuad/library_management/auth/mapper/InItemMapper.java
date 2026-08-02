package dev.shoaibsuad.library_management.auth.mapper;

import dev.shoaibsuad.library_management.auth.dto.request.InItemRequest;
import dev.shoaibsuad.library_management.auth.dto.response.InItemTreeResponse;
import dev.shoaibsuad.library_management.auth.entity.InItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InItemMapper {
    InItem toEntity(InItemRequest request);
    InItemTreeResponse toTreeDto(InItem inItem);
}
