package com.my.segmentation.mapper;

import com.my.segmentation.domain.TableState;
import org.apache.ibatis.annotations.Param;

/**
 * 表状态mapper
 *
 * @author: zhaozhonghui
 * @create: 2019-06-19 18:34
 */
public interface TableStateMapper {

    /**
     * 根据表名查找表的信息
     * @param
     * @return com.my.segmentation.mapper.TableState
     * @author 赵中晖
     * @date 2019/6/19
     */
    TableState selectByTableName(@Param("tableName")String tableName);
}
