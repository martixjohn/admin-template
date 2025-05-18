<script setup lang="ts">
import { Util } from "@/utils/util";
import { Avatar, Iphone, Promotion, User } from "@element-plus/icons-vue";
import { computed, ref } from "vue";

const dialogVisible = defineModel<boolean>();

const props = defineProps<{
  user: API.UserSafeVO;
}>();

const createdTime = computed(() => Util.formatDate(props.user.createdTime));

const updatedTime = computed(() =>
  props.user.updatedTime !== undefined ? Util.formatDate(props.user.updatedTime) : "暂无",
);

const lastLoginTime = computed(() =>
  props.user.lastLoginTime !== undefined ? Util.formatDate(props.user.lastLoginTime) : "暂无",
);

const iconStyle = {
  marginRight: "5px",
};

// 编辑模式
const editMode = ref(false);
</script>
<template>
  <el-dialog v-model="dialogVisible" title="用户详情" width="500" draggable>
    <el-descriptions class="margin-top" :title="user.username + '的详情'" :column="3" border>
      <template #extra>
        <el-button type="primary">编辑</el-button>
      </template>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <User />
            </el-icon>
            用户名
          </div>
        </template>
        {{ user.username }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Avatar />
            </el-icon>
            头像
          </div>
        </template>
        {{ user.avatar }}
      </el-descriptions-item>
      <!-- <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Promotion />
            </el-icon>
            邮件
          </div>
        </template>
        {{ user.email }}
      </el-descriptions-item> -->
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">上次登录时间</div>
        </template>
        {{ lastLoginTime }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">用户创建时间</div>
        </template>
        {{ createdTime }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">数据更新时间</div>
        </template>
        {{ updatedTime }}
      </el-descriptions-item>
    </el-descriptions>
    <template #footer v-if="editMode">
      <div class="dialog-footer">
        <el-button @click="editMode = false">取消修改</el-button>
        <el-button type="primary" @click="dialogVisible = false"> 确认修改 </el-button>
      </div>
    </template>
  </el-dialog>
</template>
