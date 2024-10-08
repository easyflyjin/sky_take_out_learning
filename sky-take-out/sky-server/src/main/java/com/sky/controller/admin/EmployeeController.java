package com.sky.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;



/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */

    @PostMapping
    @ApiOperation("新增员工")
    public Result<?> save(@RequestBody EmployeeDTO employeeDTO) {
        System.out.println("当前线程的ID："+Thread.currentThread().getId());
        //log.info("新增员工：{}",employeeDTO); 
        employeeService.save(employeeDTO);   
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("员工分类查询")
    public Result<PageResult> findEmployeeByCategory(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult pageResult = employeeService.findEmployeeByCategory(employeePageQueryDTO);
        return Result.success(pageResult);
        
    }

    @ApiOperation("设置状态")    
    @PostMapping("/status/{status}")
    public Result setStatus(@PathVariable("status") Integer status, Long id) {
        employeeService.setStatus(status,id);
        return Result.success();
    }

    @ApiOperation("根据ID查询员工")
    @GetMapping("/{id}")
    public Result findEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findEmployeeById(id);
        
        return Result.success(employee);
    }
    
    @ApiOperation("修改员工信息")
    @PutMapping
    public Result updateEmployeeInfo(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployeeInfo(employeeDTO);
        return Result.success();
    }

    
    
}
