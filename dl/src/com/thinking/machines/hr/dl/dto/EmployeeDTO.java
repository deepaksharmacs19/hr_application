package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;

public class EmployeeDTO implements EmployeeDTOInterface
{
private String EmployeeId;
private String name;
private int designationCode;
private Date dateOfBirth;
private char gender;
private boolean isIndian;
BigDecimal basicSalary;
String PANNumber;
String AadharNumber;

public EmployeeDTO()
{
this.EmployeeId="";
this.name="";
this.designationCode=0;
this.dateOfBirth=null;
this.gender=' ';
this.isIndian=false;
this.basicSalary=null;
this.PANNumber="";
this.AadharNumber="";
}
public void setEmployeeId(java.lang.String EmployeeId)
{
this.EmployeeId=EmployeeId;
}
public java.lang.String getEmployeeId()
{
return this.EmployeeId;
}
public void setName(java.lang.String name)
{
this.name=name;
}
public java.lang.String getName()
{
return this.name;
}
public void setDesignationCode(int designationCode)
{
this.designationCode=designationCode;
}
public int getDesignationCode()
{
return this.designationCode;
}
public void setDateOfBirth(java.util.Date dateOfBirth)
{
this.dateOfBirth=dateOfBirth;
}
public java.util.Date getDateOfBirth()
{
return this.dateOfBirth;
}
public void setGender(GENDER gender)
{
if(gender==GENDER.MALE) this.gender='M';
if(gender==GENDER.FEMALE) this.gender='F';
}
public char getGender()
{
return this.gender;
}
public void setIsIndian(boolean isIndian)
{
this.isIndian=isIndian;
}
public boolean getIsIndian()
{
return this.isIndian;
}
public void setBasicSalary(java.math.BigDecimal basicSalary)
{
this.basicSalary=basicSalary;
}
public java.math.BigDecimal getBasicSalary()
{
return this.basicSalary;
}
public void setPANNumber(java.lang.String PANNumber)
{
this.PANNumber=PANNumber;
}
public java.lang.String getPANNumber()
{
return this.PANNumber;
}
public void setAadharNumber(java.lang.String AadharNumber)
{
this.AadharNumber=AadharNumber;
}
public java.lang.String getAadharNumber()
{
return this.AadharNumber;
}
public boolean equals(Object other)
{
if(!(other instanceof EmployeeDTOInterface)) return false;
EmployeeDTOInterface employeeDTO=(EmployeeDTO)other;
return this.EmployeeId.equals(employeeDTO.getEmployeeId());
}
public int compareTo(EmployeeDTOInterface other)
{
return this.EmployeeId.compareTo(other.getEmployeeId());
}
public int hashCode()
{
return this.EmployeeId.toUpperCase().hashCode();
}
}