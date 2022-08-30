import net.sourceforge.tess4j.Tesseract;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class RecognizeText {
    static String SRC_PATH = "D:\\openCV-OCR\\src\\main\\java_text\\";
    static Tesseract tesseract = new Tesseract();
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        tesseract.setDatapath("D:\\Tesseract\\tessdata");
    }

    public String extractString(Mat input){
        String result = "";
        Mat gray = new Mat();

        Imgproc.cvtColor(input,gray,Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite(SRC_PATH + "gray.png",gray);

        try{
            result = tesseract.doOCR(new File(SRC_PATH + "gray.png"));
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        //Mat origin = Imgcodecs.imread(SRC_PATH + "text3.png");
        //System.out.println(new RecognizeText().extractString(origin));


        Mat origin = Imgcodecs.imread(SRC_PATH + "text3.png");
        Mat gray = new Mat();
        Imgproc.cvtColor(origin, gray, Imgproc.COLOR_BGR2GRAY);

        //thresh = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY)[1];

    }
}
