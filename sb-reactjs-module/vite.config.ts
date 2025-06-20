import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    open: '/',
    proxy: {
      '/api': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        secure: false,
        headers : {
          'X-Src-ID': 'SANDBOX-WB'
        }
      }
    }
  } 
})
