package antonio.franzese.iss2021_resumableb.annotations;

import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IssAnnotationUtil {
    /*
    -------------------------------------------------------------------------------
    RELATED TO PROTOCOLS
    -------------------------------------------------------------------------------
     */
    public static ProtocolInfo getProtocol(Object element) {
        Class<?> clazz = element.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        ProtocolInfo protocolInfo = null;
        for (Annotation annot : annotations) {
            if (annot instanceof IssProtocolSpec) {
                IssProtocolSpec info = (IssProtocolSpec) annot;
                protocolInfo = checkProtocolConfigFile(info.configFile());
                if (protocolInfo == null) {
                    protocolInfo = new ProtocolInfo(info.protocol(), info.url());
                }
                //System.out.println("IssAnnotationUtil | getProtocol protocolInfo=" + protocolInfo );
            }
        }
        return protocolInfo;
    }

    //Used also by UniboRobotApplicationStarter
    public static ProtocolInfo checkProtocolConfigFile(String configFileName) {
        try {
            System.out.println("IssAnnotationUtil | checkProtocolConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            //System.out.println("IssAnnotationUtil | line=" + line);
            String[] items = line.split(",");

            String protocol = IssAnnotationUtil.getProtocolConfigInfo("protocol", items[0]);
            System.out.println("IssAnnotationUtil | protocol=" + protocol);

            String url = IssAnnotationUtil.getProtocolConfigInfo("url", items[1]);
            //System.out.println("IssAnnotationUtil | url=" + url);

            ProtocolInfo protinfo = new ProtocolInfo(protocol, url);
            return protinfo;
        } catch (Exception e) {
            System.out.println("IssAnnotationUtil | WARNING:" + e.getMessage());
            return null;
        }
    }

    //Quite bad: we will replace with Prolog parser
    public static String getProtocolConfigInfo(String functor, String line) {
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = null;
        if (matcher.find()) {
            int end = matcher.end();
            content = line.substring(end, line.indexOf(")"))
                    .replace("\"", "")
                    .replace("(", "").trim();
        }
        return content;
    }


    /*
-------------------------------------------------------------------------------
RELATED TO ROBOT MOVES
-------------------------------------------------------------------------------
 */
    public static void getMoveTimes(Object obj, HashMap<String, Integer> mvtimeMap) {
        Class<?> clazz = obj.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        fillMap(mvtimeMap, annotations);
    }

    public static void fillMap(HashMap<String, Integer> mvtimeMap, Annotation[] annots) {
        for (Annotation annotation : annots) {
            if (annotation instanceof RobotMoveTimeSpec) {
                //Priority to the con
                RobotMoveTimeSpec info = (RobotMoveTimeSpec) annotation;
                if (!checkRobotConfigFile(info.configFile(), mvtimeMap)) {
                    mvtimeMap.put("w", info.wtime());
                    mvtimeMap.put("s", info.stime());
                    mvtimeMap.put("l", info.ltime());
                    mvtimeMap.put("r", info.rtime());
                    mvtimeMap.put("h", info.htime());
                }
                //System.out.println("IssAnnotationUtil | fillMap  ltime="  + info.ltime());
            }
        }
    }

    //Used also by IssArilRobotSupport
    public static boolean checkRobotConfigFile(
            String configFileName, HashMap<String, Integer> mvtimeMap) {
        try {
            //spec( htime( 100 ),  ltime( 300 ), rtime( 300 ),  wtime( 600 ), wstime( 600 ) ).
            //System.out.println("IssAnnotationUtil | checkRobotConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            //System.out.println("IssAnnotationUtil | checkRobotConfigFile line=" + line);
            String[] items = line.split(",");
            mvtimeMap.put("h", getRobotConfigInfo("htime", items[0]));
            mvtimeMap.put("l", getRobotConfigInfo("ltime", items[1]));
            mvtimeMap.put("r", getRobotConfigInfo("rtime", items[2]));
            mvtimeMap.put("w", getRobotConfigInfo("wtime", items[3]));
            mvtimeMap.put("s", getRobotConfigInfo("stime", items[4]));
            //System.out.println("IssAnnotationUtil | checkRobotConfigFile ltime=:" + mvtimeMap.get("l"));
            return true;
        } catch (Exception e) {
            System.out.println("IssAnnotationUtil | checkRobotConfigFile WARNING:" + e.getMessage());
            return false;
        }

    }

    protected static Integer getRobotConfigInfo(String functor, String line) {
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = "0";
        if (matcher.find()) {
            int end = matcher.end();
            content = line.substring(end, line.indexOf(")"))
                    .replace("\"", "")
                    .replace("(", "").trim();
            //System.out.println("IssAnnotationUtil | getRobotConfigInfo functor=" + functor + " v=" + Integer.parseInt(content));
        }
        return Integer.parseInt(content);
    }


/*
METHODS
 */
    /*
public static void injectRobotSupport(Object object, IssAppOperations rs)   {
    //println("injectRobotSupport object=" + object);
    Class<?> clazz = object.getClass();
    for (Method method : clazz.getDeclaredMethods()) {
        System.out.println("injectRobotSupport method=" + method);
        if (method.isAnnotationPresent(InjectSupportSpec.class)) {
            method.setAccessible(true);
            try{
                //System.out.println("injectRobotSupport invoke " + method);
                method.invoke(object,rs);   //the first argument is this
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}
*/
}
