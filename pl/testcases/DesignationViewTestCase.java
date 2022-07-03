import com.thinking.machines.hr.pl.model.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

class DesignationViewTestCase extends JFrame
{
private Container container;
private DesignationModel designationModel;
private JTable table;
private JScrollPane jsp;
DesignationViewTestCase()
{
designationModel=new DesignationModel();
table=new JTable(designationModel);
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
container.add(jsp);
setLocation(34,45);
setSize(400,500);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
}

class DesignationViewTestCasepsp
{
public static void main(String gg[])
{
DesignationViewTestCase dvtc=new DesignationViewTestCase();
}
}