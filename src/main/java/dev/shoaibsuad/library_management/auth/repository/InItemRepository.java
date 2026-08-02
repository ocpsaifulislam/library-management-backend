package dev.shoaibsuad.library_management.auth.repository;

import dev.shoaibsuad.library_management.auth.entity.InItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InItemRepository extends JpaRepository<InItem, Long> {
    @Query("SELECT i FROM InItem i ORDER BY i.itemName ASC")
    List<InItem> findAllFlat();

    // Projection interface for fast read performance
    interface ItemProjection {
        Long getItemNo();
        String getItemName();
        Long getSubitemOf();
    }

    @Query("SELECT i.itemNo AS itemNo, i.itemName AS itemName, i.subitemOf AS subitemOf FROM InItem i ORDER BY i.itemName ASC")
    List<ItemProjection> findAllFlatProjections();
}
