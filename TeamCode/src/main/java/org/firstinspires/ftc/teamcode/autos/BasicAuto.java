package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class BasicAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
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

        waitForStart();

        backRight.setPower(0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        frontLeft.setPower(0.5);

        sleep(1000);

        backRight.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        frontLeft.setPower(0);
    }
}
