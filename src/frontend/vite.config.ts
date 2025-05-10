import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import vueJsxPlugin from "@vitejs/plugin-vue-jsx";
import viteLegacyPlugin from "@vitejs/plugin-legacy";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    vueJsxPlugin(),
    // viteLegacyPlugin({
    //   targets: ["cover 99.5% in CN"]
    // })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:80',
        rewrite: url => url.replace(/^\/api/, ''),

      }
    }
  },
})
