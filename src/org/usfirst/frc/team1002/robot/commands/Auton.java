package org.usfirst.frc.team1002.robot.commands;

import org.usfirst.frc.team1002.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;

public class Auton {

    public static void run() {
        Drive.move(-1, 0, 0);
        Timer.delay(1);
        Drive.move(0, 0, 0);
        Timer.delay(0.1);
        new LiftDown().start();
        Timer.delay(2);
        new LiftUp().start();
        Timer.delay(2);
        Drive.move(1, 0, 0);
        Timer.delay(5);
        Drive.move(0, 0, 0);
    }
}
