package com.example.pain_t;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Stack;

/**
 * Pain(t) application for CS 250
 * <p>
 * This class handles the tools and features for my Pain(t) application
 * </p>
 * @author reneaguilar-delgado
 */
public class HelloApplication extends Application {
    /**
     * Starts the stage for Pain(t) application
     * @param primaryStage primary stage for application
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Stack<Shape> undoHistory = new Stack();
        Stack<Shape> redoHistory = new Stack();

        /**
         * Buttons for Pain(t) tools
         */
        ToggleButton drowbtn = new ToggleButton("Draw");
        ToggleButton linebtn = new ToggleButton("Line");
        ToggleButton eraserbtn = new ToggleButton("Eraser");
        ToggleButton rectbtn = new ToggleButton("Rectangle");
        ToggleButton circlebtn = new ToggleButton("Circle");
        ToggleButton elpslebtn = new ToggleButton("Ellipse");
        ToggleButton hexabtn = new ToggleButton("Hexagon");
        ToggleButton textBoxbtn = new ToggleButton("Text Box");

        /**
         * Arrange(s) the order of the paint tools
         * Parameters for toolbar on the left
         */
        ToggleButton[] toolsArr = {drowbtn, linebtn, eraserbtn, rectbtn, circlebtn, elpslebtn, hexabtn, textBoxbtn};

