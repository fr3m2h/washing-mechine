<script setup>
import { ref } from 'vue';

const emit = defineEmits(['filterChanged']);
const selectedStatus = ref('All');

const selectStatus = (status) => {
  selectedStatus.value = status;
  emit('filterChanged', status.toLowerCase());
};
</script>

<template>
  <div class="filter-container">
    <div class="filter-option" v-for="status in ['All', 'Available', 'In Use']" :key="status"
      :class="{ active: selectedStatus === status }" @click="selectStatus(status)">
      {{ status }}
    </div>
  </div>
</template>

<style scoped>
.filter-container {
  display: flex;
  justify-content: center;
  margin-bottom: 15px;
}

.filter-option {
  cursor: pointer;
  padding: 10px 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f0f0f0;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.filter-option.active {
  background-color: #4CAF50;
  color: white;
  border-color: #0056b3;
}

.filter-option:hover {
  background-color: #4CAF50;
}
</style>
