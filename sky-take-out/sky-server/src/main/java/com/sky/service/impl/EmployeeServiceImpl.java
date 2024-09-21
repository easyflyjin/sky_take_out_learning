package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        System.out.println("当前线程的ID："+Thread.currentThread().getId());
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账户状态 默认状态1启用。
        //状态0锁定
        employee.setStatus(StatusConstant.ENABLE);
        String defaultPassword = PasswordConstant.DEFAULT_PASSWORD;
        employee.setPassword(DigestUtils.md5DigestAsHex(defaultPassword.getBytes()));
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());

        // employee.setCreateUser(BaseContext.getCurrentId());
        // employee.setUpdateUser(BaseContext.getCurrentId());
        
        employeeMapper.employeeInsert(employee);

    }

    @Transactional
    @Override
    public PageResult findEmployeeByCategory(EmployeePageQueryDTO employeePageQueryDTO) {
        employeeMapper.getByUsername(employeePageQueryDTO.getName());

        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> p = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = p.getTotal();
        List<Employee> records = p.getResult();
        PageResult pageResult = new PageResult();
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    public void setStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
        .status(status)
        .id(id)
        .build();
        
        employeeMapper.updateEmployee(employee);
    }


    @Override
    public Employee findEmployeeById(Long id) {

        Employee emp = employeeMapper.getById(id);
        emp.setPassword("****");
        return emp;
    }


    @Override
    public void updateEmployeeInfo(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        Long id = BaseContext.getCurrentId();
        employee.setUpdateUser(id);
        employee.setUpdateTime(LocalDateTime.now());

        employeeMapper.updateEmployee(employee);
    }


}
