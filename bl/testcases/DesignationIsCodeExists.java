import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class DesignationIsCodeExists
{
public static void main(String gg[])
{
String title=gg[0];
int code=Integer.parseInt(gg[0]);
try{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
System.out.println(designationManager.designationCodeExists(code));
}catch(BLException blException)
{
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}