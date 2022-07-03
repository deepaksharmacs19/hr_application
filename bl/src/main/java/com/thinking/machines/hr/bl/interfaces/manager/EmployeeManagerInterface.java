package com.thinking.machines.hr.bl.interfaces.manager;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;

public interface EmployeeManagerInterface
{
public void addEmployee(EmployeeInterface employee) throws BLException;
public void updateEmployee(EmployeeInterface employee) throws BLException;
public void removeEmployee(String EmployeeId) throws BLException;
public EmployeeInterface getEmployeeByEmployeeId(String EmployeeId) throws BLException;
public EmployeeInterface getEmployeeByPANNumber(String PANNumber) throws BLException;
public EmployeeInterface getEmployeeByaadharNumber(String aadharNumber) throws BLException;
public int getEmployeeCount()  throws BLException;
public boolean employeeIdExists(String EmployeeId) throws BLException;
public boolean employeePANNumberExists(String PANNumber);
public boolean employeeAadharNumberExists(String aadharNumber);
public Set<EmployeeInterface> getEmployeesByDesignation(int designationCode) throws BLException;
public Set<EmployeeInterface> getEmployees() throws BLException;
public int getEmployeeCountByDesignationCode(int designationCode) throws BLException;
public boolean designationAlloted(int designationCode) throws BLException;
}