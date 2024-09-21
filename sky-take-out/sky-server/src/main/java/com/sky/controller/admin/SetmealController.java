package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/admin")
@Api(tags = "套餐接口")
public class SetmealController {
    
    @Autowired
    private SetmealService setmealService;
    
    @PostMapping("/setmeal")
    @ApiOperation("addCombo")
    public Result addCombo(@RequestBody SetmealDTO setmealDTO) {
        setmealService.addCombo(setmealDTO);
        return Result.success();
    }

    @GetMapping("/setmeal/{id}")
    @ApiOperation("getComboById")
    public Result getComboById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getComboById(id);
        return Result.success(setmealVO);
    }

    @GetMapping("/setmeal/page")
    @ApiOperation("getComboByPage")
    public Result<PageResult> getComboByPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.getComboByPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/setmeal/status/{status}")
    @ApiOperation("comboState")
    public Result comboState(@RequestParam Long id, @PathVariable Integer status) {
        setmealService.updateComboState(id,status);
        return Result.success();
    }

    @RequestMapping(value = "/setmeal", method = RequestMethod.PUT)
    @ApiOperation("updateCombo")
    public Result updateCombo(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateCombo(setmealDTO);
        return Result.success(setmealDTO);
    }
    @DeleteMapping("/setmeal")
    @ApiOperation("deleteBatchCombo")
    public Result deleteBatchCombo(@RequestParam String ids) {
        setmealService.deleteBatchCombo(ids);
        return Result.success();
    }
}

