import { fileURLToPath, URL } from "node:url";

import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";
import vueJsxPlugin from "@vitejs/plugin-vue-jsx";
import viteLegacyPlugin from "@vitejs/plugin-legacy";



// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  console.log("MODE", mode);
  const env = loadEnv(mode, process.cwd());
  return {
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
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
    server: {
      proxy: {
        [env.VITE_API_BASEURL]: {
          target: env.VITE_API_PROXY_URL,
          rewrite: (url) => url.replace(new RegExp("^" + env.VITE_API_BASEURL), ""),
        },
      },
    },
  };
});
