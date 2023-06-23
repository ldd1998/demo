package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.Stb;

/**
 * @author ldd
 */
@Mapper
public interface StbMapper extends BaseMapper<Stb> {
    void insertByTable(@Param("stb") Stb stb, @Param("tableName") String tableName);

    void selectByTable();
}
