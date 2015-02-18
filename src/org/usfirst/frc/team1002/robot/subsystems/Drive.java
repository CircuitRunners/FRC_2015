package org.usfirst.frc.team1002.robot.subsystems;

import org.usfirst.frc.team1002.robot.Robot;
import org.usfirst.frc.team1002.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drive extends Subsystem {

    // Motors
    public static Victor leftFrontMotor;
    public static Victor rightFrontMotor;
    public static Victor leftBackMotor;
    public static Victor rightBackMotor;

    // Gyro
    public static Gyro gyro;

    /*
     * Encoders public static Encoder leftFrontEncoder; public static Encoder
     * rightFrontEncoder; public static Encoder leftBackEncoder; public static
     * Encoder rightBackEncoder;
     */

    // private static final double DISTANCE_PER_PULSE = 0;

    // RobotDrive
    public static RobotDrive robotDrive;

    public static boolean isCartesian = false;
    public static double multiplier = 1;

    // Deadzone Constants
    public static final double SPIN_DEADZONE_CONSTANT = 0.2;
    public static final double STICK_DEADZONE_CONSTANT = 0.2;
    private static final double SPIN_REDUCTION_CONSTANT = 0.9;
    private static final double SPIN_POWER = 2;
    private static final double STICK_POWER = 2;

    public Drive() {
        leftFrontMotor = new Victor(RobotMap.motors[0]);
        rightFrontMotor = new Victor(RobotMap.motors[1]);
        rightBackMotor = new Victor(RobotMap.motors[2]);
        leftBackMotor = new Victor(RobotMap.motors[3]);

        gyro = new Gyro(RobotMap.gyro);

        /*
         * leftFrontEncoder = new Encoder(RobotMap.encoders[0][0],
         * RobotMap.encoders[0][1]); rightFrontEncoder = new
         * Encoder(RobotMap.encoders[1][0], RobotMap.encoders[1][1]);
         * rightBackEncoder = new Encoder(RobotMap.encoders[2][0],
         * RobotMap.encoders[2][1]); leftBackEncoder = new
         * Encoder(RobotMap.encoders[3][0], RobotMap.encoders[3][1]);
         * leftFrontEncoder.reset(); rightFrontEncoder.reset();
         * rightBackEncoder.reset(); leftBackEncoder.reset();
         * leftFrontEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
         * rightFrontEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
         * rightBackEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
         * leftBackEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
         */

        robotDrive = new RobotDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }

    /**
     * Default move function. Polar is used unless changed.
     *
     * @param x is the speed to move in the x-direction
     * @param y is the speed to move in the y-direction
     * @param rotation is the speed to rotate
     * @param isCartesian defines whether Cartesian or polar is to be used
     */
    public static void move(double x, double y, double rotation) {
        if (isCartesian) {
            robotDrive.mecanumDrive_Cartesian(x, y, rotation, gyro.getAngle()); // Bad
                                                                                // drive
                                                                                // pretend
                                                                                // it
                                                                                // doesn't
                                                                                // exist
        } else {
            robotDrive.mecanumDrive_Cartesian(x, y, rotation, 0); // "Polar"
                                                                  // drive
        }
    }

    /**
     * Alternate move function. Takes joystick input and automatically throttles
     * and sets.
     *
     * @param joystickis the joystick used
     * @param isCartesian defines whether Cartesian or polar is to be used
     */
    public static void move(GenericHID joystick) {
        // Change the multiplier
        if (Robot.xbox.getRawButton(5)) {
            Drive.multiplier = 0.5;
        } else if (Robot.xbox.getRawButton(6)) {
            Drive.multiplier = 1;
        }
        if (isCartesian) {
            robotDrive.mecanumDrive_Cartesian(throttle(joystick.getX()), throttle(joystick.getY()),
                    spinThrottle(joystick.getRawAxis(4)), gyro.getAngle());
        } else {
            robotDrive.mecanumDrive_Cartesian(throttle(joystick.getX()), throttle(joystick.getY()),
                    spinThrottle(joystick.getRawAxis(4)), 0);
        }
    }

    /**
     * Move function using encoder input
     *
     * @param turns is the number of encoder turns to move
     */
    /*
     * public static void moveTurns(int turns) { while
     * (Math.abs((leftFrontEncoder.getRaw() + leftBackEncoder.getRaw() +
     * rightBackEncoder.getRaw() + rightFrontEncoder.getRaw()) / 4) < turns) {
     * move(-0.5, 0, 0); } move(0, 0, 0); }
     */

    /**
     * Throttles joystick input using a deadzone and throttle scaler
     *
     * @param input is the raw input from the joystick
     * @return throttled input
     */
    public static double throttle(double input) {
        double output = input;
        if (input > -STICK_DEADZONE_CONSTANT && input < STICK_DEADZONE_CONSTANT) {
            output = 0; // If within deadzone then don't move to stop unintended
            // inputs and ghost inputs
        }
        return specPow(output, STICK_POWER) * multiplier;
    }

    /**
     * Throttles joystick twist input using a deadzone and throttle scaler
     *
     * @param input is the raw input from the joystick
     * @return throttled spin input
     */
    public static double spinThrottle(double input) {
        double output = input;
        if (input > -SPIN_DEADZONE_CONSTANT && input < SPIN_DEADZONE_CONSTANT) {
            output = 0;
        }
        return SPIN_REDUCTION_CONSTANT * specPow(output, SPIN_POWER) * multiplier;
    }

    private static double specPow(double base, double exp) {
        return Math.signum(base) * Math.abs(Math.pow(base, exp));
    }

    @Override
    public void initDefaultCommand() {
    }
}
