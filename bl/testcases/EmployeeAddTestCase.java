import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import java.util.*;
import java.text.*;
import java.math.*;

public class EmployeeAddTestCase
{
public static void main(String ff[])
{
try{
DesignationInterface designation=new Designation();
designation.setCode(3);
EmployeeInterface employee=new Employee();
employee.setName("SHREE GANESH");
employee.setDesignation(designation);
employee.setDateOfBirth(new Date());
employee.setGender(GENDER.MALE);
employee.setIsIndian(true);
employee.setBasicSalary(new BigDecimal(101013));
employee.setPANNumber("NSUPRCS34");
employee.setAadharNumber("101010156");
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employeeManager.addEmployee(employee);
System.out.println("Employee added with code as : "+employee.getEmployeeId());
}catch(BLException blException)
{
if(blException.hasGenericException()) System.out.println(blException.getGenericException());
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}
