import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.reduce.same.AMax;
import org.nd4j.linalg.api.ops.impl.reduce.same.Max;
import org.nd4j.linalg.api.ops.impl.transforms.MaxOut;
import org.nd4j.linalg.api.ops.impl.transforms.strict.Sin;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.imgproc.Imgproc.resize;

public class Main {
    private static MultiLayerNetwork restored;
    private final static String[] DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static void main(String[] args) throws IOException {

        MyRecognizeText myRecognizeText = new MyRecognizeText();
        Mat origin = Imgcodecs.imread(myRecognizeText.SRC_PATH + "realText3.jpg");
        //System.out.println(myRecognizeText.extractStringFromMat(origin));

        Mat dst = new Mat();
        ArrayList<MyFigure> figures = myRecognizeText.detectCounters(origin,dst);
        Imgcodecs.imwrite(myRecognizeText.OUT_PATH + "output.png", dst);
        System.out.println("AMOUNT OF OBJECTS FOUND = " + figures.size());


        double max = figures.get(0).getArea();
        int FigureIndex = 0;

        for (int i = 0; i < figures.size(); i++) {
            if(max < figures.get(i).getArea()){
                max = figures.get(i).getArea();
                FigureIndex = i;
            }
        }

        System.out.println("FIGURE INDEX = " + FigureIndex);

        for (int i = 0; i < figures.size(); i++) {
            if(i != FigureIndex){
                figures.get(i).setType(FigureEnum.NUMBER);
            }
        }

        for (int i = 0; i < figures.size(); i++) {
            if(i == FigureIndex) continue;
            try{
                Mat stuff = figures.get(i).getObjectMat();
                figures.get(i).setFigureType(FigureEnum.NUMBER);
                Imgcodecs.imwrite(myRecognizeText.OUT_PATH + "output" + (i + 1) + ".png", stuff);
            }catch (Exception e){
                System.out.println("FAILED TO PROCESS MAT NUMBER " + i);
            }
        }

        MyFigure figure = figures.get(FigureIndex);
        figure.setFigureType();
        System.out.println(figure.getType());

        ArrayList<MyFigure> numbers = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            if (i != FigureIndex){
                numbers.add(figures.get(i));
            }
        }

        System.out.println("NUMBERS FOUND : " + numbers.size());

        //---------------------------------------------------
        ArrayList<Integer> sides = new ArrayList<>();

        NumberAI numberAI = new NumberAI();
        for (int i = 0; i < numbers.size(); i++) {
            int a = numberAI.getNumberByMat(MyRecognizeText.OUT_PATH + "output" + (i+1) + ".png");
            System.out.println("number is " + a);
            sides.add(a);
        }

        System.out.println("This is a " + figure.getType() + " with sides : " + Arrays.toString(sides.toArray()));

    }
}