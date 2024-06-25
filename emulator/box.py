class Box:
	def __init__(self, name, sensor_ids, switch_ids, properties_ids, version, unique_id) -> None:
		self.name = name
		self.sensor_ids = sensor_ids
		self.switch_ids = switch_ids
		self.properties_ids = properties_ids
		self.version = version
		self.unique_id = unique_id