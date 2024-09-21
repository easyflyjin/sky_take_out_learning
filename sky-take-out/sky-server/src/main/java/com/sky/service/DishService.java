package com.sky.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

@Service
public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult getDishByPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);

    DishVO getDishById(Long id);

    void updateDish(DishDTO dishDTO);

    void updateDishStatus(Long id);

    List<Dish> getDishByCategoryId(Integer categoryId);

    List<DishVO> listWithFlavor(Dish dish);
}
