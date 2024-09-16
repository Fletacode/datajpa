package com.jpadata.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Item implements Persistable<String> {

    @Id
    private String id;

    @Column(name="item_name")
    String name;

    @CreatedDate
    LocalDateTime localDateTime;

    protected Item(){}

    public Item(String name,String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    //새 엔티티인지 확인하는 조건 부여
    @Override
    public boolean isNew() {
        return localDateTime == null;
    }
}
