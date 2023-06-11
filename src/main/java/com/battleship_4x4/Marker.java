package com.battleship_4x4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

public class Marker {

    private final Rectangle rectangle;
    private final int gridSize;
    Image miss = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/board/marker_miss_64.png")).toString());
    Image hit = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/board/marker_hit_64.png")).toString());
    Image explosion = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/explosion_with_marker_64.gif")).toString());
    Image miss_gif = new Image(Objects.requireNonNull(this.getClass().getResource("sprites/gifs/marker_miss_anim_64.gif")).toString());

    public Marker(int pos, boolean type, int gridSize){
        rectangle = new Rectangle(gridSize, gridSize);
        this.gridSize = gridSize;

        int x = (pos % 8);
        int y = (pos / 8);

        move(x, y);

        if (type){
            rectangle.setFill(new ImagePattern(miss_gif));
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2)));
            timeline.setOnFinished(e -> {
                rectangle.setFill(new ImagePattern(miss));
            });
            timeline.play();
        }
        else {
            rectangle.setFill(new ImagePattern(explosion));

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.7)));
            timeline.setOnFinished(e2 -> {
                rectangle.setFill(new ImagePattern(hit));
            });
            timeline.play();
        }
    }

    public void move (int x, int y) {
        rectangle.relocate(x * gridSize, y * gridSize);
    }

    public Rectangle getRectangle(){return this.rectangle;}
}
