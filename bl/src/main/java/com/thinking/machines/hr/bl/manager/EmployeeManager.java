package com.thinking.machines.hr.bl.manager;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.text.*;
import java.math.*;
import java.util.*;

public class EmployeeManager implements EmployeeManagerInterface
{
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharNumberWiseEmployeesMap;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
private Set<EmployeeInterface> employeeSet;

private static EmployeeManager employeeManager=null;
private EmployeeManager() throws BLException
{
populateDataStructures();
}
public static EmployeeManagerInterface getEmployeeManager() throws BLException
{
if(employeeManager==null) employeeManager=new EmployeeManager();
return employeeManager;
}

private void populateDataStructures() throws BLException
{
this.employeeIdWiseEmployeesMap=new HashMap<>();
this.panNumberWiseEmployeesMap=new HashMap<>();
this.aadharNumberWiseEmployeesMap=new HashMap<>();
this.designationCodeWiseEmployeesMap=new HashMap<>();
this.employeeSet=new TreeSet<>();
try{
Set<EmployeeDTOInterface> dlEmployees;
Set<EmployeeInterface> ets;
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface employee;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
for(EmployeeDTOInterface dlEmployee : dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(dlEmployee.getEmployeeId().trim());
employee.setName(dlEmployee.getName());
designation=designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)dlEmployee.getDateOfBirth().clone());
if(dlEmployee.getGender()=='M') employee.setGender(GENDER.MALE);
else employee.setGender(GENDER.FEMALE);
employee.setIsIndian(dlEmployee.getIsIndian());
employee.setBasicSalary(dlEmployee.getBasicSalary());
employee.setPANNumber(dlEmployee.getPANNumber());
employee.setAadharNumber(dlEmployee.getAadharNumber());

this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
this.aadharNumberWiseEmployeesMap.put(employee.getAadharNumber().toUpperCase(),employee);
this.employeeSet.add(employee);
ets=designationCodeWiseEmployeesMap.get(designation.getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(employee);
designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
}
else ets.add(employee);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public void addEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
if(employee==null) 
{
blException.addException("Employee","Employee required");
throw blException;
}
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharNumber=employee.getAadharNumber();
if(employeeId!=null)
{
if(employeeId.trim().length()>0) blException.addException("employeeId","Employee Id should be empty");
}
if(name==null)
{
blException.addException("name","Name required");
}
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","Name required");
}
if(designation==null) blException.addException("designation","Designation required :  ");
else
{
designationCode=designation.getCode();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false) blException.addException("designation","Invalid designation");
}
if(dateOfBirth==null) blException.addException("dateofbirth","Date Of Birth required");
if(gender==' ') blException.addException("gender","Gender required");
if(basicSalary==null) blException.addException("basic salary","Basic Salary required");
if(basicSalary.signum()==-1) blException.addException("basic salary","Basic Salary required");
if(panNumber==null) blException.addException("pan number","PAN Number required");
else {
panNumber=panNumber.trim();
if(panNumber.length()==0) blException.addException("pan number","PAN Number required");
}
if(aadharNumber==null) blException.addException("aadhar number","Aadhar Number required");
else{
aadharNumber=aadharNumber.trim();
if(aadharNumber.length()==0) blException.addException("aadhar number","Aadhar Number required");
}

if(this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())==true) blException.addException("pan number","PAN Number exists : "+ employee.getPANNumber());
if(this.aadharNumberWiseEmployeesMap.containsKey(aadharNumber.toUpperCase())==true) blException.addException("aadhar number","Aadhar Number exists : "+employee.getAadharNumber());