        ToggleGroup tools = new ToggleGroup();

        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(90);
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }

        /**
         * Color picker for line and fill
         */
        ColorPicker cpLine = new ColorPicker(Color.BLACK);
        ColorPicker cpFill = new ColorPicker(Color.TRANSPARENT);

        TextArea text = new TextArea();
        text.setPrefRowCount(1);

        /**
         * Slider for line and eraser width
         */
        Slider slider = new Slider(1, 50, 1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        /**
         * Labels for line and fill color picker and slider
         */
        Label line_color = new Label("Line Color");
        Label fill_color = new Label("Fill Color");
        Label line_width = new Label("1.0");

        /**
         * Buttons for Undo, Redo, Save, etc.
         */
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button save = new Button("Save");
        Button open = new Button("Open");
        Button zoom = new Button("Zoom");
        Button zoomout = new Button("Zoom Out");
        Button releaseNotes = new Button("Release Notes");
        Button help = new Button("Help");
        Button close = new Button("Close");

        /**
         * Arrange(s) buttons for Undo, Redo, Save, etc
         */
        Button[] basicArr = {undo, redo, save, open, zoom, zoomout,releaseNotes, help, close};

        /**
         * Parameters and color for buttons for Undo, Redo, Save, etc.
         */
        for(Button btn : basicArr) {
            btn.setMinWidth(90);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #80334d;");
            save.setStyle("-fx-background-color: #80334d;");
            open.setStyle("-fx-background-color: #80334d;");
        }

        /**
         * Creates toolbar on the left of the application and displays all tool and function buttons
         */
        VBox btns = new VBox(7);
        btns.getChildren().addAll(drowbtn, linebtn, eraserbtn, rectbtn, circlebtn, elpslebtn, hexabtn, textBoxbtn, text, line_color, cpLine, fill_color, cpFill, line_width, slider, undo, redo, zoom, zoomout, open, save, releaseNotes, help, close);
        btns.setPadding(new Insets(5));
        btns.setStyle("-fx-background-color: #999");
        btns.setPrefWidth(100);

        /**
         * Parameters for the canvas
         */
        // Parameters for the canvas
        Canvas canvas = new Canvas(1080, 790);
        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);

        /**
         * Creates new line, rectangle, circle, and ellipse
         */
        //Creates new line, rectangle, circle, and ellipse
        Line line = new Line();
        Rectangle rect = new Rectangle();
        Circle circ = new Circle();
        Ellipse elps = new Ellipse();

        /**
         * Icon for tool buttons
         */
        //Icon for tool buttons
        Image drawPic = new Image("https://www.getillustrations.com/packs/elemental-scribble/line-texture/_1x/scribble%20_%20curls,%20curly,%20curl,%20curve,%20line,%20lines_md.png");
        ImageView drawIcon = new ImageView(drawPic);
        drawIcon.setFitWidth(20);
        drawIcon.setFitHeight(20);
        drowbtn.setGraphic(drawIcon);

        Image linePic = new Image("https://i.pinimg.com/originals/66/15/24/661524af491cfda437692eac19eed959.jpg");
        ImageView lineIcon = new ImageView(linePic);
        lineIcon.setFitHeight(20);
        lineIcon.setFitWidth(20);
        linebtn.setGraphic(lineIcon);

        Image eraserPic = new Image("https://previews.123rf.com/images/dstarky/dstarky1701/dstarky170101661/69931031-eraser-icon-or-logo-in-modern-line-style-high-quality-black-outline-pictogram-for-web-site-design-an.jpg");
        ImageView eraserIcon = new ImageView(eraserPic);
        eraserIcon.setFitHeight(20);
        eraserIcon.setFitWidth(20);
        eraserbtn.setGraphic(eraserIcon);

        Image rectanglePic = new Image("https://cdn.hinative.com/attached_images/227294/6dc8f7299914e3487a9cc6b03d96d28c4bd41fc4/large.jpg?1515275179");
        ImageView rectangleIcon = new ImageView(rectanglePic);
        rectangleIcon.setFitWidth(20);
        rectangleIcon.setFitHeight(20);
        rectbtn.setGraphic(rectangleIcon);

        Image circlePic = new Image("https://image.shutterstock.com/image-vector/circle-icon-black-white-linear-260nw-1247479555.jpg");
        ImageView circleIcon = new ImageView(circlePic);
        circleIcon.setFitHeight(20);
        circleIcon.setFitWidth(20);
        circlebtn.setGraphic(circleIcon);

        Image ellipsePic = new Image("https://www.internetstitch.com/wp-content/uploads/2020/03/JC11-01-1450-Oval-Outline.jpg");
        ImageView ellipseIcon = new ImageView(ellipsePic);
        ellipseIcon.setFitWidth(20);
        ellipseIcon.setFitHeight(20);
        elpslebtn.setGraphic(ellipseIcon);

        Image hexagonPic = new Image("https://m.media-amazon.com/images/I/21wY6y-uqQL._AC_.jpg");
        ImageView hexagonIcon = new ImageView(hexagonPic);
        hexagonIcon.setFitHeight(20);
        hexagonIcon.setFitWidth(20);
        hexabtn.setGraphic(hexagonIcon);

        Image textBoxPic = new Image("https://previews.123rf.com/images/msidiqf/msidiqf1904/msidiqf190400335/121552787-insert-text-box-icon-vector.jpg");
        ImageView textBoxIcon = new ImageView(textBoxPic);
        textBoxIcon.setFitWidth(20);
        textBoxIcon.setFitHeight(20);
        textBoxbtn.setGraphic(textBoxIcon);

        /**
         * Features for when mouse/trackpad is pressed on the canvas
         * This will get the beginning properties for drawing tools/shapes and the eraser
         */
        //Features for when mouse/trackpad is pressed on the canvas
        //This will get the beginning properties for drawing tools/shapes and the eraser
        canvas.setOnMousePressed(e->{
            if(drowbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            }
            else if(eraserbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
            else if(linebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                line.setStartX(e.getX());
                line.setStartY(e.getY());
            }
            else if(rectbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                rect.setX(e.getX());
                rect.setY(e.getY());
            }
            else if(circlebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                circ.setCenterX(e.getX());
                circ.setCenterY(e.getY());
            }
            else if(elpslebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                elps.setCenterX(e.getX());
                elps.setCenterY(e.getY());
            }
            else if(textBoxbtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                gc.fillText(text.getText(), e.getX(), e.getY());
                gc.strokeText(text.getText(), e.getX(), e.getY());
            }
        });

        /**
         * Feature for when mouse/trackpad is dragged
         * Used for "live" draw and "live" eraser
         * Can see the progress "live" and not upon release
         */
        //Features for when mouse/trackpad is dragged
        //Used for "live" draw and "live" eraser
        //Can see the progresses "live" and not upon release
        canvas.setOnMouseDragged(e->{
            if(drowbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
            else if(eraserbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        /**
         * Final properties for drawing tools mouse/trackpad release
         */
        //Final properties for drawing tools mouse/trackpad release
        canvas.setOnMouseReleased(e->{
            //draw mouse release - will stop "live" draw
            if(drowbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            }
            /**
             * Eraser mouse release - will stop "live" erase
             */
            //eraser mouse release - wil stop "live" erase
            else if(eraserbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
            /**
             * Line mouse release - will create line from mouse press to mouse released
             * Length and direction may vary depending on user input
             * Undo for line
             */
            //line mouse release - will create line from mouse press to mouse released
            //length and direction may vary depending on user input
            //Undo for line
            else if(linebtn.isSelected()) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            }
            /**
             * rectangle mouse release - will create rectangle from mouse press to mouse release
             * size and parameters may vary depending on user input
             * Undo for rectangle
             */
            //rectangle mouse release - will create rectangle from mouse press to mouse release
            //size and parameters may vary depending on user input
            //Undo for rectangle
            else if(rectbtn.isSelected()) {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                if(rect.getX() > e.getX()) {
                    rect.setX(e.getX());
                }
                if(rect.getY() > e.getY()) {
                    rect.setY(e.getY());
                }
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            }
            /**
             * Circle mouse release - will create circle from mouse press to mouse release
             * Size and parameters may vary depending on user input
             * Undo for circle
             */
            //circle mouse release - will create circle from mouse press to mouse release
            //size and parameters may vary depending on user input
            //Undo for circle
            else if(circlebtn.isSelected()) {
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

                if(circ.getCenterX() > e.getX()) {
                    circ.setCenterX(e.getX());
                }
                if(circ.getCenterY() > e.getY()) {
                    circ.setCenterY(e.getY());
                }
                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

                undoHistory.push(new Circle(circ.getCenterX(), circ.getCenterY(), circ.getRadius()));
            }
            /**
             * Ellipse mouse release - will create ellipse from mouse press to mouse release
             * Size and parameters may vary depending on user input
             * Undo for ellipse
             */
            //ellipse mouse release - will create ellipse from mouse press to mouse release
            //size and parameters may vary depending on user input
            //Undo for ellipse
            else if(elpslebtn.isSelected()) {
                elps.setRadiusX(Math.abs(e.getX() - elps.getCenterX()));
                elps.setRadiusY(Math.abs(e.getY() - elps.getCenterY()));

                if(elps.getCenterX() > e.getX()) {
                    elps.setCenterX(e.getX());
                }
                if(elps.getCenterY() > e.getY()) {
                    elps.setCenterY(e.getY());
                }
                gc.strokeOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());
                gc.fillOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());

                undoHistory.push(new Ellipse(elps.getCenterX(), elps.getCenterY(), elps.getCenterX(), elps.getCenterY()));
            }
            /**
             * Will redo the undo for features above
             */
            //Will redo the undo for features above
            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(gc.getFill());
            lastUndo.setStroke(gc.getStroke());
            lastUndo.setStrokeWidth(gc.getLineWidth());

        });
        /**
         * Color picker for line
         */
        // color picker for line
        cpLine.setOnAction(e->{
            gc.setStroke(cpLine.getValue());
        });
        /**
         * Color picker for fill
         */
        //color picker for fill
        cpFill.setOnAction(e->{
            gc.setFill(cpFill.getValue());
        });

        /**
         *
         * Returns the width value after slider input
         * @return value width for slider
         */
        //slider for line width
        slider.valueProperty().addListener(e->{
            double width = slider.getValue();
            if(textBoxbtn.isSelected()){
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                line_width.setText(String.format("%.1f", width));
                return;
            }
            line_width.setText(String.format("%.1f", width));
            gc.setLineWidth(width);
        });

        /**
         * Open and Save action events
         */
        // Open and Save
        // Open
        open.setOnAction((e)->{
            FileChooser openFile = new FileChooser();
            openFile.setTitle("Open File");
            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    gc.drawImage(img, 0, 0);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });

        // Save
        save.setOnAction((e)->{
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save File");

            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(1080, 790);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });

        /**
         * Undo and Redo action event
         */
        //Undo and Redo
        //Undo
        undo.setOnAction(e->{
            if(!undoHistory.empty()){
                gc.clearRect(0, 0, 1080, 790);
                Shape removedShape = undoHistory.lastElement();
                if(removedShape.getClass() == Line.class) {
                    Line tempLine = (Line) removedShape;
                    tempLine.setFill(gc.getFill());
                    tempLine.setStroke(gc.getStroke());
                    tempLine.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));

                }
                else if(removedShape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) removedShape;
                    tempRect.setFill(gc.getFill());
                    tempRect.setStroke(gc.getStroke());
                    tempRect.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                }
                else if(removedShape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) removedShape;
                    tempCirc.setStrokeWidth(gc.getLineWidth());
                    tempCirc.setFill(gc.getFill());
                    tempCirc.setStroke(gc.getStroke());
                    redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                }
                else if(removedShape.getClass() == Ellipse.class) {
                    Ellipse tempElps = (Ellipse) removedShape;
                    tempElps.setFill(gc.getFill());
                    tempElps.setStroke(gc.getStroke());
                    tempElps.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                }
                Shape lastRedo = redoHistory.lastElement();
                lastRedo.setFill(removedShape.getFill());
                lastRedo.setStroke(removedShape.getStroke());
                lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
                undoHistory.pop();

                for(int i=0; i < undoHistory.size(); i++) {
                    Shape shape = undoHistory.elementAt(i);
                    if(shape.getClass() == Line.class) {
                        Line temp = (Line) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                    }
                    else if(shape.getClass() == Rectangle.class) {
                        Rectangle temp = (Rectangle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                        gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                    }
                    else if(shape.getClass() == Circle.class) {
                        Circle temp = (Circle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                    }
                    else if(shape.getClass() == Ellipse.class) {
                        Ellipse temp = (Ellipse) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                    }
                }
            } else {
                System.out.println("there is no action to undo");
            }
        });

        // Redo
        redo.setOnAction(e->{
            if(!redoHistory.empty()) {
                Shape shape = redoHistory.lastElement();
                gc.setLineWidth(shape.getStrokeWidth());
                gc.setStroke(shape.getStroke());
                gc.setFill(shape.getFill());

                redoHistory.pop();
                if(shape.getClass() == Line.class) {
                    Line tempLine = (Line) shape;
                    gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                    undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
                }
                else if(shape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) shape;
                    gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                    gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());

                    undoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                }
                else if(shape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) shape;
                    gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                    gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());

                    undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                }
                else if(shape.getClass() == Ellipse.class) {
                    Ellipse tempElps = (Ellipse) shape;
                    gc.fillOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                    gc.strokeOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());

                    undoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                }
                Shape lastUndo = undoHistory.lastElement();
                lastUndo.setFill(gc.getFill());
                lastUndo.setStroke(gc.getStroke());
                lastUndo.setStrokeWidth(gc.getLineWidth());
            } else {
                System.out.println("there is no action to redo");
            }
        });

        /**
         * Zoom and Zoom Out action event
         */
        //Zoom and Zoom Out
        // Zoom
        zoom.setOnAction((e)-> {
            Scale contentScale = new Scale(1, 1, 1);

            contentScale.setX(contentScale.getX() + contentScale.getX() * .1);
            contentScale.setY(contentScale.getY() + contentScale.getY() * .1);
            contentScale.setZ(contentScale.getZ() + contentScale.getZ() * .1);

            canvas.getTransforms().add(contentScale);
        });

        // Zoom Out
        zoomout.setOnAction((e)->{
            Scale contentScale = new Scale(1, 1, 1);

            contentScale.setX(contentScale.getX() + contentScale.getX() * -.1);
            contentScale.setY(contentScale.getY() + contentScale.getY() * -.1);
            contentScale.setZ(contentScale.getZ() + contentScale.getZ() * -.1);

            canvas.getTransforms().add(contentScale);
        });

        /**
         * Release notes and help action event
         */
        //Release notes and Help
        releaseNotes.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Release notes for Pain(t) application
             * @param event triggers event for release notes
             */
            @Override
            public void handle(ActionEvent event) {
                Label releaseLabel = new Label("New Features:\n"
                        + "Version 1 Updates: \n"
                        + "Added menu bar with the options, file, release notes and exit\n"
                        + "Added functions to each button for the menu bar categories\n"
                        + "\n"
                        + "Version 2 Updates:\n"
                        + "Added tool that allows for drawing\n"
                        + "Ability to insert image\n"
                        + "Added saving options for viewing imaage and canvas drawing\n"
                        + "\n"
                        + "Version 3 Updates:\n"
                        + "Added more tools such as line and shapes\n"
                        + "Added keyboard shortcut for save and close\n"
                        + "Added tabs\n"
                        + "\n"
                        + "Links:\n"
                        + "GitHub: ");

                //Displays patch notes in a pop-up window
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(releaseLabel);

                Scene secondScene = new Scene(secondaryLayout, 350, 400);

                Stage newWindow = new Stage();
                newWindow.setTitle("Release Notes");
                newWindow.setScene(secondScene);

                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                secondScene.getWindow().centerOnScreen();
                newWindow.show();
            }
        });

        // Help
        help.setOnAction((e)->{
            final Stage dialog = new Stage () ;
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner (primaryStage);
            VBox dialogVbox = new VBox(20);
            dialogVbox. getChildren().add (new Text("Help\n"
            + "\n"
            + "Q: How do I save my painting?\n"
            + "A: You can use the save button or control and S.\n"
            + "\n"
            + "Q: Will my progress be lost if I exit out?\n"
            + "A: No, a message will appear to make sure you would like to exit without saving.\n"
            + "\n"
            + "Q: Why are the scrollbars not visible all the time?\n"
            + "A: Scrollbars will only appear when the image does not fit on the canvas.\n"
            + "\n"
            + "Q: Where can I view what the newest features added are?\n"
            + "A: You can view the most recently added features using the release notes button.\n"));
            Scene dialogScene = new Scene (dialogVbox,500,500);
            dialog. setScene (dialogScene);
            dialog. show();
        });

        /**
         * Close and Smart save action events
         */
        //Close and Smart Save
        // Close
        close.setOnAction((e)->{
            Platform.exit();
        });

        //Smart Save

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            /**
             * Smart save feature for exit without saving
             * @param event event for smart save pop-up message
             */
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Smart Save");
                alert.setHeaderText("You have not saved. Do you wish to continue exiting?");
                alert.initOwner(primaryStage);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                }
            }
        });

        /* ----------STAGE & SCENE---------- */
        BorderPane pane = new BorderPane();
        pane.setLeft(btns);
        pane.setCenter(canvas);

        /**
         * Scrollbars for canvas
         * Only visible when image is bigger than canvas
         */
        //Scrollbars
        ScrollPane scrollBar = new ScrollPane();
        //Scroll bars will only display when needed
        //Image does not fit on canvas
        scrollBar.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollBar.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollBar.setContent(canvas);

        Scene scene = new Scene(pane, 1200, 800);

        primaryStage.setTitle("Paint");
        primaryStage.setScene(scene);
        primaryStage.show();

        /**
         * Keybinds for save, close, and clear
         */
        //Ctrl Save, shift Close, shift Clear
        KeyCombination a = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination b = new KeyCodeCombination(KeyCode.X, KeyCodeCombination.SHIFT_DOWN);
        KeyCombination c = new KeyCodeCombination(KeyCode.C, KeyCombination.SHIFT_DOWN);

        scene.setOnKeyPressed(event -> {
            if (a.match(event)) {
                FileChooser savefile = new FileChooser();
                savefile.setTitle("Save File");

                //Saves using ctrl s
                File file = savefile.showSaveDialog(primaryStage);
                if (file != null) {
                    try {
                        WritableImage writableImage = new WritableImage(1080, 790);
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        System.out.println("Error!");
                    }
                }
                //closes using shift x
            } else if (b.match(event)) {
                primaryStage.close();
                //clears using shift c
            } else if (c.match(event)){
                gc.clearRect(0,0,1080,790);
            }
        });
    }

    /**
     * launches javaFX arguments
     * @param args arguments to call the start method
     */
    public static void main(String[] args) {
        launch();
    }
}