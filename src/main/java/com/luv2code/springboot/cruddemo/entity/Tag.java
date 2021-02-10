package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tag")
public class Tag {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag")
    private int idTag;


    @Column(name = "name")
    private String name;



    @ManyToMany(fetch=FetchType.LAZY , cascade = {CascadeType.PERSIST , CascadeType.MERGE , CascadeType.DETACH , CascadeType.REFRESH })
    @JoinTable(name = "post_tag" ,
            joinColumns = {@JoinColumn(name = "tag_id_tag")}, inverseJoinColumns = {@JoinColumn(name ="post_id_post")})
    private List<Post> posts;

    public Tag(){

    }

    public Tag(String name) {
        this.name = name;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Post> getPosts() {
//        return posts;
//    }
//
//    public void setPosts(List<Post> posts) {
//        this.posts = posts;
//    }
}
