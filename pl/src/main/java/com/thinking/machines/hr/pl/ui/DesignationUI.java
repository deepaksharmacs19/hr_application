package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.pl.model.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
//import javax.swing.FileChooser.*;

public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private JLabel titleLabel,searchLabel,errorLabel;
private JTextField searchTextField;
private JButton closeButton;
private JTable designationTable;
private JScrollPane scrollPane;
private Container container;
private DesignationModel designationModel;
private DesignationPanel designationPanel;
private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF};
private MODE mode;
public DesignationUI()
{
InitComponents();
setAppearance();
addListeners();
setViewMode();
designationPanel.setViewMode();
}

private void InitComponents()
{
designationModel=new DesignationModel();
designationTable=new JTable(designationModel);
scrollPane=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
searchLabel=new JLabel("Search");
searchTextField=new JTextField(20);
titleLabel=new JLabel("Designations");
closeButton=new JButton(new ImageIcon("closeIcon.png"));
designationPanel=new DesignationPanel();
errorLabel=new JLabel("");
container=getContentPane();
container.add(scrollPane);
setLocation(34,45);
setSize(400,500);
}

public void setAppearance()
{
Font titleFont=new Font("Lucida",Font.BOLD,18);
Font textField=new Font("Lucida",Font.PLAIN,16);
Font searchFont=new Font("Lucida",Font.BOLD,16);
Font errorFont=new Font("Lucida",Font.BOLD,12);
designationTable.setFont(textField);
designationTable.setRowHeight(35);
designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(400);
designationTable.getTableHeader().setReorderingAllowed(false);
designationTable.getTableHeader().setResizingAllowed(false);
designationTable.setRowSelectionAllowed(true);
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
errorLabel.setForeground(Color.RED);
JTableHeader header=designationTable.getTableHeader();
header.setFont(searchFont);
titleLabel.setFont(titleFont);
searchTextField.setFont(textField);
searchLabel.setFont(searchFont);
errorLabel.setFont(errorFont);
container.setLayout(null);
int lm,tm;
lm=0;
tm=0;

titleLabel.setBounds(lm+10,tm+10,200,40); 
searchLabel.setBounds(lm+10,tm+50+10,100,40);
searchTextField.setBounds(lm+100,tm+65,400,30);
closeButton.setBounds(lm+505,tm+65,20,30);
scrollPane.setBounds(lm+10,tm+100,565,300);
designationPanel.setBounds(lm+10,tm+400+10,565,200);
errorLabel.setBounds(lm+440,tm+38,100,40);
container.add(titleLabel);
container.add(searchLabel);
 container.add(searchTextField);
container.add(closeButton);
container.add(scrollPane);
container.add(designationPanel);
container.add(errorLabel);
int h,b;
b=600;
h=670;
setSize(b,h);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation((d.width/2)-(b/2),(d.height/2)-(h/2));
setDefaultCloseOperation(EXIT_ON_CLOSE);
}

public void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
closeButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
searchTextField.setText("");
searchTextField.requestFocus();
}
});
designationTable.getSelectionModel().addListSelectionListener(this);
}

public void valueChanged(ListSelectionEvent ev)
{
int index=designationTable.getSelectedRow();
try{
DesignationInterface designation=designationModel.getDesignationAt(index);
designationPanel.setDesignation(designation);
}catch(BLException blException)
{
designationPanel.clearDesignation();
}
}

public void searchDesignation()
{
errorLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0) return;
int rowIndex=0;
try{
rowIndex=designationModel.indexOfTitle(title,true);
}catch(BLException blException)
{
errorLabel.setText("Not Found");
return;
}

designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
}

public void insertUpdate(DocumentEvent ev)
{
searchDesignation();
}
public void removeUpdate(DocumentEvent ev)
{
searchDesignation();
}
public void changedUpdate(DocumentEvent ev)
{
searchDesignation();
}

private void setViewMode()
{
if(designationTable.getRowCount()==0)
{
designationTable.setEnabled(false);
searchTextField.setEnabled(false);
closeButton.setEnabled(false);
}
else 
{
designationTable.setEnabled(true);
searchTextField.setEnabled(true);
closeButton.setEnabled(true);
}
}

private void setEditMode()
{
designationTable.setEnabled(false);
searchTextField.setEnabled(false);
closeButton.setEnabled(false);
}

private void setAddMode()
{
designationTable.setEnabled(false);
searchTextField.setEnabled(false);
closeButton.setEnabled(false);
}

