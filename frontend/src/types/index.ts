export interface Waypoint {
  id: string;
  lat: number;
  lng: number;
  altitude: number;   // meters AGL
  speed: number;      // m/s
  action: 'hover' | 'photo' | 'video' | 'none';
}

export interface FlightPlan {
  id: string;
  name: string;
  waypoints: Waypoint[];
  totalDistance: number;
  estimatedTime: number;
  batteryUsage: number;  // percentage
}

export interface NoFlyZone {
  id: string;
  name: string;
  center: [number, number];
  radius: number;  // meters
  type: 'airport' | 'military' | 'restricted';
}

export interface TerrainPoint {
  lat: number;
  lng: number;
  elevation: number;
}

export interface DroneConfig {
  maxAltitude: number;
  maxSpeed: number;
  batteryCapacity: number;  // mAh
  consumptionRate: number;  // mAh/min
  safeDistance: number;     // meters from obstacles
}

export type WeatherLevel = 'suitable' | 'caution' | 'unsuitable';

export interface WeatherAssessment {
  windSpeed: number;            // m/s
  visibility: number;           // km
  windLevel: WeatherLevel;
  visibilityLevel: WeatherLevel;
  overall: WeatherLevel;
  canTakeoff: boolean;
  recommendation: string;
  reasons: string[];
}

export interface WeatherData {
  windSpeed: number;            // m/s
  windDirection: number;        // degrees (0-359)
  visibility: number;           // km
  temperature: number;          // celsius
  condition: string;            // e.g. 晴 / 多云 / 阴 / 小雨
}
