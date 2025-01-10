<script setup>

import { ref, onMounted } from 'vue';
import { HOST } from './config.js'

const props = defineProps(['wm']);

</script>

<template>
  <div class="machine-display" :data-is-used="wm.isUsed">
    <div class="machine-title">{{ wm.name }}</div>
    <div class="machine-status">
      <span class="status-label" :class="{ 'in-use': wm.isUsed, 'available': !wm.isUsed }">
        {{ wm.isUsed ? 'In Use' : 'Available' }}
      </span>
    </div>
    <div class="time-info">
      <div class="time-left" v-if="wm.isUsed">
        Time Left: <span>{{ wm.timeLeft }}mn</span>
      </div>
      <div class="ready-to-use" v-else>
        Ready to Use
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
/* General Body Styling */
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

/* Container for all washing machine displays */
.container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

/* Individual washing machine card */
.machine-display {
  width: 250px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 15px;
  text-align: center;
  border-top: 5px solid #f44336;
  /* Default: Red for "In Use" */
  transition: border-color 0.3s ease, background-color 0.3s ease;
}

/* Dynamic border color based on isUsed */
.machine-display[data-is-used="false"] {
  border-top-color: #4caf50;
  /* Green for "Available" */
  background-color: #f9f9f9;
}

/* Machine Title */
.machine-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

/* Status Section */
.machine-status {
  margin-bottom: 10px;
  font-size: 1rem;
}

/* Dynamic Status Label */
.status-label {
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 0.9rem;
  display: inline-block;
  color: #fff;
  transition: background-color 0.3s ease;
}

.status-label.in-use {
  background-color: #f44336;
  /* Red for "In Use" */
}

.status-label.available {
  background-color: #4caf50;
  /* Green for "Available" */
}

/* Time Left Section */
.time-left {
  font-size: 1rem;
  color: #555;
  font-weight: bold;
}

.ready-to-use {
  font-size: 1rem;
  color: #4caf50;
  /* Green for readiness */
  font-weight: bold;
  margin-top: 10px;
}

/* Responsive Styles */
@media (max-width: 768px) {
  .container {
    flex-direction: column;
    align-items: center;
  }

  .machine-display {
    width: 90%;
  }
}

@media (min-width: 769px) and (max-width: 1200px) {
  .machine-display {
    width: 45%;
  }
}
</style>
