package com.sky.service.impl;

import java.util.List;

import com.sky.vo.DishItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealPageQueryVO;
import com.sky.vo.SetmealVO;


@Service
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;

    // @Qualifier("setmealService")
    @Autowired
    private SetmealService setmealService;

    @Override
    @Transactional
    public void addCombo(SetmealDTO setmealDTO) {
        
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        

        //将前7个属性添加到Setmeal 表下中
        setmealMapper.addCombo(setmeal);
        

        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //将几个属性添加到Setmeal_dish表中
        if(setmealDishes != null && setmealDishes.size() > 0){
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealId);
                setmealDishMapper.addSetMealDish(setmealDish);
            }
        }
    }


    @Override
    public SetmealVO getComboById(Long id) {
        
        //获取SetMeal表 中的主体数据 和CategoryName

        Setmeal setmeal = setmealMapper.getSetmealById(id);
        Long categoryId = setmeal.getCategoryId();
        String categoryName = categoryMapper.getCategoryNameById(categoryId);

        //获取setmeal_dish表中的主题数据
        List<SetmealDish> setmealDishList = setmealDishMapper.getSetmealDishById(id);

        //整合包装并返回controller层
        return SetmealVO.builder()
            .id(id)
            .categoryId(categoryId)
            .categoryName(categoryName)
            .description(setmeal.getDescription())
            .image(setmeal.getImage())
            .name(setmeal.getName())
            .setmealDishes(setmealDishList)
            .price(setmeal.getPrice())
            .status(setmeal.getStatus())
            .updateTime(setmeal.getUpdateTime())
            .build();
    }

    @Override
    public PageResult getComboByPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        //获取数据
        Page<SetmealPageQueryVO> setmealPageQueryVOs = setmealMapper.getSetmealByPage(setmealPageQueryDTO);


        //Page<SetmealPageQueryVO> p = (Page<SetmealPageQueryVO>) setmealPageQueryVOs;

        return new PageResult(setmealPageQueryVOs.getTotal(),setmealPageQueryVOs.getResult());
    }

    @Override
    public void updateComboState(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);

        setmealMapper.updateComboState(setmeal);
    }

    @Override
    @Transactional
    public void updateCombo(SetmealDTO setmealDTO) {
        //修改SetMeal表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.updateSetmealCombo(setmeal);


        //删除SetmealDish中原有的数据
        setmealDishMapper.deleteBySetmealId(setmeal.getId());

        //修改SetmealDish表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for(SetmealDish s : setmealDishes){
            s.setSetmealId(setmeal.getId());
            setmealDishMapper.addSetMealDish(s);
        }


    }

    @Override
    @Transactional
    public void deleteBatchCombo(String ids) {
        //ids = ids.substring(1, ids.length() - 1);
        String[] str_ids = ids.split(",");
        Long[] int_list = new Long[str_ids.length];
        for (int i = 0; i < int_list.length; i++) {
            int_list[i] = Long.parseLong(str_ids[i]);
            setmealMapper.deleteCombo(int_list[i]);
            setmealDishMapper.deleteBySetmealId(int_list[i]);
        }
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}