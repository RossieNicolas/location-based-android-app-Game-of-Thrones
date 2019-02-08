package com.example.testapp;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

public class BusITWeekDatabaseHelper extends StoryLineDatabaseHelper {

    public BusITWeekDatabaseHelper() {
        super(65);
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {

        builder.addGPSTask("1")
                .location(49.209598, 16.615119)
                .radius(15)
                .simplePuzzle()
                .question("Write down the first letter of each word on the copper plate on the building.")
                .answer("muvbpef")
                .puzzleTime(30000)
                .puzzleDone()
                .taskDone();
        builder.addGPSTask("2")
                .location(49.210514,16.615306)
                .radius(15)
                .simplePuzzle()
                .question("What year is written on the ground?")
                .answer("2017")
                .puzzleTime(30000)
                .puzzleDone()
                .taskDone();
        builder.addGPSTask("3")
                .location(49.209783, 16.613530)
                .radius(15)
                .imageSelectPuzzle()
                .question("What is the colour of the building with the cat on?")
                .addImage(R.drawable.image_color1,false)
                .addImage(R.drawable.image_color2, false)
                .addImage(R.drawable.image_color3, false)
                .addImage(R.drawable.image_color4, true)
                .puzzleDone()
                .taskDone();
        builder.addGPSTask("4")
                .location(49.210186, 16.615453)
                .radius(15)
                .choicePuzzle()
                .question("What letter is written on the hidden door?")
                .addChoice("A",true)
                .addChoice("V",false)
                .addChoice("Q", false)
                .addChoice("C", false)
                .puzzleDone()
                .taskDone();
        builder.addGPSTask("5")
                .location(49.210206,16.614594)
                .radius(15)
                .choicePuzzle()
                .question("How many glass circles can be seen on the statues.")
                .addChoice("5",false)
                .addChoice("6",true)
                .addChoice("8", false)
                .addChoice("17", false)
                .puzzleDone()
                .taskDone();
        builder.addGPSTask("verdict")
                .location(49.210963, 16.616509)
                .radius(15)
                .imageSelectPuzzle()
                .question("Who killed king Joffrey?")
                .addImage(R.drawable.image_aryafinal,true)
                .addImage(R.drawable.image_daenerysfinal, false)
                .addImage(R.drawable.image_johnfinal, false)
                .addImage(R.drawable.image_hodorfinal, false)
                .addImage(R.drawable.image_the_night_kingfinal, false)
                .addImage(R.drawable.image_tyrionfinal, false)
                .puzzleDone()
                .taskDone();
    }
}
