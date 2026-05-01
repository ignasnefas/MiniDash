import { apiFetch } from '@/utils/api';
import type { ApiResponse, WeatherData, NewsItem, HackerNewsItem, TrendingTopic, QuoteOfTheDay } from '@/types/api';

const WEATHER_ASCII: Record<string, string> = {
  Clear: `
    \\   /
     .-.
  ― (   ) ―
     \'-'
    /   \\
`,
  Sunny: `
    \\   /
     .-.
  ― (   ) ―
     \'-'
    /   \\
`,
  'Partly cloudy': `
   \\  /
 _ /"".-.
   \\_(   ).
   /(___(__) 
`,
  Cloudy: `
     .--.
  .-(    ).
 (___.__)__)
`,
  Overcast: `
     .--.
  .-(    ).
 (___.__)__)
`,
  Rain: `
     .--.
  .-(    ).
 (___.__)__)
  ' ' ' '
 ' ' ' '
`,
  'Light rain': `
     .--.
  .-(    ).
 (___.__)__)
    ' ' '
`,
  Snow: `
     .--.
  .-(    ).
 (___.__)__)
   * * * *
  * * * *
`,
  'Light snow': `
     .--.
  .-(    ).
 (___.__)__)
    * * *
`,
  Thunderstorm: `
     .--.
  .-(    ).
 (___.__)__)
    ⚡⚡
   ' ' ' '
`,
  Mist: `
  _ - _ - _
   _ - _ -
  _ - _ - _
`,
  Fog: `
  _ - _ - _
   _ - _ -
  _ - _ - _
`,
};

function getWeatherAscii(condition: string): string {
  for (const [key, art] of Object.entries(WEATHER_ASCII)) {
    if (condition.toLowerCase().includes(key.toLowerCase())) {
      return art;
    }
  }
  return WEATHER_ASCII['Cloudy'];
}

// Utility functions for CLI commands
export const commandUtils = {
  async fetchWeather(location: string) {
    const res: ApiResponse<WeatherData> = await apiFetch(`/api/weather?location=${encodeURIComponent(location)}`);
    if (res.data) {
      const weather = res.data;
      const forecastLines = weather.forecast.slice(0, 3).map((day) => {
        const date = new Date(day.date).toLocaleDateString('en-US', { weekday: 'short' });
        return `${date}: ${day.high}°/${day.low}° ${day.condition}`;
      }).join('\n');
      return `${getWeatherAscii(weather.current.condition)}\nLocation: ${weather.location}\n${weather.current.temp}°C (feels like ${weather.current.feels_like}°C)\n${weather.current.condition}\nHumidity: ${weather.current.humidity}%\nWind: ${weather.current.wind_speed} km/h ${weather.current.wind_direction}\nVisibility: ${weather.current.visibility} km\n\n3-day forecast:\n${forecastLines}\n\nLast updated: ${new Date(weather.lastUpdated).toLocaleString()}`;
    }
    throw new Error('No weather data available');
  },

  async fetchNews(category: string, limit = 5) {
    const res: ApiResponse<NewsItem[]> = await apiFetch(`/api/news?category=${encodeURIComponent(category)}&limit=${limit}`);
    const items = res.data || [];
    if (items.length) {
      const out = items.map((it, idx: number) => `${idx + 1}. ${it.title}`).join('\n');
      return `Top ${items.length} ${category} headlines:\n${out}`;
    }
    return `No ${category} news available`;
  },

  async fetchHackerNews(limit = 5) {
    const res: ApiResponse<HackerNewsItem[]> = await apiFetch(`/api/hackernews?limit=${limit}`);
    const items = res.data || [];
    if (items.length) {
      const out = items.slice(0, limit).map((it: any, idx: number) => `${idx + 1}. ${it.title}`).join('\n');
      return `Top Hacker News:\n${out}`;
    }
    return 'No hackernews data';
  },

  async fetchTrending() {
    const res: ApiResponse<{ github: any[], twitter: TrendingTopic[] }> = await apiFetch('/api/trending');
    const data = res.data;
    if (data && data.github && data.github.length) {
      const out = data.github.slice(0, 5).map((g: any, idx: number) => `${idx + 1}. ${g.name} (${g.stars} ★)`).join('\n');
      return `Trending repos:\n${out}`;
    }
    return 'No trending data';
  },

  async fetchQuote() {
    const res: ApiResponse<QuoteOfTheDay> = await apiFetch('/api/quote');
    const q = res.data;
    if (q) {
      return `${q.text}\n— ${q.author || 'Unknown'}`;
    }
    return 'No quote available';
  },

  async fetchReddit(subreddit: string, limit = 5) {
    const res: ApiResponse<any[]> = await apiFetch(`/api/reddit?subreddit=${encodeURIComponent(subreddit)}&limit=${limit}`);
    const items = res.data || [];
    if (items.length) {
      const out = items.map((it: any, idx: number) => `${idx + 1}. ${it.title}`).join('\n');
      return `Top posts from r/${subreddit}:\n${out}`;
    }
    return `No posts from r/${subreddit}`;
  },
};