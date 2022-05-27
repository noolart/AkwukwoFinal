package com.noolart.surfaceviewtest;

import java.util.Arrays;

public class StorageClass {

    public String[] descriptions;
    public int[] images;
    public int[] skins;
    public int[] costs;
    public String[] shopTitles;


    public StorageClass(){

        this.descriptions = new String[64];
        this.images = new int[64];
        this.skins = new int[10];
        this.costs = new int[10];
        this.shopTitles = new String[10];

        Arrays.fill(descriptions,"");
        descriptions[10]="Землятресение-Поменяй 2 клетки поля местами!";
        descriptions[11]="Самолет-Перелети на любую клетку поля!";
        descriptions[12]="Маяк-Открой любые 4 неисследованных клетки поля!";
        descriptions[14]="Сундук с монетами-Чтобы взять монету, нажми на среднюю кнопку";
        descriptions[19]="Просто трава-:>";
        descriptions[20]="Ход конем!-Сделай ход как шахматным конем";
        descriptions[63]="Палатка-Точка твоего появления!";

        images[0]=R.drawable.texture;
        images[1]=R.drawable.strup;
        images[2]=R.drawable.strdown;
        images[3]=R.drawable.strleft;
        images[4]=R.drawable.strright;
        images[5]=R.drawable.balloon;
        images[6]=R.drawable.catapultright;
        images[7]=R.drawable.catapultleft;
        images[8]=R.drawable.ice;
        images[9]=R.drawable.death;
        images[10]=R.drawable.earthquake;
        images[11]=R.drawable.airplane;
        images[12]=R.drawable.lighthouse;
        images[13]=R.drawable.chest;
        images[14]=R.drawable.chest1;
        images[15]=R.drawable.chest2;
        images[16]=R.drawable.chest3;
        images[17]=R.drawable.chest4;
        images[18]=R.drawable.chest5;
        images[19]=R.drawable.grass;
        images[20]=R.drawable.horse;
        images[21]=R.drawable.strne;
        images[22]=R.drawable.strse;
        images[23]=R.drawable.strsw;
        images[24]=R.drawable.strnw;
        images[60]=R.drawable.coin;
        images[61]=R.drawable.brokenairplane;
        images[62]=R.drawable.brokenlighthouse;
        images[63]=R.drawable.start;

        costs[0] = -1;
        costs[1] = 25;
        costs[2] = 25;
        costs[3] = 25;
        costs[4] = 50;
        costs[5] = 70;
        costs[6] = 70;
        costs[7] = 70;
        costs[8] = 100;
        costs[9] = 100;


       skins[0] = R.drawable.main;
       skins[1]  = R.drawable.minion1;
       skins[2]  = R.drawable.minion2;
       skins[3]  = R.drawable.minion3;
       skins[4]  = R.drawable.minion4;
       skins[5] = R.drawable.crow;
       skins[6] = R.drawable.warrior;
       skins[7] = R.drawable.monster;
       skins[8] = R.drawable.unicorn;
       skins[9] = R.drawable.black_death;

      shopTitles[0] = "Обычный";
      shopTitles[1] = "Красный";
      shopTitles[2] = "Синий";
      shopTitles[3] = "Желтый";
      shopTitles[4] = "Зеленый";
      shopTitles[5] = "Ниндзя";
      shopTitles[6] = "Варвар";
      shopTitles[7] = "Монстр";
      shopTitles[8] = "Единорог";
      shopTitles[9] = "Смерть";


    }

}
