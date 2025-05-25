import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    open: '/index.html',
    proxy: {
      '/groups': {
        target: 'http://localhost:8100',
        changeOrigin: true,
        secure: false,
        headers: {
          'X-Tenant-Id': 'DC-R1'
        }
      },
      '/group': {
        target: 'http://localhost:8100',
        changeOrigin: true,
        secure: false,
        headers: {
          'X-Tenant-Id': 'DC-R1'
        }
      },
      '/api': {
        target: 'http://localhost:8100',
        changeOrigin: true,
        secure: false,
        headers: {
          'X-Tenant-Id': 'DC-R1'
        }
      }
    }
  } 
})
