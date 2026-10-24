package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class BasicTeleop extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initializing the motor direction and names
        /// Driving
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        DcMotor backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        backLeft.setDirection(DcMotor.Direction.FORWARD);

        DcMotor frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DcMotor backRight = hardwareMap.get(DcMotor.class, "rightBack");
        backRight.setDirection(DcMotor.Direction.REVERSE);

        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.FORWARD);

        DcMotor transfer = hardwareMap.get(DcMotor.class, "transfer");
        transfer.setDirection(DcMotor.Direction.FORWARD);

        DcMotor launch = hardwareMap.get(DcMotor.class, "launch");
        launch.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Pauses the code here until the Play button is pressed after init
        waitForStart();
        runtime.reset();

        double driveLowPower = 0.75;
        double launchLowPower = 0.6;
        double transferLowPower = 1.0;
        double intakeLowPower = 1.0;

        double intakePower;
        double transferPower;
        double launchPower;

        double frontLeftPower;
        double backLeftPower;

        double frontRightPower;
        double backRightPower;

        boolean intaking = false;
        boolean launching = false;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            /// Driving Input
            double max;

            // Get Movement Input
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // Calculate Wheel Power from Input (I don't know how this was figured out don't change this)
            frontLeftPower = axial + lateral + yaw;
            frontRightPower = axial - lateral - yaw;
            backLeftPower = axial - lateral + yaw;
            backRightPower = axial + lateral - yaw;

            // Limit wheel power to the maximum it can handle
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }

            frontLeftPower = frontLeftPower * driveLowPower;
            frontRightPower = frontRightPower * driveLowPower;
            backLeftPower = backLeftPower * driveLowPower;
            backRightPower = backRightPower * driveLowPower;

            transferPower = gamepad2.right_trigger * transferLowPower;

            if (gamepad2.leftBumperWasPressed()) intaking = true;
            else if (gamepad2.startWasPressed()) intaking = false;

            if (gamepad2.rightBumperWasPressed()) launching = true;
            else if (gamepad2.bWasPressed()) launching = false;

            if (intaking) intakePower = intakeLowPower;
            else intakePower = 0.0;

            if (launching) launchPower = launchLowPower;
            else launchPower = 0.0;

            // Send calculated power to the motors and servos
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            intake.setPower(intakePower);
            launch.setPower(launchPower);
            transfer.setPower(transferPower);

            /// Telemetry
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime);

            telemetry.addLine();
            telemetry.addLine("MOTORS");
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.addData("Launch", "%4.2f", launchPower);
            telemetry.addData("Intake", "%4.2f", intakePower);
            telemetry.addData("Transfer", "%4.2", transferPower);

            telemetry.update();
        }
    }
}