private void setDeleteMode()
{
designationTable.setEnabled(true);
searchTextField.setEnabled(true);
closeButton.setEnabled(true);
}

public void setExportToPDFMode()
{
designationTable.setEnabled(false);
searchTextField.setEnabled(false);
closeButton.setEnabled(false);
}


//inner class starts here


class DesignationPanel extends JPanel
{
private JLabel designationTitleLabel,titleLabel;
private boolean isCompleted=false;
private JTextField titleTextField;
private JButton addButton,editButton,deleteButton,cancelButton,exportToPDFButton;
private JButton clearTextFieldButton;
private JPanel buttonPanels;
private DesignationInterface designation;
private ImageIcon saveIcon;
private ImageIcon editIcon;
private ImageIcon logoIcon;
private ImageIcon cancelIcon;
private ImageIcon deleteIcon;
private ImageIcon exportToPDFIcon;
private ImageIcon addIcon;

DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
initComponents();
setAppearance();
addListeners();
}

public void initComponents()
{
logoIcon=new ImageIcon(this.getClass().getResource("/icons/logoIcon.png"));
setIconImage(logoIcon.getImage());
editIcon=new ImageIcon(this.getClass().getResource("/icons/editIcon.png"));
deleteIcon=new ImageIcon(this.getClass().getResource("/icons/deleteIcon.png"));
cancelIcon=new ImageIcon(this.getClass().getResource("/icons/cancelIcon.png"));
addIcon=new ImageIcon(this.getClass().getResource("/icons/addIcon.png"));
designationTitleLabel=new JLabel("Designation");
titleLabel=new JLabel("");
titleTextField=new JTextField(20);
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
deleteButton=new JButton(deleteIcon);
cancelButton=new JButton(cancelIcon);
exportToPDFButton=new JButton(new ImageIcon("exportToPDFIcon.png"));
clearTextFieldButton=new JButton("closeIcon.png");
}

public void setAppearance()
{
setLayout(null);
designationTitleLabel.setFont(new Font("Lucida",Font.BOLD,18));
titleLabel.setFont(new Font("Lucida",Font.PLAIN,14));

designationTitleLabel.setBounds(10,20,120,20);
titleLabel.setBounds(130,20,200,20);
clearTextFieldButton.setBounds(460,20,25,25);
titleTextField.setBounds(150,20,300,25);

buttonPanels=new JPanel();
buttonPanels.setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
buttonPanels.setBounds(50,75,465,100);
addButton.setBounds(50,25,50,50);
editButton.setBounds(130,25,50,50);
deleteButton.setBounds(210,25,50,50);
cancelButton.setBounds(290,25,50,50);


exportToPDFButton.setBounds(370,25,50,50);
buttonPanels.setLayout(null);
buttonPanels.add(addButton);
buttonPanels.add(editButton);
buttonPanels.add(deleteButton);
buttonPanels.add(cancelButton);
buttonPanels.add(exportToPDFButton);

add(designationTitleLabel);
add(titleLabel);
add(titleTextField);
add(clearTextFieldButton);
add(buttonPanels);
}

public void setDesignation(DesignationInterface designation)
{
this.designation=designation;
titleLabel.setText(this.designation.getTitle());
}

public void clearDesignation()
{
titleLabel.setText("");
}

