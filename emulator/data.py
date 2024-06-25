from box import Box
from sensor import Sensor
from switch import Switch
from property import Property
from tid import Tid
import datetime
import random



test = Tid.START_TIME

properties = [
	Property("Start time", "Time to start something. Measured in seconds during a day, 0 = 00:00:00, 86399 = 23:59:59", "float", 0, 86399, Tid.START_TIME.value, 0, 0),
	Property("End time", "Time to end something. Measured in seconds during a day, 0 = 00:00:00, 86399 = 23:59:59", "float", 0, 86399, Tid.END_TIME.value, 1, 100),
	Property("Min temp", "Minimal temperature to turn something on. Measured in celsius", "float", -256.0, 256.0, Tid.MIN_TEMPERATURE.value, 2, 0),
	Property("Target temp", "Temperature to turn something off. Measured in celsius", "float", -256.0, 256.0, Tid.TARGET_TEMPERATURE.value, 3, 10),
	Property("Target humidity", "Required humidity. Measured in %", "float", 0, 100, Tid.TARGET_HUMIDITY.value, 4, 80),
	Property("Sensor check period", "Measured in seconds, Controls how much data will be generated", "float", 5, 600, Tid.SENSORS_CHECK_PERIOD.value, 5, 60),
]

sensors = [
	Sensor("Inside temperature", "Measured in celsius", "float", Tid.TEMPERATURE.value, -256.0, 256.0, 0),
	Sensor("Inside humidity", "Measured in %", "float", Tid.HUMIDITY.value, 0, 100, 1),
	Sensor("Inside pressure", "Measured in kPa", "float", Tid.PRESSURE.value, 0.0, 10000.0, 2),
]

switches = [
	Switch("Light bulb", Tid.LIGHT.value, 3, [], "Turns light on or off", 0, [0, 1]),
	Switch("Heating", Tid.HEATING.value, 4, [0], "Turns heating on or off", 0, [2, 3]),
	Switch("Venting", Tid.VENTING.value, 5, [1], "Turns venting on or off", 0, [3, 4]),
]

box = Box("Test box", [x.id for x in sensors], [x.id for x in switches], [x.id for x in properties], 1, 1)



class DataEntry:
	def __init__(self, value, timestamp) -> None:
		self.value = value
		self.timestamp = timestamp



def generate_sensor_data(sensor: Sensor, time: int):
	current_time = int(datetime.datetime.now().timestamp())
	data_entries = random.randint(0, 10)
	if data_entries == 0:
		return []
	delta_time = current_time - time
	result = []
	for _ in range(data_entries):
		# value = random.randint(sensor.min_value, sensor.max_value) if sensor.value_type == "int" else random.random()
		result.append(DataEntry(random.randint(sensor.min_value, sensor.max_value), random.randint(time, time + delta_time)))
	
	return result

def generate_switch_data(swt: Switch, time: int):
	current_time = int(datetime.datetime.now().timestamp())
	data_entries = random.randint(0, 10)
	if data_entries == 0:
		return []
	delta_time = current_time - time
	result = []
	for _ in range(data_entries):
		# value = random.randint(sensor.min_value, sensor.max_value) if sensor.value_type == "int" else random.random()
		result.append(DataEntry(random.randint(0, 1), random.randint(time, time + delta_time)))
	
	return result