package org.example.graaaaaaphixzy.UIStuff;

import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.SnapshotParameters;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.example.graaaaaaphixzy.gameStuff.Bunny;
import org.example.graaaaaaphixzy.gameStuff.Robot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MazeOnCanvas extends Canvas {

    private Window_1 controller;

    private Maze maze;

    public Maze getMaze() {  return maze;}

    public static final int CELL_SIZE = 40;
    public static final int WEB_PADDING = 10;

    public MazeOnCanvas(Window_1 input_controller) {super(850, 650);
         this.controller = input_controller;

       setOnMouseClicked(e -> handleClick(e.getX(), e.getY()))
        ;}

    private void handleClick(double mouseX, double mouseY) {

        if (maze == null) return;

        int col = (int)((mouseX - WEB_PADDING) / CELL_SIZE);
        int row = (int)((mouseY - WEB_PADDING) / CELL_SIZE);

        if (row < 0 || row >= maze.nr_rows ||
                col < 0 || col >= maze.nr_cols) {
            return;
        }

        Cell cell = maze.getCell(row, col);

        if (controller.editNorth && row >0) {
            cell.wall_N = !cell.wall_N;
                maze.getCell(row - 1, col).wall_S = cell.wall_N;
        }

        if (controller.editSouth && row < maze.nr_rows - 1) {
            cell.wall_S = !cell.wall_S;
                maze.getCell(row + 1, col).wall_N = cell.wall_S;
        }

        if (controller.editWest && col > 0) {
            cell.wall_W = !cell.wall_W;
                maze.getCell(row, col - 1).wall_E = cell.wall_W;
        }

        if (controller.editEast && col < maze.nr_cols - 1) {
            cell.wall_E = !cell.wall_E;
                maze.getCell(row, col + 1).wall_W = cell.wall_E;
        }

        draw(maze);
    }

     public void setMaze(Maze labirintul) { this.maze = labirintul;}

    public void draw(Maze labirintul) {

        this.maze = labirintul;
        if (maze == null) { return;}


            GraphicsContext gc = getGraphicsContext2D(); //creionn

        gc.clearRect(0, 0, getWidth(), getHeight()); // pt atunci cand apesi de mai multe ori butonul Create

        gc.setStroke(Color.RED);
        gc.strokeRect(
                0,
                0,
                maze.nr_cols*CELL_SIZE+WEB_PADDING*2,
                maze.nr_rows*CELL_SIZE+WEB_PADDING*2
        );


        gc.setStroke(Color.BLACK);
        for (int i = 0; i < maze.nr_rows; i++) {
            for (int j = 0; j < maze.nr_cols; j++) {

                Cell casuta = maze.getCell(i, j);
                double x = j * CELL_SIZE + WEB_PADDING; //trebuie double
                double y = i * CELL_SIZE + WEB_PADDING; // padding pt "gard" apoi labirint

                gc.setFill(Color.LIGHTGRAY);
                gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                if (casuta.wall_N) { gc.strokeLine(x,y,
                                                      x + CELL_SIZE,y);}

                if (casuta.wall_E) {gc.strokeLine(x + CELL_SIZE,y,
                                                                      x + CELL_SIZE,y + CELL_SIZE);}

                if (casuta.wall_S) {
                    gc.strokeLine( x, y + CELL_SIZE,
                                                        x + CELL_SIZE, y + CELL_SIZE  );}

                if (casuta.wall_W) {
                    gc.strokeLine(x,y,   x, y + CELL_SIZE);   }
            }

        }

        //BUNNY
        if (controller.getManager() != null) {
            Bunny b = controller.getManager().getBunny();

            double x = b.getCol() * CELL_SIZE + WEB_PADDING;
            double y = b.getRow() * CELL_SIZE + WEB_PADDING;

            gc.setFill(Color.YELLOW);
            gc.fillOval(x + 10, y + 10, 20, 20);
        }
        //

        //ROBOTS
        if (controller.getManager() != null) {

            for (Robot r : controller.getManager().getRobots()) {

                double x = r.getCol() * CELL_SIZE + WEB_PADDING;
                double y = r.getRow() * CELL_SIZE + WEB_PADDING;

                gc.setFill(Color.RED);
                gc.fillOval(x + 10, y + 10, 20, 20);

                gc.setStroke(Color.BLACK);
                gc.strokeOval(x + 10, y + 10, 20, 20);
            }
        }
        //
    }

    public void exportToPNG(String fileName) {

        WritableImage image = this.snapshot(new SnapshotParameters(), null);

        File file = new File(fileName + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Salvat ca png.");
        } catch (IOException e) { e.printStackTrace();  }
    }

}