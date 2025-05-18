<script lang="ts" setup>
import { getRoleName } from "@/configs/auth";
import { getBatch } from "@/request/api/user";
import { successNotification } from "@/utils/messageNotification";
import { onMounted, reactive, ref } from "vue";
import UserInfoEditWidget from "./UserInfoEditWidget.vue";

interface User {
  /** 用户名 */
  username: string;
  /** 昵称 */
  nickname: string;
  /** 电子邮件 */
  email: string;
  /** 头像访问路径 */
  avatar: string;
  /** 上一次登录时间 */
  lastLoginTime?: string;
  /** 用户创建时间 */
  createdTime: string;
  /** 用户数据更新时间 */
  updatedTime?: string;
  /** 角色 */
  roles: string[];
  /** 权限 */
  permissions: string[];
}

const tableData = ref<User[]>([]);
// 表格数据正在请求和加载
const loading = ref(false);

// 请求数据，根据model
const requestData = async () => {
  loading.value = true;
  const { pageSize, current } = page.value;
  const queryCondition = searchModel.value.username
    ? [["username", "like", searchModel.value.username]]
    : undefined;
  try {
    const { data } = await getBatch({
      pageSize,
      current,
      orders: [["createdTime", "asc"]],
      queryCondition,
    });
    tableData.value = data!.records;
    page.value.total = data!.total;
    page.value.current = data!.current;
  } finally {
    loading.value = false;
  }
};

const resetState = () => {
  page.value = {
    current: 1,
    pageSize: 10,
    total: 0,
  };
  searchModel.value = {
    username: "",
  };
};

//#region 分页数据
const page = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});

// 监听分页变化
const onCurrentChange = (current: number) => {
  page.value.current = current;
  requestData();
};
//#endregion

//#region 搜索框
const searchModel = ref({
  // 模糊搜索用户名
  username: "",
});
//#endregion

// 打开删除数据弹窗
const onClickOpenDelete = async (scope: any) => {
  const username = scope.row.username;
  // if(username !== undefined){
  //   await deleteUser({
  //     username
  //   });
  // }
  deleteModel.username = username;
  deleteModel.modalVisible = true;
};

// 确定删除
const onClickConfirmDelete = async () => {
  const username = deleteModel.username;
  if (username !== undefined) {
    // await deleteUser({
    //   username,
    // });
    deleteModel.modalVisible = false;
    successNotification("删除成功");
    resetState();
    await requestData();
  }
};

const deleteModel = reactive({
  username: "",
  modalVisible: false,
});

const editModel = reactive({
  user: {} as API.UserSafeVO,
  modalVisible: false,
});

const onClickOpenEdit = (scope: any) => {
  editModel.modalVisible = true;
  editModel.user = scope.row;
};

// 请求数据
onMounted(requestData);
</script>
<template>
  <div>
    <div>
      <el-input v-model="searchModel.username" style="max-width: 600px" placeholder="姓名模糊搜索">
        <template #prepend>用户名</template>
        <template #append>
          <el-button @click="requestData">搜索</el-button>
        </template>
      </el-input>
    </div>
    <el-table v-loading="loading" :data="tableData" row-key="id" style="width: 100%">
      <!-- <el-table-column type="selection" :selectable="selectable" width="55" /> -->
      <!-- <el-table-column label="Date" width="120">
      <template #default="scope">{{ scope.row.date }}</template>
    </el-table-column> -->
      <el-table-column property="username" label="用户名" />
      <el-table-column property="nickname" label="昵称" />
      <el-table-column property="email" label="电子邮件" />
      <el-table-column property="roles" label="角色" width="120">
        <template #default="scope">
          <div v-for="role in scope.row.roles" :key="role">{{ getRoleName(role) }}</div>
        </template>
      </el-table-column>
      <el-table-column property="permissions" label="权限" width="120">
        <template #default="scope">
          <div v-for="permission in scope.row.permissions" :key="permission">{{ permission }}</div>
        </template>
      </el-table-column>
      <el-table-column property="lastLoginTime" label="上次登录时间" width="120" />
      <el-table-column property="createdTime" label="创建时间" width="120" />
      <el-table-column property="updatedTime" label="最后修改时间" width="120" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="onClickOpenEdit(scope)">查看和编辑</el-button>
          <el-button type="danger" size="small" @click="onClickOpenDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="page.current"
      :page-size="page.pageSize"
      size="default"
      layout="total, prev, pager, next"
      :total="page.total"
      @current-change="onCurrentChange"
    />

    <!-- 删除提示框 -->
    <el-dialog v-model="deleteModel.modalVisible" title="删除警告" width="500">
      <span>确定删除,操作不可更改</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="deleteModel.modalVisible = false">取消</el-button>
          <el-button type="danger" @click="onClickConfirmDelete"> 确定 </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户信息编辑 -->
    <UserInfoEditWidget v-model="editModel.modalVisible" :user="editModel.user" />
  </div>
</template>
