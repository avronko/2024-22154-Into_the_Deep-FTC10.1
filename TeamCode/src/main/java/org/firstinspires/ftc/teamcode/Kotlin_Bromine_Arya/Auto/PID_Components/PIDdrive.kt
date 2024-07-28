package org.firstinspires.ftc.teamcode.Kotlin_Bromine_Arya.Auto.PID_Components

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.Kotlin_Bromine_Arya.Localizer.Localizer
import org.firstinspires.ftc.teamcode.Kotlin_Bromine_Arya.Util.PIDController
import kotlin.math.cos
import kotlin.math.sin


open class PIDdrive(hwMap: HardwareMap, private val localizer: Localizer){
    //constants
    private val X: PIDController = PIDController()
    private val Y: PIDController = PIDController()
    private val R: PIDController = PIDController()

    val rightBack: DcMotor = hwMap.get(DcMotorEx::class.java, "rightBack")
    val leftFront: DcMotor = hwMap.get(DcMotorEx::class.java, "leftFront")
    val rightFront: DcMotor = hwMap.get(DcMotorEx::class.java, "rightFront")
    val leftBack: DcMotor = hwMap.get(DcMotorEx::class.java, "leftBack")

    fun setPID(p: DoubleArray, i: DoubleArray, d:DoubleArray){
        X.setPIDF(p[0], i[0], d[0], 0.0)
        Y.setPIDF(p[1], i[1], d[0], 0.0)
        R.setPIDF(p[2], i[2], d[0], 0.0)
    }

    init {
        leftBack.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftFront.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightBack.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightFront.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftBack.direction = DcMotorSimple.Direction.REVERSE
        leftFront.direction = DcMotorSimple.Direction.REVERSE
    }
companion object{
    var x = 0.0
    var y =0.0
}
    fun driveTo(target: DoubleArray){
        var rx = localizer.heading.toDouble()

        //TODO(Move to A util class)
        while (rx> Math.PI){
            rx -= 2* Math.PI
        }
        while (rx< -Math.PI){
            rx += 2*Math.PI
        }

        val axial = Y.calculate(localizer.yPos,target[1])
        val lateral = X.calculate(localizer.xPos,target[0])
        val turn = R.calculate(rx, target[2])

        x= turn

        val rotX = lateral * cos(-rx) - axial * sin(-rx)
        val rotY = lateral * sin(-rx) + axial * cos(-rx)



        leftFront.power = (rotY + rotX - turn)
        leftBack.power = (rotY - rotX - turn)
        rightFront.power = (rotY - rotX + turn)
        rightBack.power = (rotY + rotX + turn)
    }



}
