# Washing-MEchine

## Introduction

This project aims to develop a web and mobile interface to monitor the availability of washing machines at our studing housing laundry before going there to avoid wasting time and energy.

### Embedded System

We installed a voltage sensors on the washing machines to detect their status based on power consumption.
The microcontroller then transmits this data to our backend API.

#### Components:
- **Potentiometer**: Simulates machine activation by varying current.
- **Motor**: Represents the washing machine's operation.
- **Power Supply**: 3.3V.
- **Current Sensor**: Detects motor activity.
- **ESP32 Feather**: Handles data transmission.

#### Wiring:
- Power **+ →** Potentiometer Pin 3
- Power **- →** Ground (GND)
- Potentiometer **Pin 1 →** GND
- Potentiometer **Pin 2 →** Sensor IN+
- Sensor IN+ **→** Potentiometer Pin 2
- Sensor IN- **→** Motor +
- Sensor 5V **→** Power +
- Sensor GND **→** GND
- Motor **+ →** Sensor OUT
- Motor **- →** GND
- ESP32 **A10/27 →** Sensor OUTDATA
- ESP32 **GND →** GND

### Backend

The backend serves as a database to store machine activity, remaining wash time, date of use...

### Web Frontend

The application and website display collected data, such as active machines, remaining wash times, and peak usage periods.
