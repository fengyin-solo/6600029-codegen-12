import type { WeatherAssessment, WeatherData, WeatherLevel } from '../types';

// Wind speed thresholds (m/s) based on common multirotor wind resistance
export const WIND_CAUTION = 5;
export const WIND_LIMIT = 10;

// Visibility thresholds (km) based on visual-line-of-sight requirements
export const VIS_CAUTION = 5;
export const VIS_LIMIT = 3;

export function classifyWind(windSpeed: number): WeatherLevel {
  if (windSpeed < WIND_CAUTION) return 'suitable';
  if (windSpeed < WIND_LIMIT) return 'caution';
  return 'unsuitable';
}

export function classifyVisibility(visibility: number): WeatherLevel {
  if (visibility > VIS_CAUTION) return 'suitable';
  if (visibility >= VIS_LIMIT) return 'caution';
  return 'unsuitable';
}

function combine(a: WeatherLevel, b: WeatherLevel): WeatherLevel {
  if (a === 'unsuitable' || b === 'unsuitable') return 'unsuitable';
  if (a === 'caution' || b === 'caution') return 'caution';
  return 'suitable';
}

function windReason(windSpeed: number, level: WeatherLevel): string {
  const base = `风速 ${windSpeed.toFixed(1)} m/s`;
  switch (level) {
    case 'suitable': return `${base}，低于 ${WIND_CAUTION} m/s，操控稳定。`;
    case 'caution': return `${base}，处于 ${WIND_CAUTION}-${WIND_LIMIT} m/s 区间，抗风压力增大。`;
    default: return `${base}，达到或超过 ${WIND_LIMIT} m/s，超出多数无人机抗风极限。`;
  }
}

function visibilityReason(visibility: number, level: WeatherLevel): string {
  const base = `能见度 ${visibility.toFixed(1)} km`;
  switch (level) {
    case 'suitable': return `${base}，优于 ${VIS_CAUTION} km，视距良好。`;
    case 'caution': return `${base}，处于 ${VIS_LIMIT}-${VIS_CAUTION} km，视距受限。`;
    default: return `${base}，低于 ${VIS_LIMIT} km，不满足视距飞行要求。`;
  }
}

function recommendationFor(overall: WeatherLevel): string {
  switch (overall) {
    case 'suitable': return '天气窗口良好，风速与能见度均满足起飞条件，当前航线适合起飞。';
    case 'caution': return '天气窗口边缘，存在不利因素，建议谨慎评估后起飞，可考虑缩短航线或降低飞行高度。';
    default: return '天气窗口不达标，风速或能见度超出安全范围，当前航线不适合起飞，建议推迟起飞。';
  }
}

export function assessWeatherWindow(windSpeed: number, visibility: number): WeatherAssessment {
  const ws = Math.max(0, windSpeed);
  const vis = Math.max(0, visibility);
  const windLevel = classifyWind(ws);
  const visibilityLevel = classifyVisibility(vis);
  const overall = combine(windLevel, visibilityLevel);
  return {
    windSpeed: ws,
    visibility: vis,
    windLevel,
    visibilityLevel,
    overall,
    canTakeoff: overall === 'suitable',
    recommendation: recommendationFor(overall),
    reasons: [windReason(ws, windLevel), visibilityReason(vis, visibilityLevel)],
  };
}

const WEATHER_CONDITIONS = ['晴', '多云', '阴', '小雨'];

function round1(n: number): number {
  return Math.round(n * 10) / 10;
}

export function generateMockWeather(): WeatherData {
  return {
    windSpeed: round1(Math.random() * 14),
    windDirection: Math.floor(Math.random() * 360),
    visibility: round1(Math.random() * 12),
    temperature: round1(Math.random() * 30 + 5),
    condition: WEATHER_CONDITIONS[Math.floor(Math.random() * WEATHER_CONDITIONS.length)],
  };
}

export function windDirectionLabel(deg: number): string {
  const dirs = ['北', '东北', '东', '东南', '南', '西南', '西', '西北'];
  const idx = Math.round(deg / 45) % 8;
  return dirs[idx];
}
