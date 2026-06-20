package com.drone.service;

import com.drone.model.WeatherAssessment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    // Wind speed thresholds (m/s) based on common multirotor wind resistance
    private static final double WIND_CAUTION = 5.0;
    private static final double WIND_LIMIT = 10.0;

    // Visibility thresholds (km) based on visual-line-of-sight requirements
    private static final double VIS_CAUTION = 5.0;
    private static final double VIS_LIMIT = 3.0;

    public WeatherAssessment assess(double windSpeed, double visibility) {
        windSpeed = Math.max(0, windSpeed);
        visibility = Math.max(0, visibility);

        String windLevel = classifyWind(windSpeed);
        String visibilityLevel = classifyVisibility(visibility);
        String overall = combine(windLevel, visibilityLevel);

        List<String> reasons = new ArrayList<>();
        reasons.add(windReason(windSpeed, windLevel));
        reasons.add(visibilityReason(visibility, visibilityLevel));

        WeatherAssessment result = new WeatherAssessment();
        result.setWindSpeed(windSpeed);
        result.setVisibility(visibility);
        result.setWindLevel(windLevel);
        result.setVisibilityLevel(visibilityLevel);
        result.setOverall(overall);
        result.setCanTakeoff("suitable".equals(overall));
        result.setRecommendation(recommendationFor(overall));
        result.setReasons(reasons);
        return result;
    }

    private String classifyWind(double windSpeed) {
        if (windSpeed < WIND_CAUTION) return "suitable";
        if (windSpeed < WIND_LIMIT) return "caution";
        return "unsuitable";
    }

    private String classifyVisibility(double visibility) {
        if (visibility > VIS_CAUTION) return "suitable";
        if (visibility >= VIS_LIMIT) return "caution";
        return "unsuitable";
    }

    private String combine(String a, String b) {
        if ("unsuitable".equals(a) || "unsuitable".equals(b)) return "unsuitable";
        if ("caution".equals(a) || "caution".equals(b)) return "caution";
        return "suitable";
    }

    private String windReason(double windSpeed, String level) {
        String base = String.format("风速 %.1f m/s", windSpeed);
        switch (level) {
            case "suitable": return base + "，低于 " + WIND_CAUTION + " m/s，操控稳定。";
            case "caution": return base + "，处于 " + WIND_CAUTION + "-" + WIND_LIMIT + " m/s 区间，抗风压力增大。";
            default: return base + "，达到或超过 " + WIND_LIMIT + " m/s，超出多数无人机抗风极限。";
        }
    }

    private String visibilityReason(double visibility, String level) {
        String base = String.format("能见度 %.1f km", visibility);
        switch (level) {
            case "suitable": return base + "，优于 " + VIS_CAUTION + " km，视距良好。";
            case "caution": return base + "，处于 " + VIS_LIMIT + "-" + VIS_CAUTION + " km，视距受限。";
            default: return base + "，低于 " + VIS_LIMIT + " km，不满足视距飞行要求。";
        }
    }

    private String recommendationFor(String overall) {
        switch (overall) {
            case "suitable": return "天气窗口良好，风速与能见度均满足起飞条件，当前航线适合起飞。";
            case "caution": return "天气窗口边缘，存在不利因素，建议谨慎评估后起飞，可考虑缩短航线或降低飞行高度。";
            default: return "天气窗口不达标，风速或能见度超出安全范围，当前航线不适合起飞，建议推迟起飞。";
        }
    }
}
