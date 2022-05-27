package com.noolart.surfaceviewtest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMap {

    StorageClass storageClass = new StorageClass();

    int[][] textures = new int[8][8];
    Bitmap[] images = new Bitmap[64];

    float width, height;
    Resources resources;
    ArrayList<Integer> cardsOnAllField;
    public String[] descriptions = new String[64];

    Paint paint = new Paint(Color.BLACK);


    public GameMap(float width, float height, Resources resources) {


        this.width = width;
        this.height = height;
        this.resources = resources;

        Arrays.fill(descriptions, "");



        descriptions[10]="Землятресение-Поменяй 2 клетки поля местами!";
        descriptions[11]="Самолет-Перелети на любую клетку поля!";
        descriptions[12]="Маяк-Открой любые 4-неисследованных клетки поля!";
        descriptions[14]="Сундук с монетами 1-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[15]="Сундук с монетами 2-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[16]="Сундук с монетами 3-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[17]="Сундук с монетами 4-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[18]="Сундук с монетами 5-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[19]="Просто трава-:>";
        descriptions[20]="Ход конем!-Сделай ход шахматным конем";
        descriptions[63]="Палатка-Точка твоего появления!";


        for (int i = 0; i < 64; i++) {
            images[i] = BitmapFactory.decodeResource(resources, storageClass.images[i]);
        }


        cardsOnAllField = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            cardsOnAllField.add(1);
            cardsOnAllField.add(2);
            cardsOnAllField.add(3);
            cardsOnAllField.add(4);
            cardsOnAllField.add(21);
            cardsOnAllField.add(22);
            cardsOnAllField.add(23);
            cardsOnAllField.add(24);

            cardsOnAllField.add(14);
            cardsOnAllField.add(15);
            cardsOnAllField.add(16);



            cardsOnAllField.add(19);
            cardsOnAllField.add(19);


        }
        for (int i = 0; i < 2; i++) {
            cardsOnAllField.add(5);
            cardsOnAllField.add(6);
            cardsOnAllField.add(7);
            cardsOnAllField.add(11);
            cardsOnAllField.add(12);
            cardsOnAllField.add(14);
            cardsOnAllField.add(17);
            cardsOnAllField.add(20);
            cardsOnAllField.add(19);
            cardsOnAllField.add(8);
        }
        cardsOnAllField.add(15);
        cardsOnAllField.add(18);
        cardsOnAllField.add(9);
        cardsOnAllField.add(10);



        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                textures[i][j] = 0;
            }
        }
        textures[7][0]=63;
    }


    public void draw(Canvas canvas) {

        int squareSide = (int) width / 10;

        int x = (int) (width - 8 * squareSide) / 2;
        int y = (int) (height - 8 * squareSide) / 2;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                canvas.drawBitmap(images[textures[i][j]], null, new Rect(x, y, x + 108, y + 108), paint);
                x += squareSide;
                if (x >= width - 1 - squareSide) {
                    break;
                }
            }
            y += squareSide;
            x -= squareSide * 8;
        }
    }
}
