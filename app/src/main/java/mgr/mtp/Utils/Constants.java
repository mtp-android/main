package mgr.mtp.Utils;

import java.text.SimpleDateFormat;

/**
 * Created by lukas on 19.03.2016.
 */
public class Constants {

    // on emulator
    public static String host = "http://10.0.2.2:8080";

    // on device
    //public static String host = "http://192.168.2.5:8080";

    public static SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yyyy");

}
