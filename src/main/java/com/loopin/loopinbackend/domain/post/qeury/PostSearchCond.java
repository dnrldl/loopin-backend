package com.loopin.loopinbackend.domain.post.qeury;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchCond {
    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
    private Long userId;
    private String sortBy;
    private String direction;
}
