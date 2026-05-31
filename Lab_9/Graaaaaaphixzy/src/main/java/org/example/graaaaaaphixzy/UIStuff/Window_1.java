package org.example.graaaaaaphixzy.UIStuff;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.util.Duration;
import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.example.graaaaaaphixzy.gameStuff.Bunny;
import org.example.graaaaaaphixzy.gameStuff.EnemyGamePlan;
import org.example.graaaaaaphixzy.gameStuff.Manager;
import org.example.graaaaaaphixzy.gameStuff.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.graaaaaaphixzy.UIStuff.MazeOnCanvas.CELL_SIZE;
import static org.example.graaaaaaphixzy.UIStuff.MazeOnCanvas.WEB_PADDING;

public class Window_1 {

    //bunny game
    private Maze labirint;
    private Manager manager;
    private EnemyGamePlan memory;
    private Timeline renderLoop;
    //bunny game
    private Timeline timeline;
    private Maze new_maze;

    int entrace_1 =0; int entrance_2=0;

    private BorderPane baza; //top+bottom+ center+ left +right

    private TextField rowsField; //input field
    private TextField colsField;
    private Button paintMazeButton;

    private MazeOnCanvas maze_Canvas;

    private Button random;
    private Button exitButton;
    private Button printPNG;
    private Button perfect;
    private Button game;

    private Button stopButton;

    private TextField robotCountField;
    private ComboBox<Bunny.Algorithm> bunnyAlgoBox;
    private ComboBox<Robot.Algorithm> robotAlgoBox;
    private Slider speedSlider;
    private Label speedLabel;

    private void createTopPanel() {

        HBox topPanel = new HBox(10);

        topPanel.setPadding(new Insets(10));// spune elementelor sa pastreze o distanta minima fata de margini

        Label rowsLabel = new Label("Rows:");
        rowsField = new TextField("10");
        rowsField.setPrefWidth(60);// dimensiune buton

        Label colsLabel = new Label("Columns:");
        colsField = new TextField("10");
        colsField.setPrefWidth(65);

         paintMazeButton = new Button("Create Maze");

        paintMazeButton.setOnAction(event -> createMaze());

        topPanel.getChildren().addAll( rowsLabel, rowsField, colsLabel, colsField, paintMazeButton );

         baza.setTop(topPanel);
    }


    private CheckBox box_N;
    private CheckBox box_S;
    private CheckBox box_E;
    private CheckBox box_W;
    public boolean editNorth = false;
    public boolean editSouth = false;
    public boolean editWest  = false;
    public boolean editEast  = false;

    private void createBottomPanel() {

        VBox bottomContainer = new VBox(6);

        bottomContainer.setPadding(new Insets(8));


        HBox bottomPanel = new HBox(10);

        bottomPanel.setPadding(new Insets(10));

        random = new Button("DIG!");

        exitButton = new Button("Exit");

        random.setOnAction(event -> randomizeMaze());

        box_N = new CheckBox("N");
        box_S = new CheckBox("S");
        box_E  = new CheckBox("E");
        box_W  = new CheckBox("W");

        box_N.setOnAction(e -> editNorth = box_N.isSelected());
        box_S.setOnAction(e -> editSouth = box_S.isSelected());
        box_W.setOnAction(e -> editWest = box_W.isSelected());
        box_E.setOnAction(e -> editEast = box_E.isSelected());

        printPNG = new Button("PNG SPER SPER");
        printPNG.setOnAction(event -> maze_Canvas.exportToPNG("denumire"));

        perfect = new Button("Is Perfect???");
        perfect.setOnAction(event ->{
            new_maze.checkIfPerfect(entrace_1,0,entrance_2, new_maze.nr_cols - 1);

        });

        game = new Button("Start Simulation");
        game.setOnAction(e -> startSimulation());

        stopButton = new Button("Stop");
        stopButton.setOnAction(e -> stopSimulation());

        exitButton.setOnAction(event -> System.exit(0));

        bottomPanel.getChildren().addAll(random, box_N, box_S, box_W, box_E, printPNG, perfect, exitButton, game, stopButton);
        
        HBox simRow = new HBox(10);
        simRow.setPadding(new Insets(2));

        robotCountField = new TextField("3");
        robotCountField.setPrefWidth(40);

        bunnyAlgoBox = new ComboBox<>();
        bunnyAlgoBox.getItems().addAll(Bunny.Algorithm.values());
        bunnyAlgoBox.setValue(Bunny.Algorithm.RANDOM);

        robotAlgoBox = new ComboBox<>();
        robotAlgoBox.getItems().addAll(Robot.Algorithm.values());
        robotAlgoBox.setValue(Robot.Algorithm.RANDOM);

        speedSlider = new Slider(0.25, 4.0, 1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(0.75);
        speedSlider.setPrefWidth(160);
        speedLabel = new Label("Speed: 1.00x");
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                speedLabel.setText(String.format("Speed: %.2fx", newVal.doubleValue()))
        );

        simRow.getChildren().addAll(
                new Label("Robots:"),   robotCountField,
                new Label("Bunny AI:"), bunnyAlgoBox,
                new Label("Robot AI:"), robotAlgoBox,
                speedLabel,             speedSlider
        );

        bottomContainer.getChildren().addAll(bottomPanel, simRow);
        baza.setBottom(bottomContainer);
    }

