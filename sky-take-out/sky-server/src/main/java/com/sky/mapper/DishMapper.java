package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);

    Page<DishVO> getDishByPage(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getDishById(Long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    void deleteByIds(List<Long> ids);


    @AutoFill(value = OperationType.UPDATE)
    void updateDish(Dish dish);

    
    @Update("update dish set status = #{status} where id = #{id}")
    @AutoFill(value = OperationType.UPDATE)
    void updateDishStatus(Dish dish);

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getDishByCategoryId(Integer categoryId);


}
