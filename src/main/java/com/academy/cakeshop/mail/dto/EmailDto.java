package com.academy.cakeshop.mail.dto;

import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailDto implements Serializable {

    private String subject;
    private String toList;
    private String body;

    public boolean isEmpty() {
        return subject.isEmpty() || toList.isEmpty() || body.isEmpty();
    }

}