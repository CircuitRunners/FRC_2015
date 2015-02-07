package org.usfirst.frc.team1002.robot.commands;

import org.usfirst.frc.team1002.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LiftDown extends Command {

    public LiftDown() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.forklift);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.forklift.LiftDown();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return !Robot.forklift.getLimitBot();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.forklift.stopLift();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.forklift.stopLift();
    }
}