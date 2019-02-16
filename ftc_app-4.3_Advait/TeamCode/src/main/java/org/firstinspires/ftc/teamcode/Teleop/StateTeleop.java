package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.Robot;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="State_Teleop", group="Linear Opmode")
//@Disabled
public class StateTeleop extends LinearOpMode {

Robot robot = new Robot();
ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
       robot.initRobot(hardwareMap);

       double shift = 1;



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()){


            double leftBackPower;
            double rightBackPower;
            double leftFrontPower;
            double rightFrontPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;
            leftBackPower    = Range.clip((drive + turn - strafe)*shift, -1.0, 1.0) ;
            rightBackPower   = Range.clip((drive - turn + strafe)*shift, -1.0, 1.0) ;
            leftFrontPower = Range.clip((drive + turn + strafe)*shift, -1.0, 1.0) ;
            rightFrontPower = Range.clip((drive - turn - strafe)*shift, -1.0, 1.0) ;

            robot.leftBackDrive.setPower(leftBackPower);
            robot.rightBackDrive.setPower(rightBackPower);
            robot.leftFrontDrive.setPower(leftFrontPower);
            robot.rightFrontDrive.setPower(rightFrontPower);

            if (gamepad1.a){
                robot.stopDrivetrain();
                robot.hangingMotor.setPower(0);
                robot.scoring.setPower(0);
                robot.intakeExtender.setPower(0);
                robot.intake.setPower(0);
                robot.strafe(-0.4, 500, this);
            }

            if (gamepad1.right_bumper){
                shift = 0.4;
            }

            else {
                shift = 1;
            }

            robot.scoring.setPower(gamepad2.left_stick_y);

            robot.dumper.setPower(Range.clip(0.35*gamepad2.right_stick_y, -0.35, 0.25));

            robot.intake.setPower(0.6*(gamepad2.right_trigger-gamepad2.left_trigger));

            robot.intakeExtender.setPower(gamepad1.right_trigger-gamepad1.left_trigger);

            if (gamepad2.left_bumper){
                robot.intakeFlip1.setPosition(0.76);
                robot.intakeFlip2.setPosition(0.76);
            }
            else if (gamepad2.right_bumper){
                robot.intakeFlip1.setPosition(0.3);
                robot.intakeFlip2.setPosition(0.3);
            }
            else if (gamepad2.left_stick_button){
                robot.intakeFlip1.setPosition(0.55);
                robot.intakeFlip2.setPosition(0.55);
            }

            if (gamepad2.dpad_up){
                robot.hangingMotor.setPower(1);
            }
            else if (gamepad2.dpad_down){
                robot.hangingMotor.setPower(-1);
            }
            else {
                robot.hangingMotor.setPower(0);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Shift", shift);
            telemetry.addData("Intake position", robot.intakeFlip1.getPosition());
            telemetry.update();
        }
    }
}