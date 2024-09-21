package com.sky.mapper;

import java.util.List;

import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealPageQueryVO;

@Mapper
public interface SetmealMapper {

    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set  category_id = #{categoryId}, description = #{description}, image = #{image}, name = #{name}, price = #{price}, status = #{status} where id = #{id}")
    void updateSetmealCombo(Setmeal setmeal);

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
    
    
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into setmeal (category_id, name, price, status, description, image,create_time,update_time,create_user,update_user) values (#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addCombo(Setmeal setmeal);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getSetmealById(Long id);


    Page<SetmealPageQueryVO> getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateComboState(Setmeal setmeal);

    @Delete("delete from setmeal where id = #{id}")
    void deleteCombo(Long id);

    List<Setmeal> list(Setmeal setmeal);

    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
