package mgr.mtp.Utils;

import java.text.SimpleDateFormat;

/**
 * Created by lukas on 19.03.2016.
 */
public class Constants {

    // on emulator
    //public static String host = "http://10.0.2.2:8081";

    // on device - router
    //public static String host = "http:/169.168.7.113:8081";

    // on device
    public static String host = "http://env-6137508.unicloud.pl/mtp";

    public static SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat shortDateFormt = new SimpleDateFormat("dd.MM");

}
