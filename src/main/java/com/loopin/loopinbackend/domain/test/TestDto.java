package com.loopin.loopinbackend.domain.test;

import lombok.Data;

import java.util.List;

@Data
public class TestDto {
    private List<Item> items;

    @Data
    public static class Item {
        private String id;
        private String name;

        private VlidInfo vlidInfo;

        @Data
        public static class VlidInfo {
            private String dtm;
        }
    }
}
