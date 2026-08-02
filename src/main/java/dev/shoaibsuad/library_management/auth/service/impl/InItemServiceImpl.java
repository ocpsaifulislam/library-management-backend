package dev.shoaibsuad.library_management.auth.service.impl;

import dev.shoaibsuad.library_management.auth.dto.response.InItemTreeResponse;
import dev.shoaibsuad.library_management.auth.entity.InItem;
import dev.shoaibsuad.library_management.auth.repository.InItemRepository;
import dev.shoaibsuad.library_management.auth.service.InItemService;
import dev.shoaibsuad.library_management.common.constants.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InItemServiceImpl implements InItemService {

    private final InItemRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<InItemTreeResponse> getItemTree() {
        List<InItem> items = repository.findAllFlat();
        return buildTree(items.stream()
                .map(item -> new ItemData(item.getItemNo(), item.getItemName(), item.getSubitemOf()))
                .toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheNames.IN_ITEM_TREE)
    public List<InItemTreeResponse> getItemTreeCaches() {
        List<InItemRepository.ItemProjection> items = repository.findAllFlatProjections();
        return buildTree(items.stream()
                .map(item -> new ItemData(item.getItemNo(), item.getItemName(), item.getSubitemOf()))
                .toList());
    }

    // Common lightweight record to unify Entity & Projection data mapping
    private record ItemData(Long itemNo, String itemName, Long subitemOf) {}

    // Single reusable method for O(N) tree building
    private List<InItemTreeResponse> buildTree(List<ItemData> items) {
        Map<Long, InItemTreeResponse> nodeMap = new HashMap<>(items.size());
        List<InItemTreeResponse> roots = new ArrayList<>();

        // Pass 1: Create nodes
        for (ItemData item : items) {
            nodeMap.put(item.itemNo(), new InItemTreeResponse(item.itemNo(), item.itemName(), item.subitemOf(), new ArrayList<>()));
        }

        // Pass 2: Connect parents and children
        for (ItemData item : items) {
            InItemTreeResponse currentNode = nodeMap.get(item.itemNo());
            Long parentId = item.subitemOf();

            if (parentId == null) {
                roots.add(currentNode);
            } else {
                InItemTreeResponse parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.children().add(currentNode);
                }
            }
        }

        return roots;
    }
}