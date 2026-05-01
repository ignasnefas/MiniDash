'use client';

import { useEffect, useState } from 'react';
import TerminalBox from '@/components/ui/TerminalBox';
import styles from './WorldClocksWidget.module.css';

const DEFAULT_ZONES: string[] = [
  'UTC',
  'America/New_York',
  'Europe/London',
  'Asia/Tokyo',
];

const PRESET_ZONES = [
  { label: 'Lithuania', value: 'Europe/Vilnius' },
  { label: 'United States (East)', value: 'America/New_York' },
  { label: 'United States (West)', value: 'America/Los_Angeles' },
  { label: 'Japan', value: 'Asia/Tokyo' },
  { label: 'United Kingdom', value: 'Europe/London' },
  { label: 'Australia', value: 'Australia/Sydney' },
  { label: 'France', value: 'Europe/Paris' },
  { label: 'UAE', value: 'Asia/Dubai' },
  { label: 'Brazil', value: 'America/Sao_Paulo' },
  { label: 'India', value: 'Asia/Kolkata' },
];

function getSupportedTimeZones() {
  if (typeof Intl?.supportedValuesOf === 'function') {
    return Intl.supportedValuesOf('timeZone');
  }

  return Array.from(
    new Set([
      ...DEFAULT_ZONES,
      ...PRESET_ZONES.map((option) => option.value),
    ])
  );
}

function normalizeTimeZone(timeZone: string) {
  const normalized = timeZone.trim().replace(/\s+/g, '_');
  if (!normalized) return null;

  const supportedZones = getSupportedTimeZones();
  const lowerInput = normalized.toLowerCase();

  const exactMatch = supportedZones.find((zone) => zone === normalized);
  if (exactMatch) return exactMatch;

  const caseInsensitiveMatch = supportedZones.find((zone) => zone.toLowerCase() === lowerInput);
  if (caseInsensitiveMatch) return caseInsensitiveMatch;

  const suffixMatch = supportedZones.find((zone) => zone.toLowerCase().endsWith(`/${lowerInput}`));
  if (suffixMatch) return suffixMatch;

  const presetMatch = PRESET_ZONES.find(
    (option) =>
      option.value.toLowerCase() === lowerInput ||
      option.label.toLowerCase() === lowerInput
  );
  if (presetMatch) return presetMatch.value;

  const inputTokens = lowerInput.replace(/[_/]+/g, ' ').split(' ').filter(Boolean);
  if (inputTokens.length > 0) {
    const fuzzyMatch = supportedZones.find((zone) => {
      const zoneTokens = zone.toLowerCase().replace(/[_/]+/g, ' ').split(' ').filter(Boolean);
      return inputTokens.every((token) => zoneTokens.includes(token));
    });
    if (fuzzyMatch) return fuzzyMatch;
  }

  return null;
}

function isValidTimeZone(timeZone: string) {
  try {
    new Intl.DateTimeFormat('en-US', { timeZone }).format();
    return true;
  } catch {
    return false;
  }
}

function getZoneHour(date: Date, timeZone: string) {
  try {
    const hourString = date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      hour12: false,
      timeZone,
    });
    return Number(hourString.split(':')[0]);
  } catch {
    return new Date(date).getHours();
  }
}

function getDayNightIndicator(date: Date, timeZone: string) {
  const hour = getZoneHour(date, timeZone);
  return hour >= 6 && hour < 18 ? 'day' : 'night';
}

function formatTime(date: Date, timeZone: string, is24Hour: boolean) {
  const options = {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: !is24Hour,
  } as const;

  if (!isValidTimeZone(timeZone)) {
    return date.toLocaleTimeString('en-US', options);
  }

  try {
    return date.toLocaleTimeString('en-US', { ...options, timeZone });
  } catch {
    return date.toLocaleTimeString('en-US', options);
  }
}

export default function WorldClocksWidget() {
  const [zones, setZones] = useState<string[]>(DEFAULT_ZONES);
  const [now, setNow] = useState<Date | null>(null);
  const [newZone, setNewZone] = useState('');
  const [selectedPreset, setSelectedPreset] = useState(PRESET_ZONES[0].value);
  const [is24Hour, setIs24Hour] = useState(true);

  const availablePresets = PRESET_ZONES.filter((option) => !zones.includes(option.value));

  useEffect(() => {
    if (availablePresets.length === 0) {
      setSelectedPreset('');
      return;
    }

    if (!availablePresets.some((option) => option.value === selectedPreset)) {
      setSelectedPreset(availablePresets[0].value);
    }
  }, [availablePresets, selectedPreset]);

  useEffect(() => {
    setNow(new Date());
    const t = setInterval(() => setNow(new Date()), 1000);
    return () => clearInterval(t);
  }, []);

  const addZone = () => {
    const normalized = normalizeTimeZone(newZone);
    if (!normalized) {
      alert('Invalid timezone. Please enter a valid IANA timezone or city name (e.g. Europe/London, Vilnius).');
      return;
    }

    if (!zones.includes(normalized)) setZones(prev => [...prev, normalized]);
    setNewZone('');
  };

  const addPresetZone = () => {
    if (!selectedPreset) {
      return;
    }

    const normalized = normalizeTimeZone(selectedPreset);
    if (!normalized) {
      alert('Invalid preset timezone selected.');
      return;
    }

    if (!zones.includes(normalized)) setZones(prev => [...prev, normalized]);
  };

  const removeZone = (zone: string) => {
    setZones(prev => prev.filter(z => z !== zone));
  };

  const formatZoneLabel = (zone: string) =>
    zone.replace(/_/g, ' ').replace(/\//g, ' / ');

  const activeNow = now;
  const statusText = now ? `Updated: ${now.toLocaleTimeString()}` : 'Updated: --:--:--';

  return (
    <TerminalBox title="clocks --world" icon="🕒" status={statusText}>
      <div className={styles.container}>
        <div className={styles.controls}>
          <select className={styles.select} value={selectedPreset} onChange={(e) => setSelectedPreset(e.target.value)} disabled={availablePresets.length === 0}>
            {availablePresets.length > 0 ? (
              availablePresets.map((option) => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))
            ) : (
              <option value="">All presets added</option>
            )}
          </select>
          <button className={styles.button} onClick={addPresetZone} disabled={availablePresets.length === 0}>Add preset</button>
          <input className={styles.input} value={newZone} onChange={(e) => setNewZone(e.target.value)} placeholder="Enter a location" />
          <button className={styles.button} onClick={addZone}>Add</button>
          <button className={styles.button} onClick={() => setIs24Hour((prev) => !prev)}>
            {is24Hour ? '24h' : 'AM/PM'}
          </button>
        </div>

        <div className={styles.list}>
          {zones.length === 0 ? (
            <div className={styles.empty}>Add timezones to display clocks</div>
          ) : (
            zones.map((z) => {
              const indicatorMode = activeNow ? getDayNightIndicator(activeNow, z) : 'day';
              return (
                <div key={z} className={styles.row}>
                  <div className={`${styles.indicator} ${styles[indicatorMode]}`} title={indicatorMode === 'day' ? 'Day' : 'Night'} />
                  <div className={styles.time}>{activeNow ? formatTime(activeNow, z, is24Hour) : '--:--:--'}</div>
                  <div className={styles.zone}>{formatZoneLabel(z)}</div>
                  <button className={styles.remove} onClick={() => removeZone(z)}>✕</button>
                </div>
              );
            })
          )}
        </div>
      </div>
    </TerminalBox>
  );
}
