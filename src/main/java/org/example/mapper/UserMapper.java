package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.User;

import java.util.List;

/**
 * @author ldd
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * mybatis分页查询
     * @param iPage IPage对象
     * @return IPage
     */
    List<User> selectUserByPage(IPage<User> iPage);
}
