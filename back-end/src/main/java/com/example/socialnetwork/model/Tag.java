package com.example.socialnetwork.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer offsetLeft;

    @Column
    private Integer offsetTop;

    @ManyToOne
    private PostPhoto postPhoto;

    @ManyToOne
    private User user;
}
