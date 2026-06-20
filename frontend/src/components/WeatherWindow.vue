<script setup lang="ts">
import { useDroneStore } from '../store/drone';
import { windDirectionLabel } from '../utils/weather';
import type { WeatherLevel } from '../types';

const store = useDroneStore();

function levelMeta(level: WeatherLevel) {
  switch (level) {
    case 'suitable':
      return { label: '适合', color: '#22c55e', chip: 'bg-green-900/50 text-green-300 border-green-700', icon: '✓' };
    case 'caution':
      return { label: '谨慎', color: '#eab308', chip: 'bg-amber-900/50 text-amber-300 border-amber-700', icon: '⚠' };
    default:
      return { label: '不适合', color: '#ef4444', chip: 'bg-red-900/50 text-red-300 border-red-700', icon: '✕' };
  }
}

function onWind(e: Event) {
  store.setWeather({ windSpeed: parseFloat((e.target as HTMLInputElement).value) });
}
function onVis(e: Event) {
  store.setWeather({ visibility: parseFloat((e.target as HTMLInputElement).value) });
}
</script>

<template>
  <div class="bg-slate-800 rounded-lg p-4 space-y-3">
    <div class="flex items-center justify-between border-b border-slate-700 pb-2">
      <h3 class="text-sm font-bold text-slate-200">🌤 天气窗口评估</h3>
      <button
        @click="store.refreshWeather()"
        class="px-2 py-1 rounded text-[10px] font-medium bg-sky-700 text-white hover:bg-sky-600 transition"
      >
        ↻ 刷新天气
      </button>
    </div>

    <!-- Current weather summary -->
    <div class="grid grid-cols-2 gap-2 text-xs">
      <div class="bg-slate-900 rounded p-2">
        <div class="text-slate-400">天气 / 温度</div>
        <div class="text-sm font-bold text-slate-100">
          {{ store.weatherData.condition }}
          <span class="text-slate-400 font-normal">{{ store.weatherData.temperature.toFixed(1) }}°C</span>
        </div>
      </div>
      <div class="bg-slate-900 rounded p-2">
        <div class="text-slate-400">风向</div>
        <div class="text-sm font-bold text-slate-100">
          {{ windDirectionLabel(store.weatherData.windDirection) }}
          <span class="text-slate-400 font-normal">{{ store.weatherData.windDirection }}°</span>
        </div>
      </div>
    </div>

    <!-- Wind speed slider -->
    <div class="space-y-1">
      <div class="flex items-center justify-between text-xs">
        <span class="text-slate-400">风速</span>
        <span class="font-bold text-slate-100">
          {{ store.weatherData.windSpeed.toFixed(1) }} m/s
          <span
            v-if="store.weatherAssessment"
            class="ml-1 px-1.5 py-0.5 rounded border text-[10px]"
            :class="levelMeta(store.weatherAssessment.windLevel).chip"
          >
            {{ levelMeta(store.weatherAssessment.windLevel).label }}
          </span>
        </span>
      </div>
      <input
        type="range"
        min="0"
        max="20"
        step="0.1"
        :value="store.weatherData.windSpeed"
        @input="onWind"
        class="w-full accent-sky-500"
      />
    </div>

    <!-- Visibility slider -->
    <div class="space-y-1">
      <div class="flex items-center justify-between text-xs">
        <span class="text-slate-400">能见度</span>
        <span class="font-bold text-slate-100">
          {{ store.weatherData.visibility.toFixed(1) }} km
          <span
            v-if="store.weatherAssessment"
            class="ml-1 px-1.5 py-0.5 rounded border text-[10px]"
            :class="levelMeta(store.weatherAssessment.visibilityLevel).chip"
          >
            {{ levelMeta(store.weatherAssessment.visibilityLevel).label }}
          </span>
        </span>
      </div>
      <input
        type="range"
        min="0"
        max="15"
        step="0.1"
        :value="store.weatherData.visibility"
        @input="onVis"
        class="w-full accent-sky-500"
      />
    </div>

    <!-- Overall assessment -->
    <div
      v-if="store.weatherAssessment"
      class="rounded-lg p-3 border"
      :class="levelMeta(store.weatherAssessment.overall).chip"
    >
      <div class="flex items-center gap-2">
        <span class="text-lg">{{ levelMeta(store.weatherAssessment.overall).icon }}</span>
        <div>
          <div class="text-xs text-slate-400">起飞建议</div>
          <div class="text-base font-bold" :style="{ color: levelMeta(store.weatherAssessment.overall).color }">
            {{ store.weatherAssessment.canTakeoff ? '当前航线适合起飞' : '当前航线不建议起飞' }}
          </div>
        </div>
      </div>
      <p class="text-[11px] text-slate-300 mt-2 leading-relaxed">
        {{ store.weatherAssessment.recommendation }}
      </p>
      <ul class="mt-2 space-y-1">
        <li
          v-for="(reason, i) in store.weatherAssessment.reasons"
          :key="i"
          class="text-[11px] text-slate-400 flex gap-1"
        >
          <span class="text-slate-500">•</span>
          <span>{{ reason }}</span>
        </li>
      </ul>
    </div>

    <!-- Current route context -->
    <div class="border-t border-slate-700 pt-2 text-[10px] text-slate-500 flex justify-between">
      <span>当前航线: {{ store.waypoints.length }} 航点</span>
      <span>{{ (store.totalDistance / 1000).toFixed(2) }} km</span>
    </div>
  </div>
</template>
