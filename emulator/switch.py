class Switch:
	def __init__(self, name, tid, id, sensors_ids, description, state, property_ids) -> None:
		self.name = name
		self.tid = tid
		self.id = id
		self.sensors_ids = sensors_ids
		self.description = description
		self.state = state
		self.property_ids = property_ids