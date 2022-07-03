package com.thinking.machines.hr.bl.manager;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;

public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationMap;
private Map<String,DesignationInterface> titleWiseDesignationMap;
private Set<DesignationInterface> designationSet;
private static DesignationManager designationManager=null;
private DesignationManager() throws BLException
{
populateDataStructures();
}
public static DesignationManagerInterface getDesignationManager() throws BLException
{
if(designationManager==null) designationManager=new DesignationManager();
return designationManager;
}

private void populateDataStructures() throws BLException
{
this.codeWiseDesignationMap=new HashMap<>();
this.titleWiseDesignationMap=new HashMap<>();
this.designationSet=new TreeSet<>();
try{
Set<DesignationDTOInterface> dlDesignations;
dlDesignations=new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface dlDesignation : dlDesignations)
{
designation=new Designation();
designation.setCode(dlDesignation.getCode());
designation.setTitle(dlDesignation.getTitle());
this.codeWiseDesignationMap.put(new Integer(designation.getCode()),designation);
this.titleWiseDesignationMap.put(designation.getTitle().toUpperCase(),designation);
this.designationSet.add(designation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void addDesignation(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code!=0) blException.addException("code","Code should be zero");
if(title==null)
{
blException.addException("title","Title required");
title="";
}
else
{
title=title.trim();
if(title.length()==0) blException.addException("title","Title required");
}
if(title.length()>0)
{
if(this.titleWiseDesignationMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Designation "+ title+" already exists");
}
}

if(blException.hasExceptions())throw blException;

try{
Designation dsDesignation=new Designation();
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setTitle(title);
dsDesignation.setTitle(title);
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.add(designationDTO);
code=designationDTO.getCode();
designationDTO.setCode(code);
dsDesignation.setCode(code);
this.codeWiseDesignationMap.put(new Integer(code),dsDesignation);
this.titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
this.designationSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}

public void updateDesignation(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();

if(code<0)
{
blException.addException("code","Code should not be negative");
throw blException;
}
if(code>0)
{
if(codeWiseDesignationMap.containsKey(code)==false)
{
blException.addException("code","Invalid code "+code);
throw blException;
}
}

if(title==null)
{
blException.addException("title","Title required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)blException.addException("title","Title required");
}

if(title.length()>0)
{
DesignationInterface d;
d=this.titleWiseDesignationMap.get(title.toUpperCase());
if(d!=null && d.getCode()!=code)
{
blException.addException("title","Designation "+title+" Exists");
throw blException;
}
}


if(blException.hasExceptions())throw blException;

try{
DesignationInterface dsDesignation=codeWiseDesignationMap.get(code);
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(code);
designationDTO.setTitle(title);

DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.update(designationDTO);
// Removing the old record
codeWiseDesignationMap.remove(dsDesignation.getCode());
titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
designationSet.remove(dsDesignation);
// updating the ds object
dsDesignation.setTitle(title);

// updating the object into ds
codeWiseDesignationMap.put(code,dsDesignation);
titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
designationSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}


public void removeDesignation(int code) throws BLException
{
BLException blException=new BLException();
if(code<0)
{
blException.addException("code","Code should not be negative");
throw blException;
}
if(code>0)
{
if(codeWiseDesignationMap.containsKey(code)==false)
{
blException.addException("code","Invalid code "+code);
throw blException;
}
}


if(blException.hasExceptions())throw blException;

try{
DesignationInterface dsDesignation=codeWiseDesignationMap.get(code);
new DesignationDAO().delete(code);
// Removing the old records from DS
codeWiseDesignationMap.remove(code);
titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
designationSet.remove(dsDesignation);

}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

DesignationInterface getDSDesignationByCode(int code)
{
DesignationInterface designation=codeWiseDesignationMap.get(code);
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}


public DesignationInterface getDesignationByCode(int code) throws BLException
{
DesignationInterface designation=codeWiseDesignationMap.get(code);
if(designation==null)
{
BLException blException=new BLException();
blException.addException("designation","Designation required");
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}
public DesignationInterface getDesignationByTitle(String title) throws BLException
{
DesignationInterface designation=titleWiseDesignationMap.get(title.toUpperCase());
if(designation==null)
{
BLException blException=new BLException();
blException.addException("designation","Designation required");
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}

public int getDesignationCount() throws BLException
{
return this.designationSet.size();
}

public boolean designationCodeExists(int code) throws BLException
{
return codeWiseDesignationMap.containsKey(code);
}

public boolean designationTitleExists(String title) throws BLException
{
return titleWiseDesignationMap.containsKey(title.toUpperCase());
}

public Set<DesignationInterface> getDesignations() throws BLException
{
Set<DesignationInterface> designations=new TreeSet<>();
this.designationSet.forEach((designation)->{
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
designations.add(d);
});
return designations;
}

}