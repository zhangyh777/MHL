package service;

import dao.EmployeeDAO;
import domain.Employee;

/**
 * 完成对Employee表的操作（通过调用EmployeeDAO）
 */
public class EmployeeService {

    EmployeeDAO employeeDAO = new EmployeeDAO();

    /**
     * 根据id和pwd判断用户是否在数据库
     * @param empId
     * @param pwd
     * @return
     */
    public Employee getEmployeeByIdAndPwd(String empId,String pwd){
        String sql = "SELECT * FROM employee WHERE empId = ? AND pwd =md5(?)";
        Employee employee =
                employeeDAO.querySingle(sql,Employee.class,empId,pwd);
        return employee;
    }
}
