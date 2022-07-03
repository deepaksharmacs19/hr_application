import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class DesignationIsTitleExists
{
public static void main(String gg[])
{
String title=gg[0];
try{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
System.out.println(designationManager.designationTitleExists(title));
}catch(BLException blException)
{
List<String> exceptions=blException.getProperties();
exceptions.forEach((k)->{
System.out.println(blException.getException(k));
});
}
}
}