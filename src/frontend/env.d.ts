/// <reference types="vite/client" />

declare module "element-plus/dist/locale/zh-cn.mjs";


interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

