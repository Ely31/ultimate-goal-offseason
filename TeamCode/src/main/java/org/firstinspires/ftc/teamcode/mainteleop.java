package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class mainteleop extends LinearOpMode {

    DcMotor FR = null;
    DcMotor FL = null;
    DcMotor BR = null;
    DcMotor BL = null;
    DcMotor shooter = null;
    DcMotor wobbleArm = null;
    DcMotor collectionMechanism = null;
    DcMotor neat = null;
    Servo ringPusher = null;
    Servo wobbleGoalGrabber = null;

    public void runOpMode() {

        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        collectionMechanism = hardwareMap.get(DcMotor.class, "collectionMechanism");
        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");
        neat = hardwareMap.get(DcMotor.class, "neat");

        wobbleGoalGrabber = hardwareMap.get(Servo.class, "wobbleGoalGrabber");
        ringPusher = hardwareMap.get(Servo.class, "ringPusher");
        ringPusher.setPosition(0.95);

        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        double intakepow;
        boolean prevy = false;
        boolean prevx = false;
        boolean prevb = false;
        boolean intaketoggle = false;
        boolean shootertoggle = false;
        boolean clawtoggle = false;
        waitForStart();
        while (opModeIsActive()) {

            double r = Math.hypot(gamepad1.left_stick_x, -1 * gamepad1.left_stick_y);
            double robotAngle = Math.atan2(-1 * gamepad1.left_stick_y, gamepad1.left_stick_x) + -20;
            double rightX = gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;

            // shooter toggler
            if (gamepad2.b && !prevb) { // if b pressed and prevx is false
                shootertoggle = !shootertoggle; // toggle shootertoggle when b is pressed
            }
            prevb = gamepad2.b;

            if (shootertoggle) { // run shooter if shootertoggle is true
                shooter.setPower(-0.5);
            } else {
                shooter.setPower(0);
            }

            // reverse intake if a is pressed
            if (gamepad2.a){
                intakepow = -1;
            } else intakepow = 1;

            // intake toggler
            if (gamepad2.x && !prevx) { // if x pressed and prevx is false
                intaketoggle = !intaketoggle;
            }
            prevx = gamepad2.x;
            if (intaketoggle) {
                collectionMechanism.setPower(intakepow);
                neat.setPower(intakepow);
            } else {
                collectionMechanism.setPower(0);
                neat.setPower(0);
            }
            // shoot a ring when a is pressed
            if (gamepad1.left_trigger > 0.1) {
                ringPusher.setPosition(0.6);
            }
                else {ringPusher.setPosition(0.95);
            }

            // toggle clawtoggle if y pressed
            if (gamepad2.y && !prevy) {
                clawtoggle = !clawtoggle;
            }
            prevy = gamepad2.y;
            // open wobble claw when toggle is true
            if (clawtoggle) {
                wobbleGoalGrabber.setPosition(1);
            } else {
                wobbleGoalGrabber.setPosition(0.25);
            }

            // control wobble arm with dpad
            if (gamepad2.dpad_down) {
                wobbleArm.setPower(-0.25);
            } else if (gamepad2.dpad_up){
                wobbleArm.setPower(0.25);
            } else wobbleArm.setPower(0);

            // slow mode
            if (gamepad1.right_trigger < 0.1) { // if left trigger not pressed
                FL.setPower(v1);
                FR.setPower(v2);
                BL.setPower(v3);
                BR.setPower(v4);
            } else if (gamepad1.right_trigger > 0.1) { // if left trigger pressed
                FL.setPower(v1 / 8);
                FR.setPower(v2 / 8);
                BL.setPower(v3 / 8);
                BR.setPower(v4 / 8);
                // drive slow
            }
            telemetry.update();
        }
    }
}