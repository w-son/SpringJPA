package com.son.SpringJPA.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class JpaBaseEntity {

    /*
     실제 실무에서는 엔티티를 생성한 시각과 수정한 시각을 알고 있는 것이 유지보수에 좋다고 한다
     @PrePersist와 @PreUpdate를 정의한 후에
     Member와 같은 엔티티 클래스에 extends로 정의해 주면 된다
     */

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    /*
     em.persist가 일어나기 전에 수행된다
     */

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

}
