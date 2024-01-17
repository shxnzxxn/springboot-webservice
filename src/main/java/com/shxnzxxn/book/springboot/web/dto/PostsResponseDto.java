package com.shxnzxxn.book.springboot.web.dto;

import com.shxnzxxn.book.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    /**
     * 해당 Dto는 Posts의 필드 중 일부만을 사용한다.
     * 해당 생성자만으로도 충분하므로, 모든 필드를 파라미터로 받는 형태는 사용하지 않는다.
     */
    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
