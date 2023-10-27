package com.safaricom.dxl.tempral_api.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDto {
    private String recipient;
    private String content;
    private String subject;
    private boolean isHtml;

}
