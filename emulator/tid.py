from enum import Enum



class Tid(Enum):
	TEMPERATURE = 1
	PRESSURE = 2
	HUMIDITY = 3

	LIGHT = 256
	HEATING = 257
	VENTING = 258

	START_TIME = 512
	END_TIME = 513
	MIN_TEMPERATURE = 514
	TARGET_TEMPERATURE = 515
	TARGET_HUMIDITY = 516
	
	SENSORS_CHECK_PERIOD = 1024