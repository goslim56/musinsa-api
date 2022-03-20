package com.musinsa.musinsaapi.domain;

import com.musinsa.musinsaapi.exception.CategoryLoopException;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(columnDefinition = "bigint default 0")
    private Long categoryLevel = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) throws CategoryLoopException {
        if (this.parent == parent) {
            return;
        }

        checkLoopIfSetParent(parent);
        this.parent = parent;
        this.setCategoryLevel(parent.getCategoryLevel()+1);
        parent.getChild().add(this);
    }

    private void setCategoryLevel(Long categoryLevel) {
        this.categoryLevel = categoryLevel;
        List<Category> child = this.getChild();
        for (Category children : child) {
            children.setCategoryLevel(categoryLevel+1);
        }
    }

    public void addChildCategory(Category category) throws CategoryLoopException {
        category.setParent(this);
    }

    private void checkLoopIfSetParent(Category targetParent) throws CategoryLoopException {
        if (this == targetParent) {
            throw new CategoryLoopException();
        }

        List<Category> child = this.getChild();
        for (Category childern : child) {
            childern.checkLoopIfSetParent(targetParent);
        }
    }
}
