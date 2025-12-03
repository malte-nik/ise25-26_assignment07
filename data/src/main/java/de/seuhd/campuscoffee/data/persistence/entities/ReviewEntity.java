package de.seuhd.campuscoffee.data.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@jakarta.persistence.Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class ReviewEntity extends Entity {
    @ManyToOne
    @JoinColumn(name = "pos_id")
    private PosEntity pos;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    private String review;

    @Column(name = "approval_count")
    private Integer approvalCount;

    @Column(name = "approved")
    private Boolean approved;
}
