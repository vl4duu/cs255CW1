import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main extends Application {
    short cthead[][][];
    short min, max;
    int CT_x_axis = 256;
    int CT_y_axis = 256;
    int CT_z_axis = 113;
    double skinTransparency = 0.12;


    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException {
        stage.setTitle("CThead Viewer");


        ReadData();

        //Good practice: Define your top view, front view and side view images (get the height and width correct)
        //Here's the top view - looking down on the top of the head (each slice we are looking at is CT_x_axis x CT_y_axis)
        int Top_width = CT_x_axis;
        int Top_height = CT_y_axis;

        int Front_width = CT_x_axis;
        int Front_height = CT_z_axis;

        int Side_width = CT_y_axis;
        int Side_height = CT_z_axis;


        WritableImage top_image = new WritableImage(Top_width, Top_height);
        WritableImage front_image = new WritableImage(Front_width, Front_height);
        WritableImage side_image = new WritableImage(Side_width, Side_height);


        ImageView topView = new ImageView(top_image);
        ImageView FrontView = new ImageView(front_image);
        ImageView sideView = new ImageView(side_image);
        Button volRend = new Button("VolRend");

        //sliders to step through the slices (top and front directions) (remember 113 slices in top direction 0-112)
        Slider Top_slider = new Slider(0, CT_z_axis - 1, 0);
        Slider Front_slider = new Slider(0, CT_y_axis - 1, 0);
        Slider Side_slider = new Slider(0, CT_x_axis - 1, 0);
        Slider Transparency = new Slider(0, 100, 50);


        volRend.setOnAction(event -> volumeRendering(top_image,side_image,front_image));

        Top_slider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        TopDownSlice(top_image, newValue.intValue());
                    }
                });

        Front_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                FrontalSlice(front_image, newValue.intValue());
            }
        });

        Side_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SideSlice(side_image, newValue.intValue());
            }
        });

        Transparency.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.skinTransparency = (double) newValue / 100;
            System.out.println(skinTransparency);
            sendVertRay(top_image);
        });



        FlowPane root = new FlowPane();
        root.setVgap(8);
        root.setHgap(4);
        root.getChildren().addAll(topView, sideView, FrontView, Top_slider, Front_slider, Side_slider,volRend, Transparency);

        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public void TopDownSlice(WritableImage image, int slice) {   //front view
        //Get image dimensions, and declare loop variables
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelWriter image_writer = image.getPixelWriter();

        double col;
        short datum;
        //Shows how to loop through each pixel and colour
        //Try to always use j for loops in y, and i for loops in x
        //as this makes the code more readable
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                //at this point (i,j) is a single pixel in the image
                //here you would need to do something to (i,j) if the image size
                //does not match the slice size (e.g. during an image resizing operation
                //If you don't do this, your j,i could be outside the array bounds
                //In the framework, the image is 256x256 and the data set slices are 256x256
                //so I don't do anything - this also leaves you something to do for the assignment
                datum = cthead[slice][j][i]; //get values from slice 76 (change this in your assignment)
                //calculate the colour by performing a mapping from [min,max] -> 0 to 1 (float)
                //Java setColor uses float values from 0 to 1 rather than 0-255 bytes for colour
                col = (((float) datum - (float) min) / ((float) (max - min)));
                image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
            } // column loop
        } // row loop
    }

    public void FrontalSlice(WritableImage image, int slice) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelWriter image_writer = image.getPixelWriter();
        short datum;


        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {

                //at this point (i,j) is a single pixel in the image
                //here you would need to do something to (i,j) if the image size
                //does not match the slice size (e.g. during an image resizing operation
                //If you don't do this, your j,i could be outside the array bounds
                //In the framework, the image is 256x256 and the data set slices are 256x256
                //so I don't do anything - this also leaves you something to do for the assignmen
                datum = cthead[j][slice][i];


                double col = (((float) datum - (float) min) / ((float) (max - min)));
                image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
            } // column loop
        } // row loop
    }

    public void SideSlice(WritableImage image, int slice) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelWriter image_writer = image.getPixelWriter();

        double col;
        short datum;


        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {

                //at this point (i,j) is a single pixel in the image
                //here you would need to do something to (i,j) if the image size
                //does not match the slice size (e.g. during an image resizing operation
                //If you don't do this, your j,i could be outside the array bounds
                //In the framework, the image is 256x256 and the data set slices are 256x256
                //so I don't do anything - this also leaves you something to do for the assignment
                datum = cthead[j][i][slice];


                col = (((float) datum - (float) min) / ((float) (max - min)));
                image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
            } // column loop
        } // row loop
    }

    public Color TransferFunction(short voxel) {
        if (voxel >= 300 && voxel <= 4096) {
            return Color.color(1, 1, 1, 0.8);
        } else if (voxel >= 50 && voxel <= 299) {
            return Color.color(0, 0, 0, 0);
        } else if (voxel >= -300 && voxel <= 49) {
            return Color.color(1, 0.79, 0.6, skinTransparency);
        } else {
            return Color.color(0, 0, 0, 0);
        }
    }

    public void sendFrontRay(WritableImage image) {
        PixelWriter writer = image.getPixelWriter();
        for (int i = 0; i < CT_z_axis; i++) {

            for (int j = 0; j < CT_x_axis; j++) {
                Vector colAccum = new Vector(0, 0, 0);
                double trans = 1;
                for (int k = 0; k < CT_y_axis; k++) {
                    Vector sample = new Vector(TransferFunction(cthead[i][k][j]));
                    colAccum.addColors(sample.multiplyColor(sample.getOpacity()).multiplyColor(trans));
                    trans *= 1 - sample.getOpacity();

                }
                writer.setColor(j, i, colAccum.getColor());
            }
        }
    }

    public void sendVertRay(WritableImage image) {
        PixelWriter writer = image.getPixelWriter();
        for (int i = 0; i < CT_x_axis; i++) {

            for (int j = 0; j < CT_y_axis; j++) {
                Vector colAccum = new Vector(0, 0, 0);
                double trans = 1;
                for (int k = 0; k < CT_z_axis; k++) {
                    Vector sample = new Vector(TransferFunction(cthead[k][j][i]));
                    colAccum.addColors(sample.multiplyColor(sample.getOpacity()).multiplyColor(trans));
                    trans *= 1 - sample.getOpacity();

                }
                writer.setColor(i, j, colAccum.getColor());
            }
        }
    }

    public void sendSideRay(WritableImage image){
        PixelWriter writer = image.getPixelWriter();
        for (int i = 0; i < CT_z_axis; i++) {

            for (int j = 0; j < CT_y_axis; j++) {
                Vector colAccum = new Vector(0, 0, 0);
                double trans = 1;
                for (int k = 0; k < CT_x_axis; k++) {
                    Vector sample = new Vector(TransferFunction(cthead[i][j][k]));
                    colAccum.addColors(sample.multiplyColor(sample.getOpacity()).multiplyColor(trans));
                    trans *= 1 - sample.getOpacity();

                }
                writer.setColor(j, i, colAccum.getColor());
            }
        }
    }

    public void volumeRendering(WritableImage top, WritableImage side, WritableImage front){
        sendFrontRay(front);
        sendSideRay(side);
        sendVertRay(top);
    }


    public void ReadData() throws IOException {
        File file = new File("src/CThead");
        //Read the data quickly via a buffer (in C++ you can just do a single fread - I couldn't find if there is an equivalent in Java)
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        int i, j, k; //loop through the 3D data set

        min = Short.MAX_VALUE;
        max = Short.MIN_VALUE; //set to extreme values
        short read; //value read in
        int b1, b2; //data is wrong Endian (check wikipedia) for Java so we need to swap the bytes around

        cthead = new short[CT_z_axis][CT_y_axis][CT_x_axis]; //allocate the memory - note this is fixed for this data set
        //loop through the data reading it in
        for (k = 0; k < CT_z_axis; k++) {
            for (j = 0; j < CT_y_axis; j++) {
                for (i = 0; i < CT_x_axis; i++) {
                    //because the Endianess is wrong, it needs to be read byte at a time and swapped
                    b1 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
                    b2 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
                    read = (short) ((b2 << 8) | b1); //and swizzle the bytes around
                    if (read < min) min = read; //update the minimum
                    if (read > max) max = read; //update the maximum
                    cthead[k][j][i] = read; //put the short into memory (in C++ you can replace all this code with one fread)
                }
            }
        }
        System.out.println(min + " " + max); //diagnostic - for CThead this should be -1117, 2248
    }

    public static void main(String[] args) {
        launch();
    }
}