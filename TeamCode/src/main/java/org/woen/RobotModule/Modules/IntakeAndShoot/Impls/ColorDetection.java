package org.woen.RobotModule.Modules.IntakeAndShoot.Impls;

import static java.lang.Math.sqrt;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;

import org.woen.Hardware.Devices.ColorSensor.Impl.ColorSensorImpl;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Hardware.Factories.HardwareFactory;
import org.woen.Util.Color.BALLS_COLOR;
import org.woen.RobotModule.Modules.IntakeAndShoot.ColorDetection.ColorsBallsConfig;
import org.woen.Util.Color.BALLS_COLOR;

@Config
public class ColorDetection{




    public static double rangeOfCos = 0.96; /// чем больше тем точнее

    ColorSensor sensor;

    public ColorDetection(AdafruitI2cColorSensor sensor){
        this.sensor = new ColorSensorImpl(sensor);
    }

    private boolean calcLenght(long r, long g, long b){

        double cosA, len1, len2, len3;

        len1 = sqrt(r^2 + b^2 + g^2); //TODO (r+g+b)/3 or sqrt(r^2 + b^2 + g^2)
        len2 = sqrt(sensor.getRed()^2 + sensor.getGreen()^2 + sensor.getBlue()^2);
        len3 = sqrt((r - sensor.getRed())^2 + ( g - sensor.getGreen())^2 + (b - sensor.getBlue()));

        if(len1 * len2 != 0 ){
            cosA = (len1*len1 + len2*len1 - len3*len3)/(2*len1*len2);
        }else cosA = 0;

        if(cosA > rangeOfCos && len2 > 5){
            return true;
        }
        return false;
    }

    public BALLS_COLOR def_color_easy(){

        if(sensor.getBlue() > 20 && sensor.getGreen() > 20 && sensor.getRed() > 20){
            if(sensor.getGreen() > sensor.getRed() && sensor.getGreen() > sensor.getBlue()){
                return BALLS_COLOR.GREEN;
            }
            else return BALLS_COLOR.PURPLE;
        }
        else return BALLS_COLOR.VOID;

    }

    public BALLS_COLOR def_color(){

        if(calcLenght(ColorsBallsConfig.rGreen, ColorsBallsConfig.gGreen, ColorsBallsConfig.bGreen)){
            return BALLS_COLOR.GREEN;
        }
        if(calcLenght( ColorsBallsConfig.rPurple, ColorsBallsConfig.gPurple, ColorsBallsConfig.bPurple)){
            return BALLS_COLOR.PURPLE;
        }
        return BALLS_COLOR.VOID;

    }

    public BALLS_COLOR defColorWithFilter(){

        BALLS_COLOR[] balls = new BALLS_COLOR[5];



        for(int i = 0; i < 4; i++){
            BALLS_COLOR ball = def_color();

            balls[i] = ball;

        }

        double counterGreen = 0;

        double counterPurple = 0;

        double counterVoid = 0;
        for(int i = 0; i < 4; i++){
            if(balls[i] == BALLS_COLOR.GREEN)
                counterGreen += 1;
            if(balls[i] == BALLS_COLOR.PURPLE)
                counterPurple += 1;
            if(balls[i] == BALLS_COLOR.VOID)
                counterVoid += 1;
        }

        if(counterGreen > counterPurple && counterGreen > counterVoid)
            return BALLS_COLOR.GREEN;
        if(counterPurple > counterGreen && counterPurple > counterVoid)
            return BALLS_COLOR.PURPLE;
        if(counterVoid > counterGreen && counterVoid > counterPurple)
            return BALLS_COLOR.VOID;
        return BALLS_COLOR.VOID;

    }


}
