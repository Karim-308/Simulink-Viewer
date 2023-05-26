//package testjavafx;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class SimulinkViewer extends Application {

    private SimulinkModel model;
    private String filePath;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

	// Load and parse the Simulink MDL file
        // create a label to display the message
        Label label = new Label("Load a Simulink file to View");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // create a file chooser to select the Simulink file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
	new FileChooser.ExtensionFilter("Simulink Files", "*.mdl"));

        // create a button to browse for the Simulink file
        Button browseButton = new Button("Browse");


        ///////////////
        // create an HBox to hold the label and button
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20));
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(browseButton, label);

        // create a VBox to hold the HBox and center it vertically
        VBox vbox = new VBox(10);
        vbox.setPrefSize(400, 200);
        vbox.setLayoutX(200);
        vbox.setLayoutY(200);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(browseButton, label);

        // create an image view with the background image
     Image image = new Image("file:src/images/Background.png");
        ImageView imageView = new ImageView(image);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        // create a border pane to hold the VBox and image view
        BorderPane root = new BorderPane();
        root.setBackground(background);
        root.setCenter(vbox);

        // create a scene with the border pane and set it to the stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.getIcons().add(new Image("file:src/images/Arrow.png"));
        /////////////////////

        showFileContent(primaryStage,scene, "Upload MDL File");

        browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    // set the file path
                    filePath = file.getAbsolutePath();

                    SimulinkParser parser = new SimulinkParser(filePath);
                    try {
                        model = parser.parse();
                    } catch (Exception ex) {
                        Logger.getLogger(SimulinkViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    Pane pane = new Pane();


                    for (SimulinkBlock block: model.getBlocks())

                    {
                        block.setRectangle();
                        setImage(block);
                        pane.getChildren().addAll(block.getRectangle(), block.getText());

                    }



                    drawAllLines(model.getlines(), model.getBlocks(), pane);

                    Scene contentScene = new Scene(pane, 1200, 800);

                    // do something with the selected file
                    primaryStage.getIcons().add(new Image("file:src/images/App.png"));
                    showFileContent(primaryStage,contentScene, "Simulink Viewer");

                }
            }
        });

    }


    public void showFileContent ( Stage primaryStage , Scene scene, String title)
    {


        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();



    }

    public void setImage(SimulinkBlock block) {
        if (block.getType().equals("Sum")) {

            // Load the image file
            Image addImage = new Image("file:src/images/plus.png");
            // Create ImageView objects and set their properties
            ImagePattern iv1 = new ImagePattern(addImage);
            block.getRectangle().setFill(iv1);
        } else if (block.getType().equals("Constant")) {

            // Load the image file
            Image addImage = new Image("file:src/images/Constant.png");

            // Create ImageView objects and set their properties
            ImagePattern iv1 = new ImagePattern(addImage);
            block.getRectangle().setFill(iv1);

        } else if (block.getType().equals("Saturate")) {

            // Load the image file
            Image addImage = new Image("file:src/images/Saturation.png");
            ImagePattern iv1 = new ImagePattern(addImage);
            block.getRectangle().setFill(iv1);
        } else if (block.getType().equals("Scope")) {

            // Load the image file
            Image addImage = new Image("file:src/images/Scope.png");

            // Create ImageView objects and set their properties
            ImagePattern iv1 = new ImagePattern(addImage);
            block.getRectangle().setFill(iv1);

        } else if (block.getType().equals("UnitDelay")) {

            // Load the image file
            Image addImage = new Image("file:src/images/Delay.png");

            // Create ImageView objects and set their properties
            ImagePattern iv1 = new ImagePattern(addImage);
            block.getRectangle().setFill(iv1);
        }

    }

    class SimulinkParser {
        private String filename;

        public SimulinkParser(String filename) {
            this.filename = filename;
        }

        public SimulinkModel parse() throws Exception {
            SimulinkModel model = new SimulinkModel();

            File f = new File(filename);
            FileInputStream sin = new FileInputStream(f);
            int d;
            StringBuilder sb = new StringBuilder();
            while ((d = sin.read()) != -1) {
                sb.append((char) d);
            }

            String data = sb.toString();
            String line = "";
            Scanner sc = new Scanner(data);
            int flagrightpart = 0;
            int k = 0;
            int u = -1;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.contains("__MWOPC_PART_BEGIN__ /simulink/systems/system_root.xml") || flagrightpart == 1) {
                    flagrightpart = 1;
                    if (line.contains("<Block")) {
                        SimulinkBlock block = new SimulinkBlock();
                        while (line.contains("</Block>") == false) {
                            if (line.contains("<Block")) {
                                block.setName(line.substring(line.indexOf("Name=") + 6, line.indexOf(" SID") - 1));

                            }
                            if (line.contains("<Block")) {
                                block.setType(line.substring(line.indexOf("BlockType=") + 11, line.indexOf(" Name=") - 1));
                            }
                            if (line.contains("<P Name=\"Position\">")) {
                                String s = line.substring(line.indexOf("<P Name=\"Position\">") + 20, line.indexOf("</P>") - 1);

                                SimulinkPosition position = new SimulinkPosition(s);

                                block.setPosition(position);

                            }

                            if (line.contains("<Block")) {
                                block.setSID(line.substring(line.indexOf("SID=") + 5, line.indexOf(">") - 1));
                            }
                            int por;
                            if (line.contains("<P Name=\"Ports\">")) {
                                por = 1;

                                String s = line.substring(line.indexOf("<P Name=\"Ports\">") + 17, line.indexOf("</P>") - 1);
                                s = s.replaceAll("\\s+", "");
                                String[] parts = s.split(",");
                                if (parts.length == 1) {

                                    block.setNuminputports(Integer.parseInt(parts[0]));

                                } else {
                                    block.setNuminputports(Integer.parseInt(parts[0]));
                                    block.setNumoutputoports(Integer.parseInt(parts[1]));
                                }
                                //SimulinkPort inputportt=new SimulinkPort();
                                //SimulinkPort outputportt=new SimulinkPort();


                            }
                            line = sc.nextLine();
                        }
                        model.addBlock(block);

                    } else if (line.contains("<Line>")) {

                        SimulinkLine lineo = new SimulinkLine();

                        while (line.contains("</Line>") == false) {

                            if (line.contains("<P Name=\"Src\">")) {

                                lineo.setSrcSID(Integer.parseInt(line.substring(line.indexOf("<P Name=\"Src\">") + 14, line.indexOf("#out"))));
                                lineo.setSrcportnumber(Integer.parseInt(line.substring(line.indexOf("#out:") + 5, line.indexOf("</P>"))) - 1);
                                for (SimulinkBlock block: model.getBlocks()) {
                                    int a = Integer.parseInt(block.getSID());
                                    if (a == lineo.getSrcSID()) {

                                        SimulinkPoint po = new SimulinkPoint();
                                        po.setX(block.getoutputPorts().get(lineo.getSrcportnumber()).getX());
                                        po.setY(block.getoutputPorts().get(lineo.getSrcportnumber()).getY());
                                        lineo.setSrcportpoint(po);
                                    }
                                }
                            }



                            if (line.contains("<P Name=\"Points\">")) {
                                String s = line.substring(line.indexOf("<P Name=\"Points\">") + 18, line.indexOf("</P>") - 1);
                                s = s.replaceAll("\\s+", "");

                                String[] parts1 = s.split(";");
                                for (int i = 0; i < parts1.length; i++) {
                                    SimulinkPoint po = new SimulinkPoint();
                                    String[] parts2 = parts1[i].split(",");
                                    po.setX(Integer.parseInt(parts2[0]));
                                    po.setY(Integer.parseInt(parts2[1]));

                                    lineo.addintermediatepoint(po);
                                }

                            }
                            if (line.contains("<P Name=\"Dst\">")) {
                                lineo.setDstSID(Integer.parseInt(line.substring(line.indexOf("<P Name=\"Dst\">") + 14, line.indexOf("#in"))));

                                lineo.setDstportnumber(Integer.parseInt(line.substring(line.indexOf("#in:") + 4, line.indexOf("</P>"))) - 1);
                                for (SimulinkBlock block: model.getBlocks()) {
                                    int a = Integer.parseInt(block.getSID());
                                    if (a == lineo.getDstSID()) {
                                        SimulinkPoint po = new SimulinkPoint();
                                        po.setX(block.getinputPorts().get(lineo.getDstportnumber()).getX());
                                        po.setY(block.getinputPorts().get(lineo.getDstportnumber()).getY());
                                        lineo.setDstportpoint(po);
                                    }
                                }
                                model.addLine(lineo);

                            }
                            if (line.contains("<Branch>")) {
                                SimulinkLine branchLine = new SimulinkLine(); // create a new line for the branch
                                branchLine.setSrcSID(lineo.getSrcSID());
                                branchLine.setSrcportnumber(lineo.getSrcportnumber());
                                branchLine.setSrcportpoint(lineo.getSrcportpoint());
                                for (int i = 0; i < lineo.getIntermrdiatepoints().size(); i++) {
                                    branchLine.addintermediatepoint(lineo.getIntermrdiatepoints().get(i)); // copy the intermediate points from the main line

                                }

                                while (line.contains("</Branch>") == false) {
                                    if (line.contains("<P Name=\"Points\">")) {
                                        String s = line.substring(line.indexOf("<P Name=\"Points\">") + 18, line.indexOf("</P>") - 1);
                                        s = s.replaceAll("\\s+", "");

                                        String[] parts1 = s.split(";");
                                        for (int i = 0; i < parts1.length; i++) {
                                            SimulinkPoint po = new SimulinkPoint();
                                            String[] parts2 = parts1[i].split(",");
                                            po.setX(Integer.parseInt(parts2[0]));
                                            po.setY(Integer.parseInt(parts2[1]));
                                            branchLine.addintermediatepoint(po);
                                        }

                                    }
                                    if (line.contains("<P Name=\"Dst\">")) {
                                        // parse the destination of the branch line
                                        branchLine.setDstSID(Integer.parseInt(line.substring(line.indexOf("<P Name=\"Dst\">") + 14, line.indexOf("#in"))));
					branchLine.setDstportnumber(Integer.parseInt(line.substring(line.indexOf("#in:") + 4, line.indexOf("</P>"))) - 1);
                                        for (SimulinkBlock block: model.getBlocks()) {
                                            int a = Integer.parseInt(block.getSID());
                                            if (a == branchLine.getDstSID()) {
                                                SimulinkPoint po = new SimulinkPoint();
                                                po.setX(block.getinputPorts().get(branchLine.getDstportnumber()).getX());
                                                po.setY(block.getinputPorts().get(branchLine.getDstportnumber()).getY());
                                                branchLine.setDstportpoint(po);
                                            }
                                        }
                                    }




                                    line = sc.nextLine();
                                }
                                model.addLine(branchLine);
                            }
                            line = sc.nextLine();
                        }
                    }
                    if (k == 0) {
                        int x = model.getBlocks().size();
                        if (x != 0) {
                            SimulinkBlock block = model.getBlocks().get(model.getBlocks().size() - 1);

                            int step = (block.getPosition().getHeight()) / (block.getNuminputports() + 1);

                            for (int i = 1; i <= block.getNuminputports(); i++) {
                                SimulinkPort inputportt = new SimulinkPort();
                                inputportt.setY(block.getPosition().getY() + i * step);
                                inputportt.setX(block.getPosition().getX());
                                block.addinputPort(inputportt);

                            }
                            step = block.getPosition().getHeight() / (block.getNumoutputoports() + 1);

                            for (int i = 1; i <= block.getNumoutputoports(); i++) {
                                SimulinkPort inputportt = new SimulinkPort();
                                inputportt.setY(block.getPosition().getY() + i * step);
                                inputportt.setX(block.getPosition().getX() + block.getPosition().getWidth());
                                block.addoutputPort(inputportt);

                            }
                            if (u == x) {

                                k = 1;

                            } else {
				    
                                u = x;
                            }
                        }



                    }
                }
            }
            return model;
        }
    }

    class SimulinkModel {
        private List < SimulinkBlock > blocks;
        private List < SimulinkLine > lines;

        public SimulinkModel() {
            blocks = new ArrayList < > ();
            lines = new ArrayList < > ();
        }

        public void addBlock(SimulinkBlock block) {
            blocks.add(block);
        }
        public void addLine(SimulinkLine line) {
            lines.add(line);
        }
        public List < SimulinkBlock > getBlocks() {
            return blocks;
        }

        public List < SimulinkLine > getlines() {
            return lines;
        }
    }

    class SimulinkBlock {
        private String name;
        private String type;
        private String SID;
        private SimulinkPosition position;
        private ArrayList < SimulinkPort > inputports;
        private ArrayList < SimulinkPort > outputports;

        private int numinputports = 1;
        private int numoutputoports = 1;


        public int getNuminputports() {
            return numinputports;
        }

        public int getNumoutputoports() {
            return numoutputoports;
        }

        public void setNuminputports(int numinputports) {
            this.numinputports = numinputports;
        }

        public void setNumoutputoports(int numoutputoports) {
            this.numoutputoports = numoutputoports;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }

        public String getSID() {
            return SID;
        }

        public SimulinkBlock() {
            outputports = new ArrayList < > ();
            inputports = new ArrayList < > ();


        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setPosition(SimulinkPosition position) {
            this.position = position;
        }

        public SimulinkPosition getPosition() {
            return position;
        }

        public void addinputPort(SimulinkPort port) {
            inputports.add(port);
        }

        public void addoutputPort(SimulinkPort port) {
            outputports.add(port);
        }
        public List < SimulinkPort > getinputPorts() {
            return inputports;
        }

        public List < SimulinkPort > getoutputPorts() {
            return outputports;
        }
        Rectangle r;
        Text t;


        public void setRectangle() {

            this.r = new Rectangle(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getWidth(), this.getPosition().getHeight());
            r.setStroke(Color.BLACK);
            r.setFill(Color.WHITE);
            r.setArcHeight(5);
            r.setArcWidth(5);
            DropShadow drop_shadow = new DropShadow(10.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
            r.setEffect(drop_shadow);

            t = new Text(this.getName());
            t.setTextAlignment(TextAlignment.CENTER);


            t.setX(r.getX() + r.getWidth() / 2 - t.getLayoutBounds().getWidth() / 2);
            t.setY(r.getY() + r.getHeight() + 12);
            t.setStrokeWidth(40);

        }

        public Rectangle getRectangle() {
            return r;
        }


        public Text getText() {
            return t;
        }
    }
    public void drawAllLines(List < SimulinkLine > lines, List < SimulinkBlock > blocks, Pane pane) {
        // loop through all lines

        for (SimulinkLine l: lines) {
            List < SimulinkPoint > intermedPointsList = l.getIntermrdiatepoints();

            if (intermedPointsList.size() <= 1) {
                if(intermedPointsList.size() == 1){
                    pane.getChildren().add(drawLine(l.getIntermrdiatepoints().get(0), l.getDstportpoint(), l));
                    pane.getChildren().add(drawArrow(l.getDstportpoint()));
                    pane.getChildren().add(drawPoint(l.getIntermrdiatepoints().get(0)));
                }
                else{
                    pane.getChildren().add(drawLine(l.getSrcportpoint(), l.getDstportpoint(),l));
                    pane.getChildren().add(drawArrow(l.getDstportpoint()));
                }
            } else {
                // draw line from port to branch point
                SimulinkPoint branchPoint = intermedPointsList.get(0);
                normalise(l.getSrcportpoint(), branchPoint);

                pane.getChildren().add(drawLine(l.getSrcportpoint(), branchPoint,l));
                // loop through all intermediate points
                int i = 0;
                for (SimulinkPoint p: intermedPointsList) {
                    if (i == 0) {
                        i++;
                        continue;
                    }
                    normalise(branchPoint, p);
                    pane.getChildren().add(drawLine(branchPoint, p,l));
                    branchPoint = p;
                }
                // get destination block
                SimulinkBlock swappedBlock = new SimulinkBlock();
                for (SimulinkBlock b: blocks) {
                    if (l.getDstSID() == Integer.parseInt(b.getSID())) {
                        swappedBlock = b;
                        break;
                    }
                }
                // check for inverted ports condition
                if (swappedBlock.getoutputPorts().size() != 0) {
                    int dstFromBranchPointToInputOfDestinationBlock = Math.abs(branchPoint.getX() - l.getDstportpoint().getX());
                    int dstFromBranchPointToOutputOfDestinationBlock = Math.abs(branchPoint.getX() - swappedBlock.getoutputPorts().get(0).getX());
                    if (dstFromBranchPointToInputOfDestinationBlock > dstFromBranchPointToOutputOfDestinationBlock) {
                        //swap block input ports places
                        int temp = swappedBlock.getinputPorts().get(0).getX();
                        for (int j = 0; j < swappedBlock.getinputPorts().size(); ++j) {
                            swappedBlock.getinputPorts().get(j).setX(swappedBlock.getoutputPorts().get(0).getX());
                        }
                        swappedBlock.getoutputPorts().get(0).setX(temp);
                        l.setDstportpoint(swappedBlock.getinputPorts().get(0).getPoint());

                        // swap line attached to swapped block
                        for(SimulinkLine swappedLine : lines){
                            if(swappedLine.getSrcSID() == Integer.parseInt(swappedBlock.getSID())){
                                swappedLine.setSrcportpoint(swappedBlock.getoutputPorts().get(0).getPoint());
                                break;
                            }
                        }

                    }
                }

                pane.getChildren().add(drawLine(branchPoint, l.getDstportpoint(),l));
                if((l.getDstportpoint().getX() - branchPoint.getX()) < 0 ){
                    pane.getChildren().add(drawArrowInverted(l.getDstportpoint()));
                }
                else{
                    pane.getChildren().add(drawArrow(l.getDstportpoint()));
                }

            }

        }
    }

    public void normalise(SimulinkPoint p1, SimulinkPoint p2) {
        p2.setX(p2.getX() + p1.getX());
        p2.setY(p2.getY() + p1.getY());
    }
    public Line drawLine(SimulinkPoint p1, SimulinkPoint p2, SimulinkLine currentLine) {
        if(((p1.getX() - p2.getX()) != 0) && ((p1.getY() - p2.getY()) != 0) ){
            currentLine.getDstportpoint().setY(p1.getY());
            Line l = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            l.setOpacity(10);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(1);
            DropShadow drop_shadow = new DropShadow(2.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
            l.setEffect(drop_shadow);

            return l;
        }
        else{
            Line l = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            l.setOpacity(10);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(1);
            DropShadow drop_shadow = new DropShadow(2.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
            l.setEffect(drop_shadow);
            return l;
        }
    }
    public Line drawLineFilled(SimulinkPoint p1, SimulinkPoint p2) {
        if(((p1.getX() - p2.getX()) != 0) && ((p1.getY() - p2.getY()) != 0) ){
            Line l = new Line(p1.getX(), p1.getY(), p2.getX(), p1.getY());
            l.setOpacity(10);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(1.5);
            DropShadow drop_shadow = new DropShadow(2.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
            l.setEffect(drop_shadow);
            return l;
        }
        else{
            Line l = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            l.setOpacity(10);
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(1.5);
            DropShadow drop_shadow = new DropShadow(2.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
            l.setEffect(drop_shadow);
            return l;
        }
    }
    public Polygon drawArrow(SimulinkPoint p){
        Polygon triangle = new Polygon();
        double x_coord = (double)p.getX();
        double y_coord = (double)p.getY();
        triangle.getPoints().addAll(new Double[]{
                x_coord - 5, y_coord + 5,
                x_coord, y_coord, //head (x,y)
                x_coord - 5, y_coord - 5});

        // set the fill color of the triangle
        triangle.setFill(Color.BLACK);
        DropShadow drop_shadow = new DropShadow(5.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
        triangle.setEffect(drop_shadow);
        return triangle;
    }
    public Polygon drawArrowInverted(SimulinkPoint p){
        Polygon triangle = new Polygon();
        double x_coord = (double)p.getX();
        double y_coord = (double)p.getY();
        triangle.getPoints().addAll(new Double[]{
                x_coord + 5, y_coord + 5,
                x_coord, y_coord, //head (x,y)
                x_coord + 5, y_coord - 5});

        // set the fill color of the triangle
        triangle.setFill(Color.BLACK);
	
        DropShadow drop_shadow = new DropShadow(5.0f, Color.color(10.0/255.0, 200.0/255.0, 255.0/255.0));
        triangle.setEffect(drop_shadow);
        return triangle;
    }
    public Circle drawPoint(SimulinkPoint p) {
        Circle point = new Circle(2.5,Color.BLACK);
        point.setLayoutX(p.getX());
        point.setLayoutY(p.getY());
        return point;
    }


    class SimulinkPort {
        private int x;
        private int y;


        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public SimulinkPoint getPoint() {
            SimulinkPoint p = new SimulinkPoint();
            p.setX(x);
            p.setY(y);
            return p;
        }
    }

    class SimulinkPosition {
        private int x;
        private int y;
        private int width;
        private int height;

        public SimulinkPosition(String str) {
            str = str.replaceAll("\\s+", "");
            String[] parts = str.split(",");

            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
            width = Integer.parseInt(parts[2]) - x;
            height = Integer.parseInt(parts[3]) - y;

        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
    class SimulinkPoint {
        private int x;
        private int y;

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
    class SimulinkLine {
        private SimulinkPoint srcportpoint;
        private SimulinkPoint dstportpoint;
        private ArrayList < SimulinkPoint > intermrdiatepoints;
        int srcportnumber;
        int dstportnumber;
        int srcSID;
        int dstSID;
        public SimulinkLine() {
            intermrdiatepoints = new ArrayList < > ();
        }
        public void addintermediatepoint(SimulinkPoint point) {
            intermrdiatepoints.add(point);
        }

        public void setSrcportpoint(SimulinkPoint srcportpoint) {
            this.srcportpoint = srcportpoint;
        }

        public void setDstportpoint(SimulinkPoint dstportpoint) {
            this.dstportpoint = dstportpoint;
        }

        public void setSrcportnumber(int srcportnumber) {
            this.srcportnumber = srcportnumber;
        }

        public void setDstportnumber(int dstportnumber) {
            this.dstportnumber = dstportnumber;
        }

        public void setSrcSID(int srcSID) {
            this.srcSID = srcSID;
        }

        public void setDstSID(int dstSID) {
            this.dstSID = dstSID;
        }

        public SimulinkPoint getSrcportpoint() {
            return srcportpoint;
        }

        public SimulinkPoint getDstportpoint() {
            return dstportpoint;
        }

        public ArrayList < SimulinkPoint > getIntermrdiatepoints() {
            return intermrdiatepoints;
        }

        public int getSrcportnumber() {
            return srcportnumber;
        }

        public int getDstportnumber() {
            return dstportnumber;
        }

        public int getSrcSID() {
            return srcSID;
        }

        public int getDstSID() {
            return dstSID;
        }
    }
}
