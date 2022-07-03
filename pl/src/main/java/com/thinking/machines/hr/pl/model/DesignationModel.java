package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.io.image.*;
import com.itextpdf.layout.borders.*;


import com.itextpdf.io.image.*;
import com.itextpdf.layout.property.*;



public class DesignationModel extends AbstractTableModel
{
private java.util.List<DesignationInterface> designations;
private String columnTitle[];
DesignationManagerInterface designationManager;

public DesignationModel()
{
populateDataStructure();
}

private void populateDataStructure()
{
columnTitle=new String[2];
columnTitle[0]="S.No.";
columnTitle[1]="Designation";
designations=new LinkedList<>();
Set<DesignationInterface> blDesignations=null;
try{
designationManager=DesignationManager.getDesignationManager();
blDesignations=designationManager.getDesignations();
}catch(BLException blException)
{
// ??????????? Something to be done
}
for(DesignationInterface designation : blDesignations) this.designations.add(designation);
Collections.sort(designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}

public int getRowCount()
{
return designations.size();
}

public int getColumnCount()
{
return this.columnTitle.length;
}

public String getColumnName(int columnIndex)
{
return this.columnTitle[columnIndex];
}

public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}

public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0) return rowIndex+1;
return designations.get(rowIndex).getTitle();
}

public Class getColumnClass(int columnIndex)
{
Class c=null;
try{
if(columnIndex==0) c=Class.forName("java.lang.Integer");
if(columnIndex==1) c=Class.forName("java.lang.String");
}catch(Exception e)
{
System.out.println(e);
}
return c;
}

public void add(DesignationInterface designation) throws BLException
{
designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}

public int indexOfDesignation(DesignationInterface designation) throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(d.equals(designation)) return index;
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid Designation : "+designation.getTitle());
throw blException;
}

public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d=null;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(partialLeftSearch)
{
if(d.getTitle().toUpperCase().startsWith(title.toUpperCase())) return index;
}
else 
{
if(d.getTitle().equalsIgnoreCase(title)) return index;
}
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid Designation : "+title);
throw blException;
}


public void update(DesignationInterface designation) throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));
this.designations.add(designation);
Collections.sort(designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}


public void remove(int code) throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d=null;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(code==d.getCode()) break;
index++;
}
if(index==this.designations.size()) 
{
BLException blException=new BLException();
blException.setGenericException("Invalid Code");
throw blException;
}
this.designations.remove(d);
fireTableDataChanged();
}


public void exportToPDF(File file) throws BLException
{
try{
if(file.exists()) file.delete();
PdfWriter pdfWriter=new PdfWriter(file);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument);

Table topTable,dataTable;
topTable=null;
dataTable=null;
float [] topTableColumnWidth={1,5};
float [] dataTableColumnWidth={1,5};
DesignationInterface designation;
Image logoIcon=new Image(ImageDataFactory.create(this.getClass().getResource("/icons/addIcon.png")));
Paragraph logoPara=new Paragraph();
logoPara.add(logoIcon);
Paragraph companyParagraph=new Paragraph("YouTube");
companyParagraph.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD));
companyParagraph.setFontSize(18);
Paragraph pageNumberParagraph=null;
Paragraph reportTitleParagraph=new Paragraph("List of Designations");
reportTitleParagraph.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD));
reportTitleParagraph.setFontSize(15);
Paragraph columnTitle1,columnTitle2;
columnTitle1=new Paragraph("S. No.");
columnTitle2=new Paragraph("Designations");
columnTitle1.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD));
columnTitle2.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD));
columnTitle1.setFontSize(14);
columnTitle2.setFontSize(14);
Paragraph dataParagraph=null;
int r=0;
int sno,pageSize;
sno=0;
pageSize=5;
boolean newPage=true;
int numberOfPages=this.designations.size()/5;

int pageNumber=0;
Table pageNumberTable=null;

if(numberOfPages%pageSize!=0) numberOfPages++;
while(r<this.designations.size())
{
Cell cell=new Cell();

if(newPage)
{
topTable=new Table(UnitValue.createPercentArray(topTableColumnWidth));
cell.setBorder(Border.NO_BORDER);
cell.add(logoPara);
topTable.addCell(cell);
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(companyParagraph);
cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
topTable.addCell(cell);
document.add(topTable); 

pageNumber++;
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
pageNumberParagraph=new Paragraph("Page : "+pageNumber+"/"+numberOfPages);
pageNumberParagraph.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN));
pageNumberParagraph.setFontSize(12);
cell.add(pageNumberParagraph);
pageNumberTable=new Table(1);
pageNumberTable.setWidth(UnitValue.createPercentValue(100));
pageNumberTable.addCell(cell);
document.add(pageNumberTable);

dataTable=new Table(UnitValue.createPercentArray(dataTableColumnWidth));
dataTable.setWidth(400);

cell=new Cell(1,2);
cell.add(reportTitleParagraph);
cell.setTextAlignment(TextAlignment.CENTER);
dataTable.addHeaderCell(cell);
dataTable.addHeaderCell(columnTitle1);
dataTable.addHeaderCell(columnTitle2);
newPage=false;
}
designation=this.designations.get(r);
sno++;
cell=new Cell();
dataParagraph=new Paragraph(String.valueOf(sno));
dataParagraph.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN));
dataParagraph.setFontSize(13);
cell.add(dataParagraph);
dataTable.addCell(cell);

cell=new Cell();
cell.add(new Paragraph(designation.getTitle()));
dataParagraph.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN));
dataParagraph.setFontSize(13);
dataTable.addCell(cell);
r++;
if(sno%pageSize==0 || r==this.designations.size())
{
// create footer
document.add(dataTable);
document.add(new Paragraph("Software by : Deepak Sharma"));
if(r<this.designations.size())
{
document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage=true;
}
}
}

document.close();
}catch(Exception exception)
{
BLException blException;
blException=new BLException();
blException.setGenericException(exception.getMessage());
throw blException;
}
}

 /*public void exportToPDF(File file)
{
int pageNumber=0;
int sno=0;
int pageSize=5;
int r=0;
boolean newPage=true;
while(r<designations.size())
{
if(newPage)
{
PdfWriter pdfWriter=new PdfWriter(new File(file.getAbsolutePath()));
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument);
Image image=new Image("saveIcon.png");
Paragraph p1=new Paragraph("Thinking Robots");
document.add(image).add(p1); 
newPage=false;
}
r++;
}
}
*/
public DesignationInterface getDesignationAt(int index) throws BLException
{
if(index<0 || index>this.designations.size())
{
BLException blException=new BLException();
blException.setGenericException("Invalid Index : "+index);
throw blException;
}
return this.designations.get(index);
}

}