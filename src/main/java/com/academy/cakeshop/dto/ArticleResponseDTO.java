package com.academy.cakeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record ArticleResponseDTO(
        String articleName,
        Double price,
        Long productId,
        String productName
) {
}