if(blException.hasExceptions()) throw blException;
Set<EmployeeInterface> ets;
try{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTO dlEmployee=new EmployeeDTO();
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth(dateOfBirth);
if(gender=='M')dlEmployee.setGender(GENDER.MALE);
else dlEmployee.setGender(GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharNumber(aadharNumber);
employeeDAO.add(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());

EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();

dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
if(employee.getGender()=='M')dsEmployee.setGender(GENDER.MALE);
if(employee.getGender()=='F')dsEmployee.setGender(GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharNumber(employee.getAadharNumber());
this.employeeSet.add(dsEmployee);
this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
this.panNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(),dsEmployee);
this.aadharNumberWiseEmployeesMap.put(dsEmployee.getAadharNumber().toUpperCase(),dsEmployee);
ets=designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else{
ets.add(dsEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}


public void updateEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
if(employee==null) 
{
blException.addException("Employee","Employee required");
throw blException;
}
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharNumber=employee.getAadharNumber();
if(employeeId==null)
{
blException.addException("employeeid","EmployeeId required");
}
else
{
employeeId=employeeId.trim();
if(employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
blException.addException("employeeid","Invalid EmployeeId : "+employeeId+"DS employeeId : "+employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()));
throw blException;
}
}
//done done
if(name==null)
{
blException.addException("name","Name required");
}
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","Name required");
}
if(designation==null) blException.addException("designation","Designation required :  ");
else
{
designationCode=designation.getCode();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false) blException.addException("designation","Invalid designation");
}
if(dateOfBirth==null) blException.addException("dateofbirth","Date Of Birth required");
if(gender==' ') blException.addException("gender","Gender required");
if(basicSalary==null) blException.addException("basic salary","Basic Salary required");
if(basicSalary.signum()==-1) blException.addException("basic salary","Basic Salary required");
if(panNumber==null) blException.addException("pan number","PAN Number required");
else {
panNumber=panNumber.trim();
if(panNumber.length()==0) blException.addException("pan number","PAN Number required");
}
if(aadharNumber==null) blException.addException("aadhar number","Aadhar Number required");
else{
aadharNumber=aadharNumber.trim();
if(aadharNumber.length()==0) blException.addException("aadhar number","Aadhar Number required");
}

if(this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())==true)
{
EmployeeInterface e=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(e!=null && employeeId.equalsIgnoreCase(e.getEmployeeId())==false)
{
blException.addException("pan number","PAN Number Exists : "+ employee.getPANNumber());
}
}

if(this.aadharNumberWiseEmployeesMap.containsKey(aadharNumber.toUpperCase())==true)
{
EmployeeInterface e=aadharNumberWiseEmployeesMap.get(aadharNumber.toUpperCase());
if(e!=null && employeeId.equalsIgnoreCase(e.getEmployeeId())==false)
{
blException.addException("aadharnumber","Aadhar Number Exists : "+ employee.getAadharNumber());
}
}

if(blException.hasExceptions()) throw blException;

