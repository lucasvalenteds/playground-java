package com.playground.java.hibernate.manytoone.bidirectional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PostComment> comments = new ArrayList<>();

    public void addComment(PostComment comment) {
        comment.post = this;
        this.comments.add(comment);
    }
}
