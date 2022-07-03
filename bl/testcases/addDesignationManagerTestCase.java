import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import java.util.*;

public class addDesignationManagerTestCase
{
public static void main(String ff[])
{
try{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
Designation designation=new Designation();
designation.setTitle("Manager");
designationManager.addDesignation(designation);
}catch(BLException blException)
{
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}
