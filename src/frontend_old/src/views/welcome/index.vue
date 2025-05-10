<script setup lang="ts">
defineOptions({
  name: "Welcome"
});
import { useECharts } from '@pureadmin/utils';
import { ref } from 'vue';
const echart = ref()
const { setOptions } = useECharts(echart)
const windBarbData = generateWindBarbPath(65, 45); // 65节，东北风
function generateWindBarbPath(speed, direction) {
  // 常量定义
  const BARB_HEIGHT = 60; // 杆高
  const LONG_BARB_LENGTH = 15; // 长羽长度
  const SHORT_BARB_LENGTH = 10; // 短羽长度
  const BARB_GAP = 10; // 风羽间距
  const TRIANGLE_SIZE = 8; // 50节三角形大小
  
  let path = `M 0 0 L 0 -${BARB_HEIGHT}`; // 杆身
  
  let remainingSpeed = Math.round(speed);
  let position = BARB_GAP; // 第一个风羽位置
  
  // 处理50节以上的风速（三角形标志）
  while (remainingSpeed >= 50) {
    const y = -BARB_HEIGHT;
    path += ` M 0 ${y} L -${TRIANGLE_SIZE} ${y + TRIANGLE_SIZE} L ${TRIANGLE_SIZE} ${y + TRIANGLE_SIZE} Z`;
    remainingSpeed -= 50;
  }
  
  // 处理10节的风速（长线）
  while (remainingSpeed >= 10) {
    path += ` M 0 -${position} L ${LONG_BARB_LENGTH} -${position}`;
    remainingSpeed -= 10;
    position += BARB_GAP;
  }
  
  // 处理5节的风速（短线）
  if (remainingSpeed >= 5) {
    path += ` M 0 -${position} L ${SHORT_BARB_LENGTH} -${position}`;
    position += BARB_GAP;
  }
  
  // 添加自定义拐点（杆顶的圆形）
  path += ` M 0 -${BARB_HEIGHT} a 3 3 0 1 0 6 0 a 3 3 0 1 0 -6 0`;
  
  // 计算旋转角度（气象学角度转换为数学角度）
  const rotateAngle = 270 - direction; // 转换为数学坐标系角度
  console.log(path)
  return {
    path: path,
    rotation: rotateAngle * Math.PI / 180 // 转换为弧度
  };
}
setOptions({
  xAxis: {
  type: 'category',
        data: ['北京', '上海', '广州', '成都', '乌鲁木齐'],
        axisLabel: {
            interval: 0
        }
  },
 yAxis: {
        type: 'category',
       data: [1,2,3,4,5,6,7,8,9,0,11],
        axisLabel: {
            interval: 0
        }
    },
  series: [
    {
       type: 'custom',
       renderItem: function(params, api) {
      const coords = api.coord([api.value(0), api.value(1)]);
      return {
        type: 'path',
        shape: {
          pathData:windBarbData.path
     }
      };
    },
        data: [[120, 30, 65, 45]] // 经度, 纬度, 风速, 风向
    }
  ]
})
  // renderItem: function(params, api) {
  //           const stationIndex = api.value(0);
  //           const windSpeed = Number(api.value(2));
  //           const windDirection = Number(api.value(3));
            
  //           // 获取坐标点位置
  //           const point = api.coord([api.value(0), api.value(1)]);
            
  //           // 风向杆参数配置
  //           const config = {
  //               lineLength: 25,          // 主线长度
  //               circleRadius: 2,        // 端点圆半径
  //               longFeatherLength: 6,    // 长羽毛长度
  //               shortFeatherLength: 3,   // 短羽毛长度
  //               featherGap: 5,           // 羽毛间距
  //               calmCircleRadius: 3      // 静风圆圈半径
  //           };
            
  //           // 计算风向（转换为弧度，0°为北风，顺时针增加）
  //           const direction = (270 - windDirection) * Math.PI / 180;
  //           const perpendicular = direction + Math.PI / 2; // 垂直方向（羽毛方向）
            
  //           // 计算羽毛数量
  //           const longFeathers = Math.floor(windSpeed / 4);
  //           const remaining = windSpeed % 4;
  //           const shortFeathers = Math.floor(remaining / 2);
  //           const hasSmallLine = (windSpeed > 0 && windSpeed < 2);
            
  //           // 生成主线段Path
  //           const endPoint = [
  //               point[0] + config.lineLength * Math.cos(direction),
  //               point[1] + config.lineLength * Math.sin(direction)
  //           ];
            
  //           let pathData = [];
            
  //           // 1. 绘制主线
  //           pathData.push([
  //               'M', point[0], point[1],
  //               'L', endPoint[0], endPoint[1]
  //           ].join(' '));
            
  //           // 2. 绘制端点圆
  //           pathData.push([
  //               'M', endPoint[0] + config.circleRadius, endPoint[1],
  //               'A', config.circleRadius, config.circleRadius, 0, 1, 0,
  //               endPoint[0] - config.circleRadius, endPoint[1],
  //               'A', config.circleRadius, config.circleRadius, 0, 1, 0,
  //               endPoint[0] + config.circleRadius, endPoint[1],
  //               'Z'
  //           ].join(' '));
            
  //           // 3. 绘制长羽毛
  //           for (let i = 1; i <= longFeathers; i++) {
  //               const featherPos = [
  //                   point[0] + (config.lineLength - i * config.featherGap) * Math.cos(direction),
  //                   point[1] + (config.lineLength - i * config.featherGap) * Math.sin(direction)
  //               ];
                
  //               const featherEnd = [
  //                   featherPos[0] + config.longFeatherLength * Math.cos(perpendicular),
  //                   featherPos[1] + config.longFeatherLength * Math.sin(perpendicular)
  //               ];
                
  //               pathData.push([
  //                   'M', featherPos[0], featherPos[1],
  //                   'L', featherEnd[0], featherEnd[1]
  //               ].join(' '));
  //           }
            
  //           // 4. 绘制短羽毛
  //           for (let i = 0; i < shortFeathers; i++) {
  //               const featherPos = [
  //                   point[0] + (config.lineLength - longFeathers * config.featherGap - i * config.featherGap) * Math.cos(direction),
  //                   point[1] + (config.lineLength - longFeathers * config.featherGap - i * config.featherGap) * Math.sin(direction)
  //               ];
                
  //               const featherEnd = [
  //                   featherPos[0] + config.shortFeatherLength * Math.cos(perpendicular),
  //                   featherPos[1] + config.shortFeatherLength * Math.sin(perpendicular)
  //               ];
                
  //               pathData.push([
  //                   'M', featherPos[0], featherPos[1],
  //                   'L', featherEnd[0], featherEnd[1]
  //               ].join(' '));
  //           }
            
  //           // 5. 绘制小短线（风速<2m/s）
  //           if (hasSmallLine) {
  //               const featherPos = [
  //                   point[0] + (config.lineLength - config.featherGap) * Math.cos(direction),
  //                   point[1] + (config.lineLength - config.featherGap) * Math.sin(direction)
  //               ];
                
  //               const featherEnd = [
  //                   featherPos[0] + config.shortFeatherLength/2 * Math.cos(perpendicular),
  //                   featherPos[1] + config.shortFeatherLength/2 * Math.sin(perpendicular)
  //               ];
                
  //               pathData.push([
  //                   'M', featherPos[0], featherPos[1],
  //                   'L', featherEnd[0], featherEnd[1]
  //               ].join(' '));
  //           }
            
  //           // 6. 绘制静风圆圈（风速=0）
  //           if (windSpeed === 0) {
  //               const circleCenter = [
  //                   point[0] + config.lineLength/2 * Math.cos(direction),
  //                   point[1] + config.lineLength/2 * Math.sin(direction)
  //               ];
                
  //               pathData.push([
  //                   'M', circleCenter[0] + config.calmCircleRadius, circleCenter[1],
  //                   'A', config.calmCircleRadius, config.calmCircleRadius, 0, 1, 0,
  //                   circleCenter[0] - config.calmCircleRadius, circleCenter[1],
  //                   'A', config.calmCircleRadius, config.calmCircleRadius, 0, 1, 0,
  //                   circleCenter[0] + config.calmCircleRadius, circleCenter[1],
  //                   'Z'
  //               ].join(' '));
  //           }
            
  //           return {
  //               type: 'path',
  //               shape: {
  //                   pathData: pathData.join(' ')
  //               },
  //               style: {
  //                   fill:  '#333', 
  //                   stroke: '#333',
  //                   lineWidth: 1
  //               },
  //               emphasis: {
  //                   style: {
  //                       stroke: '#000',
  //                       lineWidth: 2
  //                   }
  //               }
  //           };
  //       },
</script>

<template>
<div class="chart" ref="echart"></div>
</template>
<style lang="scss">
.chart{
  width: 300px;
  height: 400px;
}
</style>
