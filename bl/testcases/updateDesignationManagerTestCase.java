import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import java.util.*;

public class updateDesignationManagerTestCase
{
public static void main(String ff[])
{
try{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
Designation designation=new Designation();
designation.setTitle("Barber");
designation.setCode(1);
designationManager.updateDesignation(designation);
//System.out.println("Designation Updated");
}catch(BLException blException)
{
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}
