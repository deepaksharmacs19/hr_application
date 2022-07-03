package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class DesignationDTO implements DesignationDTOInterface
{
private int code;
private String title;
public DesignationDTO()
{
this.code=0;
this.title="";
}
public void setCode(int code)
{
this.code=code;
}
public int getCode()
{
return this.code;
}
public void setTitle(String title)
{
this.title=title;
}
public String getTitle()
{
return this.title;
}
public boolean equals(Object other)
{
DesignationDTOInterface designationDTO;
if(!(other instanceof DesignationDTOInterface))return false;
designationDTO=(DesignationDTOInterface)other;
return this.code==designationDTO.getCode();
}
public int compareTo(DesignationDTOInterface other)
{
return this.code-other.getCode();
}
public int hashCode()
{
return this.code;
}
}