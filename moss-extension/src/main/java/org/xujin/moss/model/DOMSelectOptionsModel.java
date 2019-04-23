package org.xujin.moss.model;

import lombok.*;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/11/22 9:57
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class DOMSelectOptionsModel {
    private String value;
    private String text;
}
