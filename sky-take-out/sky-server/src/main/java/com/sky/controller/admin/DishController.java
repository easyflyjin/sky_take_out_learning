package com.sky.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        
        log.info("新增菜品：{}",dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult> getDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.getDishByPage(dishPageQueryDTO);
        return Result.success(pageResult);

    }
    
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result deleteDish(@RequestParam List<Long> ids){
        log.info("删除菜品：{}", ids);
        dishService.deleteDish(ids);
        return Result.success();
    }

    @GetMapping("/{id}")  
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO> getDish(@PathVariable Long id) {
        log.info("根据ID查询菜品:..................... {}",id);
        DishVO dishVO = dishService.getDishById(id);
        
        return Result.success(dishVO);
    }
    
    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @ApiOperation("停售起售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable @RequestParam Long id) {
        dishService.updateDishStatus(id);
        return Result.success();
    }

    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result getDishById(@RequestParam Integer categoryId) {
        List<Dish> dish = dishService.getDishByCategoryId(categoryId);
        return Result.success(dish);
    }
    
}
