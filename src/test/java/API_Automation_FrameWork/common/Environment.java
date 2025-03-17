package API_Automation_FrameWork.common;


import java.util.HashMap;

public class Environment {

    public static HashMap<String,Object> env = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Environment HashMap: " + Environment.env);
    }
}
