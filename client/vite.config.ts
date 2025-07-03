import { fileURLToPath, URL } from 'node:url'
import fs from 'node:fs'

import { defineConfig, Plugin } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'
import path from 'node:path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx(), vueDevTools(), fixSourceMaps()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  }
})

function fixSourceMaps(): Plugin {
  let currentInterval: any = null
  return {
    name: 'fix-source-map',
    enforce: 'post',
    transform: function (source: any) {
      if (currentInterval) {
        return
      }
      currentInterval = setInterval(function () {
        const nodeModulesPath = path.join(__dirname, 'node_modules', '.vite', 'deps')
        if (fs.existsSync(nodeModulesPath)) {
          clearInterval(currentInterval)
          currentInterval = null
          const files = fs.readdirSync(nodeModulesPath)
          files.forEach(function (file) {
            const mapFile = file + '.map'
            const mapPath = path.join(nodeModulesPath, mapFile)
            if (fs.existsSync(mapPath)) {
              const mapData = JSON.parse(fs.readFileSync(mapPath, 'utf8'))
              if (!mapData.sources || mapData.sources.length == 0) {
                mapData.sources = [path.relative(mapPath, path.join(nodeModulesPath, file))]
                fs.writeFileSync(mapPath, JSON.stringify(mapData), 'utf8')
              }
            }
          })
        }
      }, 100)
      return source
    }
  }
}
