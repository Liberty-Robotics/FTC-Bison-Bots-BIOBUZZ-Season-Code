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

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Pauses the code here until the Play button is pressed after init
        waitForStart();
        runtime.reset();

        double driveLowPower = 0.75;

        double frontLeftPower;
        double backLeftPower;

        double frontRightPower;
        double backRightPower;

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

            // Send calculated power to the motors and servos
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            /// Telemetry
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime);

            telemetry.addLine();
            telemetry.addLine("MOTORS");
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);

            telemetry.update();
        }
    }
}