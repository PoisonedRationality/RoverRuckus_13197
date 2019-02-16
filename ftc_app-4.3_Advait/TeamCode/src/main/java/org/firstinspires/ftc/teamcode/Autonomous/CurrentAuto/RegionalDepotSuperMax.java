package org.firstinspires.ftc.teamcode.Autonomous.CurrentAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.VisionV2;

@Autonomous
//@Disabled
public class RegionalDepotSuperMax extends LinearOpMode{

    // call the hardware from the Robot class
    Robot robot = new Robot();
    //call the objects from the Vision class
    VisionV2 vision = new VisionV2();

    //Having all of our hardware and functions in a separate class allows us
    //to more easily organize our code. This way, whenever we need to use a function,
    //we can just call them from there instead of cluttering up the code in this program

    @Override
    public void runOpMode(){

        //Initialize hardware
        robot.initRobot(hardwareMap);
        //Initialize vision (Vuforia and TensorFlow)
        vision.initVision(hardwareMap);

        telemetry.addData("Status", "All set");
        telemetry.update();

        //Wait for Driver to press PLAY
        waitForStart();


        //Lower Down from latch
        robot.lowering(this);
//        sleep(100);



        //strafe out of latch
        robot.strafe(0.5, 450, this);
        sleep(100);

        //back up against lander to straighten out
        robot.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBackDrive.setPower(-0.3);
        robot.leftFrontDrive.setPower(-0.3);
        sleep(300);
        robot.stopDrivetrain();

        //activate tensorflow
        if (opModeIsActive()) {
            // Activate Tensor Flow Object Detection.
            if (vision.tfod != null) {
                vision.tfod.activate();
            }
        }
        //bring lead screw down
        //robot.hangingMotor.setPower(-1);
        boolean isGold = vision.checkForGold(this);
        telemetry.addData("Gold?", isGold);
        telemetry.update();
        //robot.hangingMotor.setPOwer(0);
        byte mineralPosition;
        if (isGold&&opModeIsActive()){
            double degreesToGold = vision.degreesToGold(this);
            if (degreesToGold>0&&opModeIsActive()){
                mineralPosition = 1;
            }
            else{
                mineralPosition = 2;
            }
        }
        else {
            mineralPosition = 3;
        }
        telemetry.addData("Mineral Position", mineralPosition);
        telemetry.update();


/*
        //set intake servos to a certain position so they don't come down
        //while the robot moves
        robot.intakeFlip1.setPosition(0.73);
        robot.intakeFlip2.setPosition(0.73);


        //go forward
        robot.moveWithEncoders(0.5, 600, this);
        sleep(100);

        //strafe over a little past the silver mineral
        robot.strafe(-0.5, 550, this);
        sleep(100);

        //bring lead screw down
        //robot.hangingMotor.setPower(-1);
        //go forward just a little bit more
        robot.moveWithEncoders(0.3, 300, this);

        //extend intake to put team marker in depot
        robot.intakeExtender.setPower(1);
        sleep(1350);
        robot.intakeExtender.setPower(0);

        //bring down servo halfway first so the team marker doesn't fly out
        robot.intakeFlip1.setPosition(0.55);
        robot.intakeFlip2.setPosition(0.55);

        sleep(300);

        //bring intake down all the way
        robot.intakeFlip1.setPosition(0.3);
        robot.intakeFlip2.setPosition(0.3);
        sleep(100);

        //outtake team marker
        robot.intake.setPower(-1);
        sleep(1300);
        robot.intake.setPower(0);

        //stop bringing lead screw down
        //robot.hangingMotor.setPower(0);

        //bring intake back up
        robot.intakeFlip1.setPosition(0.73);
        robot.intakeFlip2.setPosition(0.73);

        //retract intake
        robot.intakeExtender.setPower(-1);
        sleep(1000);
        sleep(350);
        robot.intakeExtender.setPower(0);
        sleep(100);

        //move back a little
        robot.moveWithEncoders(-0.3, 400, this);
        sleep(100);
        robot.reorientIMU(0, -0.3, 0.3, 0.5, this,
                0.9, 0.04, 0);

        switch (mineralPosition){
            case 1:
                robot.strafe(0.5, 1000, this);
                robot.moveWithEncoders(-0.3, 400, this);
                sleep(100);

                robot.intakeFlip1.setPosition(0.3);
                robot.intakeFlip2.setPosition(0.3);
                sleep(100);
                robot.intake.setPower(1);
                robot.moveWithEncoders(0.3, 700, this);
                sleep(500);

                robot.intakeFlip1.setPosition(0.78);
                robot.intakeFlip2.setPosition(0.78);

                robot.strafe(-0.5, 800, this);
                sleep(100);
                break;

            case 2:
                robot.strafe(0.5, 400, this);
                robot.moveWithEncoders(-0.3, 400, this);
                sleep(100);

                robot.intakeFlip1.setPosition(0.3);
                robot.intakeFlip2.setPosition(0.3);
                sleep(100);
                robot.intake.setPower(1);
                robot.moveWithEncoders(0.3, 700, this);
                sleep(500);

                robot.intakeFlip1.setPosition(0.78);
                robot.intakeFlip2.setPosition(0.78);
                break;

            case 3:

                robot.strafe(-0.5, 700, this);
                robot.moveWithEncoders(-0.3, 400, this);
                sleep(100);
                robot.reorientIMU(0, -0.3, 0.3, 0.5, this,
                        0.9, 0.03, 0);
                robot.intakeFlip1.setPosition(0.3);
                robot.intakeFlip2.setPosition(0.3);
                sleep(100);
                robot.intake.setPower(1);
                robot.moveWithEncoders(0.3, 700, this);
                sleep(500);

                robot.intakeFlip1.setPosition(0.78);
                robot.intakeFlip2.setPosition(0.78);

                robot.strafe(0.5, 1000, this);
            default:
                robot.strafe(0.5, 400, this);
                robot.moveWithEncoders(-0.3, 400, this);
                sleep(100);

                robot.intakeFlip1.setPosition(0.3);
                robot.intakeFlip2.setPosition(0.3);
                sleep(100);
                robot.intake.setPower(1);
                robot.moveWithEncoders(0.3, 700, this);
                sleep(500);

                robot.intakeFlip1.setPosition(0.78);
                robot.intakeFlip2.setPosition(0.78);
                break;
        }


        robot.intakeFlip1.setPosition(0.55);
        robot.intakeFlip2.setPosition(0.55);

        robot.moveWithEncoders(-0.4, 700, this);
        sleep(100);

        robot.scoring.setPower(1);
        sleep(2700);
        robot.scoring.setPower(0);

        robot.dumper.setPosition(0.89);
        sleep(500);

        robot.scoring.setPower(-1);
        robot.dumper.setPosition(0.41);
        robot.moveWithEncoders(0.5, 500, this);
        robot.reorientIMU(90, -0.3, 0.3, 0.5, this, 0.9, 0.05, 0.03);
        robot.scoring.setPower(0);
        robot.intakeFlip1.setPosition(0.75);
        robot.intakeFlip2.setPosition(0.75);
        robot.moveWithEncoders(0.7, 1100, this);
        robot.intakeFlip1.setPosition(0.4);
        robot.intakeFlip2.setPosition(0.4);
        sleep(10000);
    */}
}