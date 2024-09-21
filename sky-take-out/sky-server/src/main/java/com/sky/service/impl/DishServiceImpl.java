package com.sky.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

@Service
public class DishServiceImpl implements DishService{
    
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    @Transactional  
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        
        dishMapper.addDish(dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&!flavors.isEmpty()){
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishId);
            }
            dishFlavorMapper.addFlavor(flavors);
        }
    }
    @Override
    public PageResult getDishByPage(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        
        Page<DishVO> page = dishMapper.getDishByPage(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {
        //1. 判断当前菜品是否能够删除（起售中）
        for (Long id : ids) {
            Dish dish = dishMapper.getDishById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //2. 是否被套餐关联
        List<Long> setMealIds = setmealMapper.getSetmealIdsByDishIds(ids);
        if(setMealIds!=null && setMealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3. 删除菜品表中的菜品数据
        // for (Long id : ids) {
        //     dishMapper.deleteById(id);

        //     dishFlavorMapper.deleteByDishId(id);
        // }

        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
        //4. 删除口味数据
    }
    @Override
    @Transactional
    public DishVO getDishById(Long id) {
        //查询菜品数据
        Dish dishById = dishMapper.getDishById(id);

        //查询口味数据
        List<DishFlavor> dishFlavorById = dishFlavorMapper.getDishFlavorById(id);

        //封装到DishVO
        DishVO dishVO = new DishVO();
        
        BeanUtils.copyProperties(dishById, dishVO);
        dishVO.setFlavors(dishFlavorById);

        return dishVO;
    }
    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);


        //修改菜品表基本信息
        dishMapper.updateDish(dish);

        //删除原有口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());


        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&!flavors.isEmpty()){
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.addFlavor(flavors);
        }
        
    }
    @Override
    public void updateDishStatus(Long id) {
        Dish dish = new Dish();
        dish.setId(id);

        Dish dishById = dishMapper.getDishById(id);
        Integer status = dishById.getStatus();


        if(status == StatusConstant.ENABLE){
            status = StatusConstant.DISABLE;
        }else{
            status = StatusConstant.ENABLE;
        }
        dish.setStatus(status);
        dishMapper.updateDishStatus(dish);
    }
    @Override
    public List<Dish> getDishByCategoryId(Integer categoryId) {
        
        List<Dish> d = dishMapper.getDishByCategoryId(categoryId);
        return d;
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.getDishByCategoryId(Math.toIntExact(dish.getCategoryId()));

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getDishFlavorById(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
