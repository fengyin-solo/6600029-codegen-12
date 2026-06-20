package com.drone.model;

import java.util.List;

public class WeatherAssessment {
    private double windSpeed;          // m/s
    private double visibility;         // km
    private String windLevel;          // suitable | caution | unsuitable
    private String visibilityLevel;    // suitable | caution | unsuitable
    private String overall;            // suitable | caution | unsuitable
    private boolean canTakeoff;
    private String recommendation;
    private List<String> reasons;

    public WeatherAssessment() {}

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public double getVisibility() { return visibility; }
    public void setVisibility(double visibility) { this.visibility = visibility; }

    public String getWindLevel() { return windLevel; }
    public void setWindLevel(String windLevel) { this.windLevel = windLevel; }

    public String getVisibilityLevel() { return visibilityLevel; }
    public void setVisibilityLevel(String visibilityLevel) { this.visibilityLevel = visibilityLevel; }

    public String getOverall() { return overall; }
    public void setOverall(String overall) { this.overall = overall; }

    public boolean isCanTakeoff() { return canTakeoff; }
    public void setCanTakeoff(boolean canTakeoff) { this.canTakeoff = canTakeoff; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}
