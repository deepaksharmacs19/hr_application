package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;

public class EmployeeDAO implements EmployeeDAOInterface
{
private final static String FILE_NAME="Employee.data";

public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
int found=0;
if(employeeDTO==null) throw new DAOException("Invalid Input");
String EmployeeId=employeeDTO.getEmployeeId();
if(EmployeeId==null) throw new DAOException("Invalid Employee ID");
String name=employeeDTO.getName();
if(name.length()==0) throw new DAOException("Length of name is 0");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid Designation");
Date dateOfBirth=employeeDTO.getDateOfBirth();
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary.signum()==-1) throw new DAOException("Invalid Basic salary");
String AadharNumber=employeeDTO.getAadharNumber();
if(AadharNumber==null)throw new DAOException("Invalid Aadhar number");
if(AadharNumber.length()==0) throw new DAOException("Length Aadhar Number is 0");
String PANNumber=employeeDTO.getPANNumber();
if(PANNumber==null)throw new DAOException("Invalid PANNumber");
if(PANNumber.length()==0) throw new DAOException("length of PAN number is 0");
if(name.length()==0) throw new DAOException("Invalid Name");
String lastGeneratedEmployeeIdString,recordCountString;
try{
int lastGeneratedEmployeeId=10000000,recordCount=0;
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) 
{
lastGeneratedEmployeeIdString="A"+String.format("%-10s","10000000");
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
recordCountString=String.format("%-10s","0");
randomAccessFile.writeBytes(recordCountString+"\n");
}
else 
{
lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
}

boolean panNumberExists=false;
boolean aadharNumberExists=false;

String fAadharNumber,fPANNumber;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(int i=0;i<7;i++) randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharNumber=randomAccessFile.readLine();
if(aadharNumberExists==false && fAadharNumber.equalsIgnoreCase(AadharNumber))aadharNumberExists=true;
if(panNumberExists==false && fPANNumber.equalsIgnoreCase(PANNumber))panNumberExists=true;
if(panNumberExists && aadharNumberExists) break;
}

if(panNumberExists || aadharNumberExists)
{
randomAccessFile.close();
if(panNumberExists && aadharNumberExists) throw new DAOException("PAN number ("+PANNumber+") and Aadhar number ("+AadharNumber+") exists");
else if(aadharNumberExists) throw new DAOException("Aadhar ("+AadharNumber+") number exists");
else throw new DAOException("PAN ("+PANNumber+") number exists");
}

lastGeneratedEmployeeId++;
recordCount++;
EmployeeId="A"+String.format("%-10d",lastGeneratedEmployeeId); 

randomAccessFile.writeBytes(EmployeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
SimpleDateFormat simpleDateFormat;
simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(PANNumber+"\n");
randomAccessFile.writeBytes(AadharNumber+"\n");
// done done
randomAccessFile.seek(0);
lastGeneratedEmployeeIdString=String.format("%-10d",lastGeneratedEmployeeId);
recordCountString=String.format("%-10d",recordCount);
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.close();
employeeDTO.setEmployeeId(EmployeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
boolean found=false;
if(employeeDTO==null) throw new DAOException("Invalid Input");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("Invalid Employee ID");
if(employeeId.length()==0) throw new DAOException("Length of EmployeeId is zero");
String name=employeeDTO.getName();
if(name.length()==0) throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid Designation");
Date dateOfBirth=employeeDTO.getDateOfBirth();
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary.signum()==-1) throw new DAOException("Invalid Basic salary");
String aadharNumber=employeeDTO.getAadharNumber();
if(aadharNumber==null)throw new DAOException("Invalid Aadhar number");
if(aadharNumber.length()==0) throw new DAOException("Length Aadhar Number is 0");
String PANNumber=employeeDTO.getPANNumber();
if(PANNumber==null)throw new DAOException("Invalid PANNumber");
if(PANNumber.length()==0) throw new DAOException("length of PAN number is 0");
if(name.length()==0) throw new DAOException("Invalid Name");

try{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("File does not exists.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("File does not have any record.");
String fEmployeeId="";
String fPANNumber="";
String fAadharNumber="";
randomAccessFile.readLine();
randomAccessFile.readLine();

while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine().trim();
for(int i=0;i<8;i++)randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
found=true;
break;
}
}
if(!found)
{
randomAccessFile.close();
throw new DAOException("Record with employeeId "+employeeId+" does not exists to update.");
}
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.readLine();
boolean panNumberExists=false;
boolean aadharNumberExists=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine().trim();
for(int i=0;i<6;i++) randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharNumber=randomAccessFile.readLine();
if(fPANNumber.equalsIgnoreCase(PANNumber) && panNumberExists==false && employeeId.equalsIgnoreCase(fEmployeeId)==false)
{
panNumberExists=true;
}
if(fAadharNumber.equalsIgnoreCase(aadharNumber) && aadharNumberExists==false && employeeId.equalsIgnoreCase(fEmployeeId)==false)
{
aadharNumberExists=true;
}
if(aadharNumberExists && panNumberExists) break;
}

