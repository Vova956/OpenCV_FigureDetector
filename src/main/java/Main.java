import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyRecognizeText myRecognizeText = new MyRecognizeText();
        Mat origin = Imgcodecs.imread(myRecognizeText.SRC_PATH + "realText.jpg");
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

            try{
                Mat stuff = figures.get(i).getObjectMat();
                Imgcodecs.imwrite(myRecognizeText.OUT_PATH + "output" + (i + 1) + ".png", stuff);
                System.out.print("output" + (i + 1) + ".png - ");
                System.out.println(myRecognizeText.extractStringFromMat(stuff));
            }catch (Exception e){
                System.out.println("FAILED TO EXTRACT STRING FROM MAT NUMBER " + i);
            }
        }

        Mat val = origin.clone();

        MyFigure figure = figures.get(FigureIndex);

        Point[] rectPoints = figure.getRectanglepoints();
        for (int i = 0; i < rectPoints.length; i++) {
            Imgproc.circle(val,rectPoints[i],10,new Scalar(255,255,0));
        }


        figure.setFigureType();
        System.out.println(figure.getType());
        ArrayList<Point> MainPoints = figure.getMainPoints();
        Point[] cornerPoints = figure.getCornerPoints();
        Point[] allPoints = figure.getMatOfPoint().toArray();

        for (int i = 0; i < allPoints.length; i++) {
            Imgproc.circle(val,allPoints[i],5,new Scalar(255,0,0));
        }

        for (int i = 0; i < cornerPoints.length; i++) {
            Imgproc.circle(val,cornerPoints[i],5,new Scalar(0,255,0));
        }

        for (int i = 0; i < MainPoints.size(); i++) {
            Imgproc.circle(val,MainPoints.get(i),5,new Scalar(0,0,255));
        }


        Imgproc.circle(val,figure.getRectCenter(),5,new Scalar(0,255,0));
        Imgproc.rectangle(val,rectPoints[1],rectPoints[2],new Scalar(255,0,0));
        Imgcodecs.imwrite(myRecognizeText.OUT_PATH + "ValPoints.png", val);





    }
}