    private void stopSimulation() {
        if (manager != null) manager.stopGame();
        if (renderLoop != null) { renderLoop.stop(); renderLoop = null; }
    }

    private boolean positionTaken(int r, int c, List<long[]> taken) {
        for (long[] pos : taken) {
            if (pos[0] == r && pos[1] == c) return true;
        }
        return false;
    }



    public Window_1() {

        baza = new BorderPane();

        createTopPanel(); // nu trebuie "baza.setTop()" ??

        maze_Canvas = new MazeOnCanvas(this);
        baza.setCenter(maze_Canvas);

        createBottomPanel();
    }



    private void createMaze() {

        int rows = Integer.parseInt(rowsField.getText());
          int cols = Integer.parseInt(colsField.getText());

        labirint = new Maze(rows, cols);

        //maze_Canvas.setMaze(labirintul);  //Nu mere maze_Canvas= new Maze(10,10); fiindca vreea = MazeOnCanvas
        maze_Canvas.draw(labirint);
    }

    private void performMazeStep() {

        if (new_maze.huntAround( new_maze.current_row, new_maze.current_col))
        { maze_Canvas.draw(new_maze); return; }

        if (new_maze.searchHunt())
        { maze_Canvas.draw(new_maze); return; }

        entrace_1=new_maze.cutEntrance(true);
        entrance_2=new_maze.cutEntrance(false);
        maze_Canvas.draw(new_maze);

        maze_Canvas.draw(new_maze);

        timeline.stop();

    }

    private void randomizeMaze() {

        if (maze_Canvas == null) { return; }

        new_maze = maze_Canvas.getMaze();

        if (new_maze == null) { return; }

       new_maze.prepareHuntPray();

        timeline = new Timeline(
                   new KeyFrame(Duration.millis(50), event -> { performMazeStep();}));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();


        //FARA "ANIMATIE"


        /*int checks = new_maze.nr_rows * new_maze.nr_cols;

        do {
            while (new_maze.huntAround(new_maze.current_row, new_maze.current_col)) {
                maze_Canvas.draw(new_maze);
                //timeline.play();};
            }

            //timeline.play();
        } while (new_maze.searchHunt() == true);

        entrace_1= new_maze.cutEntrance(true);
        entrance_2= new_maze.cutEntrance(false);
        maze_Canvas.draw(new_maze);
    }*/


       /* for (int i = 0; i < new_maze.nr_rows; i++) {
            for (int j = 0; j < new_maze.nr_cols; j++) {

                Cell cell = new_maze.getCell(row, col);


                cell.wall_N = Math.random() < 0.5;
                cell.wall_E = Math.random() < 0.5;
                cell.wall_S = Math.random() < 0.5;
                cell.wall_W = Math.random() < 0.5;
            }*/
        }


    public Parent getRoot() {return baza;} //fiindca se plange HelloApplication
    //======================================================
    //Gamestuff:

    public Manager getManager() {return manager;}

    private void startRenderLoop() {

        if (renderLoop != null) {
            renderLoop.stop();
        }

        renderLoop = new Timeline(
                new KeyFrame(Duration.millis(30), e -> {
                    if (maze_Canvas.getMaze() != null) {
                        maze_Canvas.draw(labirint);
                    }

                    if (memory != null && memory.gameOver) {
                        renderLoop.stop();
                    }
                })
        );

        renderLoop.setCycleCount(Animation.INDEFINITE);
        renderLoop.play();
    }


    private void startSimulation() {

        if (manager != null) { manager.stopGame(); }

        if (labirint == null) return;

        memory = new EnemyGamePlan();

        Bunny bunny = new Bunny(
                "Bunny",
                labirint,
                entrace_1, 0,
                entrance_2,
                labirint.nr_cols - 1,
                (int)(200 / speedSlider.getValue()),
                memory,
                bunnyAlgoBox.getValue()
        );

        List<Robot> robots = new ArrayList<>();
        int robotCount = Integer.parseInt(robotCountField.getText());
        Robot.Algorithm robotAlgo = robotAlgoBox.getValue();
        Random rng = new Random();
        List<long[]> taken = new ArrayList<>();
        taken.add(new long[]{entrace_1, 0});

        for (int i = 0; i < robotCount; i++) {
            int rr, rc;
            do {
                rr = rng.nextInt(labirint.nr_rows);
                rc = rng.nextInt(labirint.nr_cols);
            } while (positionTaken(rr, rc, taken));
            taken.add(new long[]{rr, rc});
            robots.add(new Robot("R." + i, labirint, rr, rc,
                    (int)(500 / speedSlider.getValue()), memory, 1, robotAlgo));
        }

        manager = new Manager(labirint, bunny, robots, memory);

        labirint.getCell(bunny.getRow(), bunny.getCol()).enter(bunny);
        for (Robot r : robots) labirint.getCell(r.getRow(), r.getCol()).enter(r);

        manager.startGame();
        startRenderLoop();
    }

}

