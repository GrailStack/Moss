package org.xujin.moss.model;

import lombok.*;

import java.util.List;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/11/22 10:11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class DOMSelectModel {
    private String id;
    private String label;
    private List<DOMSelectOptionsModel> options;
}
