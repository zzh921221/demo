package com.asset.bo;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

/**
 * @author: zhaozhonghui
 * @create: 2019-06-25 19:38
 */
@Data
public class UserInfo {

    private Long id;

    private String name;

    private int age;

    private int count;

    private Byte status;
}
