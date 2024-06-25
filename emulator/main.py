from tid import Tid
from property import Property
import json




if __name__ == "__main__":
	p = Property("Start time", "Time to start something. Measured in seconds during a day, 0 = 00:00:00, 86399 = 23:59:59", "int", 0, 86399, Tid.START_TIME.value, 0)

	print(json.dumps(p.__dict__))