public void setViewMode()
{
DesignationUI.this.setViewMode();
addButton.setIcon(addIcon);
editButton.setIcon(editIcon);
mode=MODE.VIEW;
addButton.setEnabled(true);
cancelButton.setEnabled(false);
clearTextFieldButton.setVisible(false);
titleTextField.setVisible(false);
titleLabel.setVisible(true);
if(designationTable.getRowCount()>0)
{
editButton.setEnabled(true);
deleteButton.setEnabled(true);
exportToPDFButton.setEnabled(true);
}
else
{
editButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
}

public void setAddMode()
{
mode=MODE.ADD;
DesignationUI.this.setAddMode();
titleTextField.setText("");
titleTextField.setVisible(true);
clearTextFieldButton.setVisible(true);
titleLabel.setVisible(false);
addButton.setIcon(new ImageIcon("SaveIcon.png"));
exportToPDFButton.setEnabled(false);
editButton.setEnabled(false);
deleteButton.setEnabled(false);
cancelButton.setEnabled(true);
}

void setEditMode()
{
mode=MODE.EDIT;
if(designationTable.getSelectedRow()<=0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select Designation to edit");
mode=MODE.VIEW;
return;
}
DesignationUI.this.setEditMode();
clearTextFieldButton.setVisible(true);
titleTextField.setText(designation.getTitle());
titleTextField.setVisible(true);
titleLabel.setVisible(false);
editButton.setIcon(editIcon);
exportToPDFButton.setEnabled(false);
addButton.setEnabled(false);
deleteButton.setEnabled(false);
cancelButton.setEnabled(true);
}

private void setDeleteMode()
{
isCompleted=false;
mode=MODE.DELETE;
if(designationTable.getSelectedRow()<=0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select Designation to delete");
return;
}
DesignationUI.this.setDeleteMode();
titleTextField.setText(designation.getTitle());
titleTextField.setVisible(true);
clearTextFieldButton.setVisible(true);
titleLabel.setVisible(false);
exportToPDFButton.setEnabled(false);
addButton.setEnabled(false);
deleteButton.setEnabled(false);
cancelButton.setEnabled(false);
isCompleted=true;
}

void setExportToPDFMode()
{
mode=MODE.EXPORT_TO_PDF;
DesignationUI.this.setExportToPDFMode();
titleLabel.setVisible(false);
exportToPDFButton.setEnabled(false);
addButton.setEnabled(false);
deleteButton.setEnabled(false);
cancelButton.setEnabled(false);
}


private boolean addDesignation()
{
DesignationInterface blDesignation=new Designation();
try{
blDesignation.setTitle(titleTextField.getText());
designationModel.add(blDesignation);
int rowIndex=0;
try{
rowIndex=designationModel.indexOfDesignation(blDesignation);
}catch(BLException blException)
{
JOptionPane.showMessageDialog(this,"Designation does not exists.");
return false;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
return true;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}else 
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
titleTextField.requestFocus();
return false;
}
}

private boolean editDesignation()
{
if(designationTable.getSelectedRow()<=0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select Designation to edit");
return false;
}
String title=titleTextField.getText().trim();
if(title.length()==0) 
{
JOptionPane.showMessageDialog(this,"Designation required");
return false;
}
DesignationInterface blDesignation=new Designation();
try{
blDesignation.setTitle(title);
blDesignation.setCode(this.designation.getCode());
designationModel.update(blDesignation);
int rowIndex=0;
try{
rowIndex=designationModel.indexOfDesignation(blDesignation);
}catch(BLException blException)
{
JOptionPane.showMessageDialog(this,"Designation does not exists.");
return false;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
return true;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}else 
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
titleTextField.requestFocus();
return false;
}
}

public void deleteDesignation()
{
String title=designation.getTitle();
try{
int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+title,"Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==0)
{
designationModel.remove(designation.getCode());
JOptionPane.showMessageDialog(this,title+" deleted");
}
else return;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}else 
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
} // catch finished
}


public void addListeners()
{
addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW) setAddMode();
else {
if(addDesignation()) setViewMode();
}
}
});



editButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setEditMode();
//done done
}
else{
if(editDesignation()) setViewMode();
}
}
});

deleteButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
setDeleteMode();
if(isCompleted)
{
deleteDesignation();
setViewMode();
}
}
});

exportToPDFButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
JFileChooser jfc=new JFileChooser();
jfc.setAcceptAllFileFilterUsed(false);
jfc.setCurrentDirectory(new File("."));
jfc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){

public boolean accept(File file)
{
if(file.isDirectory()) return true;
if(file.getName().endsWith(".pdf")) return true;

return false;
}
public String getDescription()
{
return "Pdf Files";
}

});

int selectedOption=jfc.showSaveDialog(DesignationUI.this);
try{
if(selectedOption==jfc.APPROVE_OPTION)
{
File selectedFile=jfc.getSelectedFile();
String  pdfFile=selectedFile.getAbsolutePath();
if(pdfFile.endsWith(".")==true) pdfFile+="pdf";
else if(pdfFile.endsWith(".pdf")==false) pdfFile+=".pdf ";
File file=new File(pdfFile);
File parent=new File(file.getParent());
if(parent.exists()==false || parent.isDirectory()==false) JOptionPane.showMessageDialog(DesignationUI.this,"Path does not exists");
System.out.println(file);
designationModel.exportToPDF(file);
JOptionPane.showMessageDialog(DesignationUI.this,"Pdf file is successfully created as "+file);
}
} 
catch(Exception e)
{
System.out.println(e.getMessage());
}
}
});

cancelButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
setViewMode();
}
});

}
}
}