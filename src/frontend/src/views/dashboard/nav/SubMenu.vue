<script setup lang="ts">
import type { NavMenuItem } from "@/config/nav.tsx";
import { useRouter } from "vue-router";

interface Props extends NavMenuItem {
  index: string;
}

const props = defineProps<Props>();

const router = useRouter();

// 根据权限判断是否显示
const canPass = (item: NavMenuItem) => {
  let route = router.resolve({
    name: item.routeName
  });
  let passCount = 0;
  let roles = route.meta.roles;
  if(roles && roles.length > 0) {

  } else {
    passCount++;
  }

  let permissions = route.meta.permissions;
  if(permissions && permissions.length > 0) {

  } else {
    passCount++;
  }
  return passCount === 2;
};

</script>
<template>
  <el-sub-menu :index="props.index" v-if="canPass(props)">
    <template #title>
      <el-icon v-if="props.icon">
        <component :is="props.icon" />
      </el-icon>
      <span>{{ props.title }}</span>
    </template>

    <template v-for="(child, index) in props.children">
      <template v-if="child.children">
        <SubMenu v-bind="child" :index="props.index + '.' + index" />
      </template>
      <template v-else>
        <el-menu-item v-if="canPass(child)" :index="props.index + '.' + index">
          <RouterLink :to="{ name: child.routeName }" >
            <el-icon v-if="child.icon">
              <component :is="child.icon" />
            </el-icon>
            <span>{{ child.title }}</span>
          </RouterLink>
        </el-menu-item>
      </template>
    </template>
  </el-sub-menu>
</template>
