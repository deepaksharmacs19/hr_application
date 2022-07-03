package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dto.*;
import java.util.*;
import java.io.*;

public class DesignationDAO implements DesignationDAOInterface
{
public final String FILE_NAME="Designation.data";
public void add(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null");
String title=designationDTO.getTitle();
if(title==null) throw new DAOException("Designation is null");
title=title.trim();
if(title.length()==0) throw new DAOException("Title's Length is Zero.");
try{
int lastGeneratedCode;
int recordCount;
String lastGeneratedCodeString="";
String recordCountString="";
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
lastGeneratedCode=0;
recordCount=0;
lastGeneratedCodeString="0";
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString="0";
while(recordCountString.length()<10) recordCountString+=" ";

randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
}
else
{
lastGeneratedCodeString=randomAccessFile.readLine();
recordCountString=randomAccessFile.readLine();
lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString.trim());
recordCount=Integer.parseInt(recordCountString.trim());
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine().trim());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException("Title already exists.");
}
}
int code=lastGeneratedCode+1;
randomAccessFile.writeBytes(String.valueOf(code));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(title);
randomAccessFile.writeBytes("\n");
designationDTO.setCode(code);
lastGeneratedCode++;
recordCount++;
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
while(lastGeneratedCodeString.length()<10)lastGeneratedCodeString+=" ";
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
System.out.println("Title added with the code : "+code);
randomAccessFile.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
}


public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Invalid Input");
if(designationDTO.getCode()<0) throw new DAOException("Invalid Input");
try{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("File does not exist");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records found");
randomAccessFile.readLine();
randomAccessFile.readLine();
int code=designationDTO.getCode();
String title=designationDTO.getTitle();
int found=0;
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
randomAccessFile.readLine();
if(fCode==code)
{
found=1;
break;
}
}
if(found==0)
{
randomAccessFile.close();
throw new DAOException("Code does not exists.");
}
found=0;
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
found=1;
break;
}
}

if(found==1)
{
randomAccessFile.close();
throw new DAOException("Title already exists.");
}

File tmpFile=new File("tmp.data");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");

while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode==code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(designationDTO.getTitle());
tmpRandomAccessFile.writeBytes("\n");
}
else
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(fTitle);
tmpRandomAccessFile.writeBytes("\n");
}
}

tmpRandomAccessFile.seek(0);
randomAccessFile.seek(0);

while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
}

randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
randomAccessFile.close();
System.out.println("Record updated");
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
}


public void delete(int code) throws DAOException
{
if(code<0) throw new DAOException("Invalid code.");
try{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("File does not exists.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records found");
randomAccessFile.readLine();
randomAccessFile.readLine();
int found=0;
int fCode;
String fTitle="";
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode==code)
{
found=1;
break;
}
}

if(found==0)
{
randomAccessFile.close();
throw new DAOException("Code does not exists.");
}

if(new EmployeeDAO().isDesignationAlloted(code))
{
randomAccessFile.close();
throw new DAOException("Designation "+fTitle+" alloted.");
}
File tmpFile=new File("tmp.data");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
String recordCountString=randomAccessFile.readLine().trim();
int recordCount=Integer.parseInt(recordCountString)-1;
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";

tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(recordCountString);
tmpRandomAccessFile.writeBytes("\n");

while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode==code)
{
continue;
}
else
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(fTitle);
tmpRandomAccessFile.writeBytes("\n");
}
}

tmpRandomAccessFile.seek(0);
randomAccessFile.seek(0);

while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
}

randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
randomAccessFile.close();
System.out.println("Record with code : "+code+" is Deleted");
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
}

public Set<DesignationDTOInterface> getAll() throws DAOException

{
Set<DesignationDTOInterface> designations=new TreeSet<>();
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return designations;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(Integer.parseInt(randomAccessFile.readLine()));
designationDTO.setTitle(randomAccessFile.readLine());
designations.add(designationDTO);
}
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
return designations;
}

public DesignationDTOInterface getByCode(int code) throws DAOException
{
String fTitle="";
int fCode=0;
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records in the file");
String lastGeneratedCodeString=randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine();

int lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString.trim());
int recordCount=Integer.parseInt(recordCountString.trim());

if(code>lastGeneratedCode || code<=0) throw new DAOException("Invalid code is given");
int i,found=0;
i=0;
while(i<recordCount)
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(code==fCode)
{
found=1;
break;
}
i++;
}

if(found==0) throw new DAOException("Record with the given code does not exists.");
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
DesignationDTOInterface designationDTOInterface=new DesignationDTO();
designationDTOInterface.setCode(code);
designationDTOInterface.setTitle(fTitle);
return designationDTOInterface;
}
public DesignationDTOInterface getByTitle(String title) throws DAOException
{
String fTitle="";
int fCode=0;
String tTitle="";
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records in the file");
String lastGeneratedCodeString=randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine();
int lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString.trim());
int recordCount=Integer.parseInt(recordCountString.trim());

if(title==null) throw new DAOException("Invalid title is given");
tTitle=title.trim();
if(tTitle==null) throw new DAOException("Invalid title is given");

int i,found=0;
i=0;
while(i<recordCount)
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(tTitle.equalsIgnoreCase(fTitle))
{
found=1;
break;
}
i++;
}

if(found==0) throw new DAOException("Record with the given title does not exists.");
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
DesignationDTOInterface designationDTOInterface=new DesignationDTO();
designationDTOInterface.setCode(fCode);
designationDTOInterface.setTitle(fTitle);
return designationDTOInterface;
}
public boolean titleExists(String title) throws DAOException
{
int found=0;
try{
String tTitle="";
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records in the file");
randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine();
int recordCount=Integer.parseInt(recordCountString.trim());

if(title==null) throw new DAOException("Invalid title is given");
tTitle=title.trim();
if(tTitle==null) throw new DAOException("Invalid title is given");

String fTitle="";
int i;
i=0;
while(i<recordCount)
{
randomAccessFile.readLine();
fTitle=randomAccessFile.readLine();
if(tTitle.equalsIgnoreCase(fTitle))
{
found=1;
break;
}
i++;
}
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
if(found==0) return false;
else return true;
}


public boolean codeExists(int code) throws DAOException
{
int found=0;
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records in the file");
randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine();
int recordCount=Integer.parseInt(recordCountString.trim());
int i=0;
int fCode=0;
while(i<recordCount)
{
fCode=Integer.parseInt(randomAccessFile.readLine());
randomAccessFile.readLine();
if(code==fCode)
{
found=1;
break;
}
i++;
}
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
if(found==1) return true;
else return false;
}
public int getCount() throws DAOException
{
int total=0;
try{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("No records in the file");
randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine();
int recordCount=Integer.parseInt(recordCountString.trim());
total=recordCount;
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return total;
}

}