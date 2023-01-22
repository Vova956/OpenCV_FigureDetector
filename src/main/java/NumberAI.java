import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.io.File;
import java.io.IOException;

import static org.opencv.imgproc.Imgproc.resize;

public class NumberAI {
    private static final String dataPath = "D:\\openCV-OCR\\cnn-model.data";
    private static MultiLayerNetwork restored;
    private RecognizeCounters recognizeCounters = new RecognizeCounters();

    public int getNumberByMat(String path) throws IOException {
        try {
            File net = new File("D:\\openCV-OCR\\cnn-model.data");
            restored = ModelSerializer.restoreMultiLayerNetwork(net);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Mat digit = recognizeCounters.prepareForExtraction(path);
        resize(digit, digit, new Size(28, 28));
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1);
        INDArray dig = loader.asMatrix(digit);
        INDArray flaten = dig.reshape(new int[]{1, 784});
        INDArray output = restored.output(flaten);

        for (int i = 0; i < output.length(); i++) {
            if(output.getFloat(i) == 1.0)
                return i;
        }
        return 0;
    }

    public int getNumberByMat(Mat mat) throws IOException {
        try {
            File net = new File(dataPath);
            restored = ModelSerializer.restoreMultiLayerNetwork(net);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Imgcodecs.imwrite(MyRecognizeText.OUT_PATH + "inAI.png", mat);

        Mat digit = recognizeCounters.prepareForExtraction(mat);

        //Imgcodecs.imwrite(MyRecognizeText.OUT_PATH + "digit.png", digit);

        resize(digit, digit, new Size(28, 28));
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1);
        INDArray dig = loader.asMatrix(digit);
        INDArray flaten = dig.reshape(new int[]{1, 784});
        INDArray output = restored.output(flaten);
        System.out.println("HI!");
        for (int i = 0; i < output.length(); i++) {
            if(output.getFloat(i) == 1.0)
                return i;
        }
        System.out.println("ERROR!");
        return 0;
    }
}