if(aadharNumberExists || panNumberExists)
{
randomAccessFile.close();
if(aadharNumberExists && panNumberExists)throw new DAOException("Aadhar Number and PAN Number already exists against another record.");
else if(aadharNumberExists) throw new DAOException("Aadhar Number Exists against another record.");
else throw new DAOException("PAN Number Exists against another record.");
}

randomAccessFile.seek(0);
File tmpFile=new File("tmp.data");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw"); 
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine().trim();
if(fEmployeeId.equalsIgnoreCase(employeeId)==false)
{
tmpRandomAccessFile.writeBytes(fEmployeeId+"\n");
for(int i=0;i<8;i++)tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
else{
tmpRandomAccessFile.writeBytes(employeeId+"\n");
tmpRandomAccessFile.writeBytes(name+"\n");
tmpRandomAccessFile.writeBytes(designationCode+"\n");
SimpleDateFormat simpleDateFormat;
simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
tmpRandomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
tmpRandomAccessFile.writeBytes(gender+"\n");
tmpRandomAccessFile.writeBytes(isIndian+"\n");
tmpRandomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
tmpRandomAccessFile.writeBytes(PANNumber+"\n");
tmpRandomAccessFile.writeBytes(aadharNumber+"\n");
for(int i=0;i<8;i++)randomAccessFile.readLine();
}
}
tmpRandomAccessFile.seek(0);
randomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");

randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
randomAccessFile.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}

}

public void delete(String EmployeeId) throws DAOException{
boolean found=false;

try{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("File does not exists.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("File does not have any record.");
String fEmployeeId="";
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());

while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine().trim();
for(int i=0;i<8;i++)randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(EmployeeId))
{
found=true;
break;
}
}
if(!found)
{
randomAccessFile.close();
throw new DAOException("Record with employeeId "+EmployeeId+" does not exists to update.");
}
String recordCountString=String.format("%-10d",(recordCount-1));

randomAccessFile.seek(0);
File tmpFile=new File("tmp.data");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw"); 
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
tmpRandomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine().trim();
if(fEmployeeId.equalsIgnoreCase(EmployeeId)==false)
{
tmpRandomAccessFile.writeBytes(fEmployeeId+"\n");
for(int i=0;i<8;i++)tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
else{
for(int i=0;i<8;i++) randomAccessFile.readLine();
continue;
}
}
tmpRandomAccessFile.seek(0);
randomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");

randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
randomAccessFile.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}

}

public Set<EmployeeDTOInterface> getAll() throws DAOException{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
char fGender=' ';
try{
File file=new File(FILE_NAME);
if(file.exists()==false) return employees; 
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employees;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(randomAccessFile.readLine()); 
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));

fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
if(fGender=='F') employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
}
catch(IOException ioe){
System.out.println(ioe.getMessage());
}catch(ParseException pe)
{

}
return employees;
}

public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException{
if(new DesignationDAO().codeExists(designationCode)==false)
{
throw new DAOException("Designation Code does not exists.");
}

Set<EmployeeDTOInterface> employees=new TreeSet<>();
try{
File file=new File(FILE_NAME);
if(file.exists()==false) return employees; 
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employees;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
String fName;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode!=designationCode)
{
for(int i=0;i<6;i++)randomAccessFile.readLine();
continue;
}
employeeDTO.setEmployeeId(fEmployeeId); 
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
char fGender=' ';
try{
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//Nothing.
}
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
if(fGender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
}catch(IOException ioe)
{
System.out.println(ioe.getMessage());
}
return employees;
}

public boolean isDesignationAlloted(int designationCode) throws DAOException{
if(new DesignationDAO().codeExists(designationCode)==false)
{
throw new DAOException("Designation Code does not exists.");
}

Boolean found=false;
try{
File file=new File(FILE_NAME);
if(file.exists()==false) return false; 
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
String fName;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode!=designationCode)
{
for(int i=0;i<6;i++)randomAccessFile.readLine();
continue;
}
else {
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
if(found) return true;
}catch(IOException ioe)
{
System.out.println(ioe.getMessage());
}
return false;
}


