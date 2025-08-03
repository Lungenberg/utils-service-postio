package com.post_hub.utils_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaginationResponse<T> implements Serializable {

    private List<T> content;
    private Pagination pagination;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination implements Serializable {

        private long total; // общее кол-во записей
        private int limit; // лимит записей
        private int page; // текущая страница
        private int pages; // общее кол-во страниц
    }
    // вспомогательный класс, содержащий данные

}
// используем для возврата любого списка данных, не только постов

