package com.loopin.loopinbackend.domain.post.qeury;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchCond {
    private int page = 0;
    private int size = 10;
    private Long userId;
    private String sortBy;
    private String direction;
}