public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
try{
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid EmployeeId"+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) 
{
randomAccessFile.close();
throw new DAOException("Invalid EmployeeId"+employeeId);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId="";
long pos=0;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
pos=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine().trim();
for(int i=0;i<8;i++) randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
found=true;
break;
}
}

if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee Id");
}
randomAccessFile.seek(pos);
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
char fGender=' ';
try{
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//Nothing.
}

fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
if(fGender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharNumber(randomAccessFile.readLine());
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return employeeDTO;
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
try{
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid PAN Number : "+panNumber);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid PAN Number : "+panNumber);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String FPANNumber="";
long pos=0;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
pos=randomAccessFile.getFilePointer();
for(int i=0;i<7;i++) randomAccessFile.readLine();
FPANNumber=randomAccessFile.readLine();
if(FPANNumber.equalsIgnoreCase(panNumber))
{
found=true;
break;
}
randomAccessFile.readLine();
}

if(found==false)
{
randomAccessFile.close();
return null;
}
randomAccessFile.seek(pos);
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
char fGender=' ';
try{
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//Nothing.
}

fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
if(fGender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharNumber(randomAccessFile.readLine());
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return employeeDTO;
}

public EmployeeDTOInterface getByAadharNumber(String aadharNumber) throws DAOException{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
try{
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid Aadhar Number : "+aadharNumber);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("File does not have a single record");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String FAadharNumber="";
long pos=0;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
pos=randomAccessFile.getFilePointer();
for(int i=0;i<8;i++) randomAccessFile.readLine();
FAadharNumber=randomAccessFile.readLine();
if(FAadharNumber.equalsIgnoreCase(aadharNumber))
{
found=true;
break;
}
}

if(found==false)
{
randomAccessFile.close();
return null;
}
randomAccessFile.seek(pos);
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
char fGender=' ';
try{
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//Nothing.
}

fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
if(fGender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharNumber(randomAccessFile.readLine());
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return employeeDTO;
}

public boolean aadharNumberExists(String aadharNumber) throws DAOException{
if(aadharNumber==null) return false;
Boolean found=false;
try{
File file=new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw"); 
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
randomAccessFile.readLine();
String FAadharNumber="";
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(int i=0;i<8;i++) randomAccessFile.readLine();
FAadharNumber=randomAccessFile.readLine();
if(FAadharNumber.equalsIgnoreCase(aadharNumber))
{
found=true;
break;
}
}
if(!found)
{
randomAccessFile.close();
return false;
}

}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return true;
}

public boolean panNumberExists(String PANNumber) throws DAOException{
if(PANNumber==null) return false;
Boolean found=false;
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw"); 
randomAccessFile.readLine();
randomAccessFile.readLine();
String FPANNumber="";
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(int i=0;i<7;i++) randomAccessFile.readLine();
FPANNumber=randomAccessFile.readLine();
randomAccessFile.readLine();
if(FPANNumber.equalsIgnoreCase(PANNumber))
{
found=true;
break;
}
}
if(!found)
{
randomAccessFile.close();
return false;
}

}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return true;
}

public boolean employeeIdExists(String EmployeeId) throws DAOException{
if(EmployeeId==null) return false;
Boolean found=false;
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw"); 
randomAccessFile.readLine();
randomAccessFile.readLine();
String FemployeeId="";
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
FemployeeId=randomAccessFile.readLine().trim();
if(FemployeeId.equalsIgnoreCase(EmployeeId))
{
found=true;
break;
}
for(int i=0;i<=7;i++) randomAccessFile.readLine();
}
if(found==false)
{
randomAccessFile.close();
return false;
}

}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return true;
}

public int getCount() throws DAOException{
int recordCount=0;
try{
File file=new File(FILE_NAME);
if(file.exists()==false)return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return 0;
randomAccessFile.readLine();
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return recordCount;
}

public int getCountByDesignationCode(int designationCode) throws DAOException{
int count=0;
try{
File file=new File(FILE_NAME);
if(file.exists()==false) return 0; 
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return 0;
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
String fName;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
fEmployeeId=randomAccessFile.readLine().trim();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode) count++;
for(int i=0;i<6;i++)randomAccessFile.readLine();
}
randomAccessFile.close();
}catch(IOException ioe)
{
System.out.println(ioe.getMessage());
}
return count;
}

}