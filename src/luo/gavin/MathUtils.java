/*
 * MathUtils.java
 * Date: 8/3/2015
 * Time: 10:58 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin;

import java.util.Arrays;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class MathUtils {
    public static int square(Integer num) {
        long value = num.longValue();
        Long rs = value * value;
        return rs.intValue();
    }

    public static int sqrtInt(Integer num) {
        Double rs = Math.sqrt(num.longValue());
        return rs.intValue();
    }

    /**
     * 求等腰三角形底边长度
     * @param isosceles 腰边长
     * @param angle 顶角度数
     * @return 底边长度
     */
    public static int isoscelesTriangleBottomWidth(int isosceles, int angle) {
        Double sqrt = sqrt(2 * (1 - Math.round(cos(toRadians(angle)) * 100)));
        Double width = isosceles * sqrt;
        return width.intValue();
    }

    /**
     * 已知起点坐标，圆弧角度，圆弧半径，求终点坐标
     * @param x 起点坐标x
     * @param y 起点坐标y
     * @param radii 圆弧半径
     * @param angle 圆弧夹角
     * @return 终点坐标[x, y]
     */
    public static int[] arcEndpointCoordinate(int x, int y, int radii, int angle) {
        Double x1 = cos(toRadians(atan(toRadians(y / x)) + angle)) * radii;
        Double y1 = sin(toRadians(atan(toRadians(y / x)) + angle)) * radii;
        return new int[]{x1.intValue(), y1.intValue()};
    }

    /**
     * 计算圆上任意一点的坐标，根据圆心坐标，半径， 和X正轴的圆心角。
     * @param x 圆心坐标x
     * @param y 圆心坐标y
     * @param r 圆半径
     * @param angle 与X正轴的夹角
     * @return 在圆上的端点的坐标
     */
    public static int[] circleCoordinate(int x, int y, int r, int angle) {
        Double x1 = x + r * cos(toRadians(angle));
        Double y1 = y + r * sin(toRadians(angle));
        return new int[]{x1.intValue(), y1.intValue()};
    }

    public static void main(String... args) {
        System.out.println(30 * 3.14 /180);
        System.out.println(toRadians(30));
        System.out.println(Arrays.toString(circleCoordinate(0, 0, 1, 180)));
    }
}
