package com.academy.cakeshop.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailAttachmentDto implements Serializable {
    private String encodeFile;
    private String fileName;
    private String fileType;
    private String disposition;

    @Override
    public String toString() {
        return "EmailAttachmentDto{" +
                "encodeFile='" + encodeFile + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", disposition='" + disposition + '\'' +
                '}';
    }
}