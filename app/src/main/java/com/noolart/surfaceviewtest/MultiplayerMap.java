package com.noolart.surfaceviewtest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class MultiplayerMap {



    int[][] textures = new int[8][8];
    Bitmap[] images;

    float width, height;
    Bitmap[] tools;
    Resources resources;
    ArrayList<Integer> cardsOnAllField;
    public String[] descriptions = new String[64];

    Paint paint = new Paint(Color.BLACK);


    public MultiplayerMap(float width, float height, Resources resources) {


        this.width = width;
        this.height = height;
        this.resources = resources;

        descriptions[10]="Землятресение-Поменяй 2 клетки поля местами!";
        descriptions[11]="Самолет-Перелети на любую клетку поля!";
        descriptions[12]="Маяк-Открой любые 4-неисследованных клетки поля!";
        descriptions[14]="Сундук с монетами 1-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[15]="Сундук с монетами 2-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[16]="Сундук с монетами 3-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[17]="Сундук с монетами 4-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[18]="Сундук с монетами 5-Чтобы взять монету,-нажми на среднюю кнопку";
        descriptions[19]="Просто трава-:>";
        descriptions[20]="Ход конем!-Сделай ход шахматным конем!";
        descriptions[63]="Палатка-Точка твоего появления!";


        images = new Bitmap[64];
        images[0] = BitmapFactory.decodeResource(resources, R.drawable.texture);
        images[1] = BitmapFactory.decodeResource(resources, R.drawable.strup);
        images[2] = BitmapFactory.decodeResource(resources, R.drawable.strdown);
        images[3] = BitmapFactory.decodeResource(resources, R.drawable.strleft);
        images[4] = BitmapFactory.decodeResource(resources, R.drawable.strright);
        images[5] = BitmapFactory.decodeResource(resources, R.drawable.balloon);
        images[6] = BitmapFactory.decodeResource(resources, R.drawable.catapultright);
        images[7] = BitmapFactory.decodeResource(resources, R.drawable.catapultleft);
        images[8] = BitmapFactory.decodeResource(resources, R.drawable.ice);
        images[9] = BitmapFactory.decodeResource(resources, R.drawable.death);
        images[10] = BitmapFactory.decodeResource(resources, R.drawable.earthquake);
        images[11] = BitmapFactory.decodeResource(resources, R.drawable.airplane);
        images[12] = BitmapFactory.decodeResource(resources, R.drawable.lighthouse);
        images[13] = BitmapFactory.decodeResource(resources,R.drawable.chest);
        images[14] = BitmapFactory.decodeResource(resources, R.drawable.chest1);
        images[15] = BitmapFactory.decodeResource(resources, R.drawable.chest2);
        images[16] = BitmapFactory.decodeResource(resources, R.drawable.chest3);
        images[17] = BitmapFactory.decodeResource(resources, R.drawable.chest4);
        images[18] = BitmapFactory.decodeResource(resources, R.drawable.chest5);
        images[19] = BitmapFactory.decodeResource(resources, R.drawable.grass);
        images[20] = BitmapFactory.decodeResource(resources, R.drawable.horse);
        images[21] = BitmapFactory.decodeResource(resources, R.drawable.strne);
        images[22] = BitmapFactory.decodeResource(resources, R.drawable.strse);
        images[23] = BitmapFactory.decodeResource(resources, R.drawable.strsw);
        images[24] = BitmapFactory.decodeResource(resources, R.drawable.strnw);



        images[58] = BitmapFactory.decodeResource(resources,R.drawable.home_blue);
        images[59] = BitmapFactory.decodeResource(resources,R.drawable.home_red);
        images[60] = BitmapFactory.decodeResource(resources,R.drawable.coin);
        images[61] = BitmapFactory.decodeResource(resources,R.drawable.brokenairplane);
        images[62] = BitmapFactory.decodeResource(resources,R.drawable.brokenlighthouse);
        images[63] = BitmapFactory.decodeResource(resources,R.drawable.start);

        tools = new Bitmap[5];
        tools[0] = BitmapFactory.decodeResource(resources, R.drawable.strup);
        tools[1] = BitmapFactory.decodeResource(resources, R.drawable.strleft);
        tools[2] = BitmapFactory.decodeResource(resources, R.drawable.texture);
        tools[3] = BitmapFactory.decodeResource(resources, R.drawable.strright);
        tools[4] = BitmapFactory.decodeResource(resources, R.drawable.strdown);

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
        textures[7][0] = 58;
        textures[0][7] = 59;
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
