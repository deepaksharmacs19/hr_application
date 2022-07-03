import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class DesignationGetDesignations
{
public static void main(String gg[])
{
try{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
Set<DesignationInterface> designation=designationManager.getDesignations();
designation.forEach((d)->{System.out.printf("Code : %d, Title : %s\n",d.getCode(),d.getTitle());
});
}catch(BLException blException)
{
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}