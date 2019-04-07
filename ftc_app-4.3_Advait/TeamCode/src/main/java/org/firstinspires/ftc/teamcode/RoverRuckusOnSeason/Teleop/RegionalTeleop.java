package org.firstinspires.ftc.teamcode.RoverRuckusOnSeason.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


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

@TeleOp(name="Regional_Teleop", group="Linear Opmode")
@Disabled
public class RegionalTeleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor hangingMotor = null;
    private DcMotor intake = null;
    private DcMotor intakeExtender = null;
    private DcMotor scoring = null;
    private Servo dumper = null;
    private Servo intakeFlip1 = null;
    private Servo intakeFlip2 = null;


    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
       initMotors();

       double shift = 1;

       double dumperPos = 0;



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

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

            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);

            hangingMotor.setPower(gamepad2.right_stick_y);

            scoring.setPower(gamepad2.left_stick_y);


            if (gamepad1.left_bumper&&opModeIsActive()&&gamepad1.left_trigger==0){
                intakeFlip1.setPosition(0.45);
                intakeFlip2.setPosition(0.45);
            }
            else {
                intakeFlip1.setPosition(Range.clip(gamepad1.right_trigger, 0.3, 0.8));
                intakeFlip2.setPosition(Range.clip(gamepad1.right_trigger, 0.3, 0.8));
            }
            intake.setPower(gamepad2.right_trigger-gamepad2.left_trigger);

            dumper.setPosition(Range.clip(gamepad1.left_trigger, 0.42, 0.92));


            if (gamepad2.dpad_up){
                intakeExtender.setPower(1);
            }
            else if (gamepad2.dpad_down){
                intakeExtender.setPower(-1);
            }
            else {
                intakeExtender.setPower(0);
            }

            if (gamepad1.a){
                strafe(-0.4, 500);
            }


            if (gamepad1.right_bumper){
                shift = 0.4;
            }

            else {
                shift = 1;
            }



            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Shift", shift);
            telemetry.addData("dumper Position", dumperPos);
            telemetry.addData("Intake position", intakeFlip1.getPosition());
            telemetry.update();
        }
    }

    public void initMotors() {


        leftBackDrive = hardwareMap.get(DcMotor.class, "BackLeft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "BackRight");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "FrontLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "FrontRight");
        hangingMotor = hardwareMap.get(DcMotor.class, "HangingArm");
        intake = hardwareMap.get(DcMotor.class, "Intake");
        intakeExtender = hardwareMap.get(DcMotor.class, "IntakeExtend");
        scoring = hardwareMap.get(DcMotor.class, "Scoring");
        intakeFlip1 = hardwareMap.get(Servo.class, "IntakeFlip1");
        intakeFlip2 = hardwareMap.get(Servo.class, "IntakeFlip2");
        dumper = hardwareMap.get(Servo.class, "Dumper");



        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        intakeFlip2.setDirection(Servo.Direction.REVERSE);
        intakeFlip1.setDirection(Servo.Direction.FORWARD);

        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hangingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        scoring.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftBackDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        intake.setPower(0);
        intakeExtender.setPower(0);
        hangingMotor.setPower(0);
        scoring.setPower(0);
    }

    public void strafe(double power, int sleepTime){
        leftBackDrive.setPower(-power);
        leftFrontDrive.setPower(power);
        rightBackDrive.setPower(power);
        rightFrontDrive.setPower(-power);

        sleep(sleepTime);

        leftBackDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
    }


}