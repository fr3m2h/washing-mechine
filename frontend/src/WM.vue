<script setup>

import { ref, onMounted } from 'vue';
import { HOST } from './config.js'

const props = defineProps(['wm']);

// Function to toggle the "not working" state
const toggleMachineState = async () => {
  const newIsHS = !props.wm.isHS; // Toggle the current isHS state

  try {
    const response = await fetch(`${HOST}/api/machines/${props.wm.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        name: props.wm.name,
        isUsed: props.wm.isUsed,
        timeLeft: props.wm.timeLeft,
        isHS: newIsHS,
      }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    if (data.isHS == newIsHS) {
      props.wm.isHS = newIsHS; // Update the local state to reflect the change
    }
  } catch (error) {
    console.error('Failed to update machine state:', error);
  }
};
</script>


<template>
  <div class="machine-display" :data-is-used="wm.isUsed" :data-is-hs="wm.isHS">
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
    <button class="toggle-state-button" @click="toggleMachineState">
      {{ wm.isHS ? 'Mark as Working' : 'Mark as Not Working' }}
    </button>
  </div>
</template>

<style lang="scss" scoped>
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

/* Dynamic border color and background based on isUsed */
.machine-display[data-is-used="false"] {
  border-top-color: #4caf50;
  /* Green for "Available" */
  background-color: #f9f9f9;
}

/* New styling for machines marked as not working (isHS: true) */
.machine-display[data-is-hs="true"] {
  border-top-color: #000000;
  /* Black border for "Not Working" */
  background-color: #333333;
  /* Black background for "Not Working" */
  color: #ffffff;
  /* White text for contrast */
}

/* Machine Title */
.machine-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
  color: inherit;
  /* Inherit text color from parent */
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
  color: inherit;
  /* Inherit text color from parent */
  font-weight: bold;
}

.ready-to-use {
  font-size: 1rem;
  color: #4caf50;
  /* Green for readiness */
  font-weight: bold;
  margin-top: 10px;
}

/* Toggle State Button */
.toggle-state-button {
  margin-top: 15px;
  padding: 10px 15px;
  font-size: 0.9rem;
  font-weight: bold;
  color: #fff;
  background-color: #ff5722;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.toggle-state-button:hover {
  background-color: #e64a19;
}

/* Toggle State Button when machine is marked as not working */
.machine-display[data-is-hs="true"] .toggle-state-button {
  background-color: #757575;
  /* Gray button for "Not Working" */
}

.machine-display[data-is-hs="true"] .toggle-state-button:hover {
  background-color: #616161;
  /* Darker gray on hover */
}
</style>
