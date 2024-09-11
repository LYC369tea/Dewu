package com.lyc.mapper;

import com.lyc.entity.Shoe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@Mapper
public interface DataMapper extends BaseMapper<Shoe> {

    Shoe selectByArticleNumber(String articleNumber, String size);

    Shoe selectByBarcode(String barcode);

    List<Shoe> selectListByArticleNumber(String articleNumber, String size);

}
