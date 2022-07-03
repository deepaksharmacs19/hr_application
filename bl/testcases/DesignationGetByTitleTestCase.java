import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class DesignationGetByTitleTestCase
{
public static void main(String gg[])
{
String title=gg[0];
try{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation=designationManager.getDesignationByTitle(title);
System.out.printf("Code : %d, Title : %s",designation.getCode(),designation.getTitle());
}catch(BLException blException)
{
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}