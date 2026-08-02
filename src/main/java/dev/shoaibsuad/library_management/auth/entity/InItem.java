package dev.shoaibsuad.library_management.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "IN_ITEM")
@Getter
@Setter
public class InItem {
    @Id
    @Column(name = "ITEM_NO")
    private Long itemNo;

    @Column(name = "ITEM_NAME", nullable = false)
    private String itemName;

    @Column(name = "SUBITEM_OF")
    private Long subitemOf;
}
