package sample;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

public class KernelConnection {
    private static KernelLink kernelLink;

    public static void init(String link ) throws MathLinkException {
            String[] mlArgs = {"-linkmode", "launch", "-linkname", link};
            kernelLink = MathLinkFactory.createKernelLink(mlArgs);
            kernelLink.discardAnswer();
    }

    public static KernelLink getKernelLink() {
        return kernelLink;
    }
    public static void closeConnection(){
        if (kernelLink != null)
            kernelLink.close();
    }
}
