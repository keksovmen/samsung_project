class Sensor:
	def __init__(self, name, description, value_type, tid, min_value, max_value, id) -> None:
		self.name = name
		self.description = description
		self.value_type = value_type
		self.tid = tid
		self.min_value = min_value
		self.max_value = max_value
		self.id = id