try{
EmployeeInterface dsEmployee;
dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String oldPANNumber=dsEmployee.getPANNumber();
String oldAadharNumber=dsEmployee.getAadharNumber();
int oldDesignationCode=dsEmployee.getDesignation().getCode();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTO dlEmployee=new EmployeeDTO();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth(dateOfBirth);
if(gender=='M')dlEmployee.setGender(GENDER.MALE);
else dlEmployee.setGender(GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharNumber(aadharNumber);
employeeDAO.update(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());

dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
if(employee.getGender()=='M')dsEmployee.setGender(GENDER.MALE);
if(employee.getGender()=='F')dsEmployee.setGender(GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharNumber(employee.getAadharNumber());
this.employeeSet.remove(dsEmployee);
this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
this.panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
this.aadharNumberWiseEmployeesMap.remove(oldAadharNumber.toUpperCase());

this.employeeSet.add(dsEmployee);
this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
this.panNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(),dsEmployee);
this.aadharNumberWiseEmployeesMap.put(dsEmployee.getAadharNumber().toUpperCase(),dsEmployee);
//done done

if(oldDesignationCode!=dsEmployee.getDesignation().getCode())
{
Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(oldDesignationCode);
ets.remove(dsEmployee);
ets=designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else{
ets.add(dsEmployee);
}
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}

public void removeEmployee(String employeeId) throws BLException
{
BLException blException=new BLException();
if(employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false) 
{
blException.addException("employeeid","Invalid EmployeeId");
throw blException;
}

if(employeeId==null)
{
blException.addException("employeeid","Invalid EmployeeId");
throw blException;
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0) 
{
blException.addException("employeeid","Invalid EmployeeId");
throw blException;
}
}
try{
EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String panNumber=dsEmployee.getPANNumber();
String aadharNumber=dsEmployee.getAadharNumber();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);

employeeSet.remove(dsEmployee);
employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(panNumber.toUpperCase());
aadharNumberWiseEmployeesMap.remove(aadharNumber.toUpperCase());
Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
ets.remove(dsEmployee);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}

public EmployeeInterface getEmployeeByEmployeeId(String EmployeeId) throws BLException
{
BLException blException=new BLException();
if(EmployeeId==null) 
{
blException.addException("employeeid","Invalid EmployeeId");
throw blException;
}
EmployeeInterface dsEmployee=employeeIdWiseEmployeesMap.get(EmployeeId.toUpperCase());
EmployeeInterface employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
DesignationInterface dsDesignation=dsEmployee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharNumber(dsEmployee.getAadharNumber());
return employee;
}

public EmployeeInterface getEmployeeByPANNumber(String PANNumber) throws BLException{
BLException blException=new BLException();
if(PANNumber==null) 
{
blException.addException("pannumber","Invalid PANNumber");
throw blException;
}
EmployeeInterface dsEmployee=panNumberWiseEmployeesMap.get(PANNumber.toUpperCase());
if(dsEmployee==null) 
{
blException.addException("pannumber","PAN Number required");
}
EmployeeInterface employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
DesignationInterface dsDesignation=dsEmployee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharNumber(dsEmployee.getAadharNumber());
return employee;
}

public EmployeeInterface getEmployeeByaadharNumber(String aadharNumber) throws BLException{
BLException blException=new BLException();
if(aadharNumber==null) 
{
blException.addException("aadharnumber","Invalid Aadhar Number");
throw blException;
}
EmployeeInterface dsEmployee=aadharNumberWiseEmployeesMap.get(aadharNumber.toUpperCase());
if(dsEmployee==null) 
{
blException.addException("aadharnumber","Aadhar Number required");
}
EmployeeInterface employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
DesignationInterface dsDesignation=dsEmployee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharNumber(dsEmployee.getAadharNumber());
return employee;
}

public int getEmployeeCount()  throws BLException{
return this.employeeIdWiseEmployeesMap.size();
}

public boolean employeeIdExists(String EmployeeId){
return this.employeeIdWiseEmployeesMap.containsKey(EmployeeId.toUpperCase());
}

public boolean employeePANNumberExists(String PANNumber){
return this.panNumberWiseEmployeesMap.containsKey(PANNumber.toUpperCase());
}

public boolean employeeAadharNumberExists(String aadharNumber){
return this.aadharNumberWiseEmployeesMap.containsKey(aadharNumber.toUpperCase());
}

public Set<EmployeeInterface> getEmployees() throws BLException{
Set<EmployeeInterface> employees=new TreeSet<>();
if(this.employeeSet.size()==0) return employees;
for(EmployeeInterface employee : this.employeeSet)
{
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationInterface dsDesignation=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
dsEmployee.setDesignation(designation);
dsEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
if(employee.getGender()=='M')dsEmployee.setGender(GENDER.MALE);
if(employee.getGender()=='F')dsEmployee.setGender(GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharNumber(employee.getAadharNumber());

employees.add(dsEmployee);
}
return employees;
}

public int getEmployeeCountByDesignationCode(int designationCode) throws BLException
{
if(designationCode<0) return 0;
Set<EmployeeInterface> ets=designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null) return 0;
return ets.size();
}

public boolean designationAlloted(int designationCode) throws BLException{
return designationCodeWiseEmployeesMap.containsKey(designationCode);
}

public Set<EmployeeInterface> getEmployeesByDesignation(int designationCode) throws BLException
{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false)
{
BLException blException=new BLException();
blException.setGenericException("Invalid designation code");
throw blException;
}
Set<EmployeeInterface> employees=new TreeSet<>();
if(this.designationCodeWiseEmployeesMap.size()==0) return employees;
Set<EmployeeInterface> dlEmployee=designationCodeWiseEmployeesMap.get(designationCode);
if(dlEmployee==null) return employees;

for(EmployeeInterface employee : dlEmployee)
{
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationInterface dsDesignation=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
dsEmployee.setDesignation(designation);
dsEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
if(employee.getGender()=='M')dsEmployee.setGender(GENDER.MALE);
if(employee.getGender()=='F')dsEmployee.setGender(GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharNumber(employee.getAadharNumber());

employees.add(dsEmployee);
}
return employees;
}
}