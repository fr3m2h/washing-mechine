<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { HOST } from './config.js'
import WM from './WM.vue'
import WMStatusFilter from './WMStatusFilter.vue'

const wmList = ref([]);
const selectedFilter = ref('all');
let intervalId;

const fetchMachines = async () => {
  try {
    const response = await fetch(`${HOST}/api/machines`);
    wmList.value = await response.json();
  } catch (error) {
    console.error("Failed to fetch machines:", error);
  }
};

onMounted(() => {
  fetchMachines();
  intervalId = setInterval(fetchMachines, 5000);
});

onUnmounted(() => {
  clearInterval(intervalId);
});

const handleFilterChanged = (filter) => {
  selectedFilter.value = filter;
};

const filteredwmList = computed(() => {
  if (selectedFilter.value === 'all') return wmList.value;
  return wmList.value.filter((wm) => {
    if (selectedFilter.value == "in use") {
      return wm.isUsed;
    } else {
      return !wm.isUsed;
    }
  });
});
</script>
<template>
  <WMStatusFilter @filterChanged="handleFilterChanged" />
  <div class="wms-container">
    <WM v-for="wm in filteredwmList" :key="wm.id" :wm="wm"></WM>
  </div>
</template>

<style lang="scss" scoped>
/* styles.css */
body {
  font-family: Arial, sans-serif;
  background: #f4f4f9;
  margin: 0;
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.wms-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}
</style>
